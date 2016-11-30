package org.web3j.protocol.infura;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.ssl.SSLContexts;

import org.web3j.protocol.http.HttpService;

/**
 * HttpService for working with <a href="https://infura.io/">Infura</a> clients.
 */
public class InfuraHttpService extends HttpService {

    private static final String INFURA_ETHEREUM_PREFERRED_CLIENT =
            "Infura-Ethereum-Preferred-Client";

    private static final char[] TEMP_KEY_STORE_PASSWORD = "web3j runtime cert store".toCharArray();

    private final Header clientVersionHeader;

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
        if (clientVersionHeader != null) {
            headers.add(clientVersionHeader);
        }
    }

    static Header buildHeader(String clientVersion, boolean required) {
        if (clientVersion == null || clientVersion.equals("")) {
            return null;
        }

        if (required) {
            return new BasicHeader(INFURA_ETHEREUM_PREFERRED_CLIENT, clientVersion);
        } else {
            return new BasicHeader(
                    INFURA_ETHEREUM_PREFERRED_CLIENT, clientVersion + "; required=false");
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
                .setSslcontext(sslContext)
                .build();
    }
}
