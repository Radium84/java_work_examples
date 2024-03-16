package realwork;

/*
mTLS config для reactor.netty.http.client.HttpClient
*/

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Пример конфигурирования mTls для  reactive.function.client.WebClient
 */
@Component
public class WebClientConfig {
    @Value("${server.ssl.trust-store}")
    String trustStorePath;
    @Value("${server.ssl.trust-store-password}")
    String trustStorePass;
    @Value("${server.ssl.key-store}")
    String keyStorePath;
    @Value("${server.ssl.key-store-password}")
    String keyStorePass;

    public WebClient getWebClient() {

        SslContext sslContext;
        final PrivateKey privateKey;
        final X509Certificate[] certificates;
        try {
            final KeyStore trustStore;
            final KeyStore keyStore;
            trustStore = KeyStore.getInstance("PKCS12");
            trustStore.load(new FileInputStream(ResourceUtils.getFile(trustStorePath)), trustStorePass.toCharArray());
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(ResourceUtils.getFile(keyStorePath)), keyStorePass.toCharArray());
            String keyAlias = keyStore.aliases().nextElement();
            List<Certificate> certificateList = Collections.list(trustStore.aliases())
                    .stream()
                    .filter(t -> {
                        try {
                            return trustStore.isCertificateEntry(t);
                        } catch (KeyStoreException e1) {
                            throw new RuntimeException("Error reading truststore", e1);
                        }
                    })
                    .map(t -> {
                        try {
                            return trustStore.getCertificate(t);
                        } catch (KeyStoreException e2) {
                            throw new RuntimeException("Error reading truststore", e2);
                        }
                    }).toList();
            certificates = certificateList.toArray(new X509Certificate[0]);
            privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyStorePass.toCharArray());
            Certificate[] certChain = keyStore.getCertificateChain(keyAlias);
            X509Certificate[] x509CertificateChain = Arrays.stream(certChain)
                    .map(certificate -> (X509Certificate) certificate)
                    .toArray(X509Certificate[]::new);

            sslContext = SslContextBuilder.forClient()
                    .keyManager(privateKey, keyStorePass, x509CertificateChain)
                    .trustManager(certificates)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
            ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
            return WebClient.builder()
                    .clientConnector(connector)
                    .build();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException |
                UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
