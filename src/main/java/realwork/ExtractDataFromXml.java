package realwork;

import idm.rest.dto.AddedToCardData;
import idm.rest.dto.AddedToRoleObjects;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Пример разбора сложного Xml и создания на его основы структуры данных
 */
@Component
@Slf4j
public class ExtractDataFromXml {
    @Value("${idm.url}")
    private String idmUrl;
    @Value("${idm.login}")
    private String idmLogin;
    @Value("${idm.password}")
    private String idmPassword;


    public String getIdmUrl() {
        return idmUrl;
    }

    public String getIdmLogin() {
        return idmLogin;
    }

    public String getIdmPassword() {
        return idmPassword;
    }

    public AddedToCardData extractAddedDataFromCard(String xml) {
        try {
            AddedToCardData addedToCardData = new AddedToCardData();
            //Ищем проекции
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml));
            Namespace ns = Namespace.getNamespace("def", "http://midpoint.evolveum.com/xml/ns/public/common/common-3");
            List<Element> linkRefElements = document.getRootElement().getChildren("linkRef", ns);
            List<String> cardShadows = linkRefElements.stream()
                    .filter(element -> element.getAttributeValue("type").equals("c:ShadowType"))
                    .map(element -> element.getAttributeValue("oid"))
                    .toList();
            log.info("Shadow oid " + cardShadows);
            addedToCardData.setShadowType(cardShadows);
            //Ищем роль родителя
            List<Element> assignments = document.getRootElement().getChildren("assignment", ns);
            Map<String, Boolean> parentRoleList = assignments.stream()
                    .filter(element -> {
                        Element targetRefElement = element.getChild("targetRef", ns);
                        return targetRefElement != null && "c:RoleType".equals(targetRefElement.getAttributeValue("type"));
                    })
                    .collect(Collectors.toMap(
                            element -> element.getChild("targetRef", ns).getAttributeValue("oid"),
                            element -> {
                                Element activationElement = element.getChild("activation", ns);
                                return activationElement != null && "enabled".equals(activationElement.getChildText("effectiveStatus", ns));
                            }
                    ));

            log.info("Parent role oid " + parentRoleList);
            addedToCardData.setParentRoleType(parentRoleList);

            //Ищем роли, навешенные через родителя
            List<Element> roleMembershipRef = document.getRootElement().getChildren("roleMembershipRef", ns);
            List<String> roleTypeList = roleMembershipRef.stream()
                    .filter(element -> element.getAttributeValue("type").equals("c:RoleType"))
                    .map(element -> element.getAttributeValue("oid"))
                    .toList();
            log.info("Role oid " + roleTypeList);
            addedToCardData.setRolesType(roleTypeList);

            return addedToCardData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AddedToRoleObjects extractAssignedObjectsFromRole(String xml) {
        try {
            AddedToRoleObjects addedToRoleObjects = new AddedToRoleObjects();
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml));
            Namespace ns = Namespace.getNamespace("http://midpoint.evolveum.com/xml/ns/public/common/common-3");
            List<Element> inducements = document.getRootElement().getChildren("inducement", ns);
            List<String> resources = inducements.stream()
                    .filter(element -> element.getChild("construction", ns) != null)
                    .map(element -> element.getChild("construction", ns).getChild("resourceRef", ns).getAttributeValue("oid"))
                    .toList();
            log.info("Assigned to Role Resources" + resources);
            addedToRoleObjects.setResources(resources);

            List<String> targetRef = inducements.stream()
                    .filter(element -> element.getChild("targetRef", ns) != null)
                    .map(element -> element.getChild("targetRef", ns).getAttributeValue("oid"))
                    .toList();
            log.info("Assigned to Role RoleType" + targetRef);
            addedToRoleObjects.setRoles(targetRef);
            return addedToRoleObjects;

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("При обработке запроса на извлечение списка ресурсов обнаружена ошибка" + e.getMessage());
        }

    }

    public String extractLiteDataFromRole(String xml) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml));
            Namespace ns = Namespace.getNamespace("http://midpoint.evolveum.com/xml/ns/public/common/common-3");
            // Находим элемент construction
            Element constructionElement = document.getRootElement().getChild("inducement", ns).getChild("construction", ns);

            // Теперь ищем элемент value внутри construction
            Element attributeElement = constructionElement.getChild("attribute", constructionElement.getNamespace());
            if (attributeElement != null) {
                Element outboundElement = attributeElement.getChild("outbound", attributeElement.getNamespace());
                if (outboundElement != null) {
                    Element expressionElement = outboundElement.getChild("expression", outboundElement.getNamespace());
                    if (expressionElement != null) {
                        Element valueElement = expressionElement.getChild("value", expressionElement.getNamespace());
                        if (valueElement != null) {
                            return valueElement.getText();
                        }
                    }
                }
            }
            throw new RuntimeException("Элемент value не найден в XML");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("При обработке запроса на извлечение списка ресурсов обнаружена ошибка: " + e.getMessage(), e);
        }
    }

    public String extractResourceFromShadow(String xml) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml));
            Namespace ns = Namespace.getNamespace("def", "http://midpoint.evolveum.com/xml/ns/public/common/common-3");
            List<Element> operationExecution = document.getRootElement().getChildren("operationExecution", ns);
            return operationExecution.stream()
                    .filter(operationExecutions -> operationExecutions.getChild("operation", ns) != null)
                    .map(operationExecutions -> operationExecutions.getChild("operation", ns).getChildText("resourceOid", ns))
                    .findFirst().get();

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("При обработке запроса на извлечение списка ресурсов обнаружена ошибка" + e.getMessage());
        }

    }
    public Set<String> extractAttributeListFromResource(String xml){
        try{
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml));
            Element rootElement = document.getRootElement();
            Namespace ns = rootElement.getNamespace();
            Set<String> refValues = rootElement.getChildren("schemaHandling", ns).stream()
                    .flatMap(schema -> schema.getChildren("objectType", ns).stream())
                    .flatMap(objectType -> objectType.getChildren("attribute", ns).stream())
                    .map(attribute -> attribute.getChild("ref", ns))
                    .filter(Objects::nonNull)
                    .map(Element::getTextNormalize)
                    .map(element->element.substring(3))
                    .collect(Collectors.toSet());
            return refValues;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("При обработке запроса на извлечение списка ресурсов обнаружена ошибка" + e.getMessage());
        }

    }
    public Set<String> extractAttributeListFromShadow(String xml){
        try{
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(new StringReader(xml));
            Element rootElement = document.getRootElement();
            Namespace ns = rootElement.getNamespace();
            return rootElement.getChild("attributes", ns)
                    .getChildren() // Получаем всех потомков в namespace ri, не фильтруя по конкретному имени
                    .stream()
                    .map(Element::getName) // Преобразуем элементы в их имена
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("При обработке запроса на извлечение списка ресурсов обнаружена ошибка" + e.getMessage());
        }

    }


}