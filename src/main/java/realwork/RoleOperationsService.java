package realwork;

import idm.config.WebClientConfig;
import idm.rest.utils.Matchers;
import idm.rest.utils.ExtractDataFromXml;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Function;
@Slf4j
@Service
public class RoleOperationsService extends ExtractDataFromXml implements Matchers {

    public final RestTemplate restTemplate;
    public final String idmUrl;
    public final String login;
    public final String password;
    public final WebClientConfig webClientConfig;

    public RoleOperationsService(RestTemplate restTemplate, ExtractDataFromXml extractDataFromXml, WebClientConfig webClientConfig) {
        this.restTemplate = restTemplate;
        this.idmUrl = extractDataFromXml.getIdmUrl();
        this.login = extractDataFromXml.getIdmLogin();
        this.password = extractDataFromXml.getIdmPassword();
        this.webClientConfig = webClientConfig;
    }
    public HttpStatusCode addRoleToCardASAdmin(String oid, String roleId) {
        String bodyXml = String.format(addRoleBody, roleId);
        return sendRequest(oid, bodyXml, login, password);
    }

    public HttpStatusCode addRoleToCardASTyz(String oid, String roleId, String tyzLogin, String tyzPassword) {
        String bodyXml = String.format(addRoleBody, roleId);
        return sendRequest(oid, bodyXml, tyzLogin, tyzPassword);
    }

    public HttpStatusCode deleteRoleFromCard(String oid, String roleId) {
        String bodyXml = String.format(deleteRoleBody, roleId);
        return sendRequest(oid, bodyXml, login, password);
    }

    public HttpStatusCode deleteRoleFromCardAsTyz(String oid, String roleId, String tyzLogin, String tyzPassword) {
        String bodyXml = String.format(deleteRoleBody, roleId);
        return sendRequest(oid, bodyXml, tyzLogin, tyzPassword);
    }
    private HttpStatusCode sendRequest(String oid, String body, String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(login, password);
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(idmUrl + "/users/" + oid, HttpMethod.PATCH, entity, String.class);
            return response.getStatusCode();
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
            return e.getStatusCode();
        }
    }

    public String getLiteDataFromRole(@PathVariable("role") String role) {
        try {
            WebClient webClient = webClientConfig.getWebClient();
            final String response = webClient.get()
                    .uri(String.format("%s/roles/%s", idmUrl, role))
                    .headers(httpHeaders -> httpHeaders.setBasicAuth(login, password))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return extractLiteDataFromRole(response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String getRoleOid(String name, Function<String, String> responseHandler) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(login, password);
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<>(String.format("<query><filter><equal>" +
                "<path>name</path><value>%s</value></equal></filter></query>", name), headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(idmUrl + "/roles/search", HttpMethod.POST, entity, String.class);
            return responseHandler.apply(response.getBody());
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage());
            return e.getResponseBodyAsString();
        }
    }

    String addRoleBody = """
                <objectModification xmlns='http://midpoint.evolveum.com/xml/ns/public/common/api-types-3'
                 xmlns:c='http://midpoint.evolveum.com/xml/ns/public/common/common-3'
                 xmlns:t="http://prism.evolveum.com/xml/ns/public/types-3">
                 <itemDelta>
                 <t:modificationType>add</t:modificationType>
                 <t:path>c:assignment</t:path>
                 <t:value>
                 <c:targetRef oid="%s" type="c:RoleType"/>
                 </t:value>
                 </itemDelta>
                 </objectModification>""";
    String deleteRoleBody = """
                <objectModification xmlns='http://midpoint.evolveum.com/xml/ns/public/common/api-types-3'
                 xmlns:c='http://midpoint.evolveum.com/xml/ns/public/common/common-3'
                 xmlns:t="http://prism.evolveum.com/xml/ns/public/types-3">
                 <itemDelta>
                 <t:modificationType>delete</t:modificationType>
                 <t:path>c:assignment</t:path>
                 <t:value>
                 <c:targetRef oid="%s" type="c:RoleType"/>
                 </t:value>
                 </itemDelta>
                 </objectModification>""";
}
