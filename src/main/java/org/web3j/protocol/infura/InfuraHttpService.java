package org.web3j.protocol.infura;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Optional;

import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;

import org.web3j.protocol.http.HttpService;

/**
 * HttpService for working with <a href="https://infura.io/">Infura</a> clients.
 */
public class InfuraHttpService extends HttpService {

    private static final String INFURA_ETHEREUM_PREFERRED_CLIENT =
            "Infura-Ethereum-Preferred-Client";

    private static final char[] TEMP_KEY_STORE_PASSWORD = "web3j runtime cert store".toCharArray();

    private final Optional<Header> clientVersionHeader;

    public InfuraHttpService(String url, String clientVersion, boolean required) {
        super(url);
        setHttpClient(createTrustTlsHttpClient(url));
        clientVersionHeader = buildHeader(clientVersion, required);
    }

    public InfuraHttpService(String url, String clientVersion) {
        this(url, clientVersion, true);
    }

    public InfuraHttpService(String url) {
        this(url, "", false);
    }

    @Override
    protected void addHeaders(List<Header> headers) {
        if (clientVersionHeader.isPresent()) {
            headers.add(clientVersionHeader.get());
        }
    }

    static Optional<Header> buildHeader(String clientVersion, boolean required) {
        if (clientVersion == null || clientVersion.equals("")) {
            return Optional.empty();
        }

        if (required) {
            return Optional.of(new BasicHeader(INFURA_ETHEREUM_PREFERRED_CLIENT, clientVersion));
        } else {
            return Optional.of(new BasicHeader(
                    INFURA_ETHEREUM_PREFERRED_CLIENT, clientVersion + "; required=false"));
        }
    }

    /**
     * Create an {@link InfuraHttpService} instance with a local keystore that implicitly trusts
     * the provided endpoint.
     *
     * This is achieved by creating a local temporary keystore file which we add the certificate
     * of the endpoint to upon application startup.
     *
     * @param url we wish to connect to
     * @return the file containing the keystore
     * @throws UnrecoverableKeyException if keystore file cannot be loaded
     * @throws NoSuchAlgorithmException if keystore file cannot be loaded
     * @throws KeyStoreException if keystore file cannot be loaded
     * @throws KeyManagementException if keystore file cannot be loaded
     */
    private static CloseableHttpClient createTrustTlsHttpClient(String url) {

        File keyFile = CertificateManager.buildKeyStore(url, TEMP_KEY_STORE_PASSWORD);

        SSLContext sslContext;
        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(keyFile, TEMP_KEY_STORE_PASSWORD)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return HttpClients.custom()
                .setConnectionManagerShared(true)
                .setSSLContext(sslContext)
                .build();
    }
}
