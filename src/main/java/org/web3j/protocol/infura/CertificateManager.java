package org.web3j.protocol.infura;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Certificate manager to simplify working with TLS endpoints.
 */
public class CertificateManager {

    static File buildKeyStore(String url, char[] keyStorePassword) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, keyStorePassword);

            CertificateChainTrustManager certificateChainTrustManager =
                    createCertificateChainTrustManager(keyStore);
            URI endpoint = new URI(url);
            SSLSocket sslSocket = createSslSocket(endpoint, certificateChainTrustManager);

            if (!isTrustedEndPoint(sslSocket)) {
                X509Certificate[] x509Certificates = certificateChainTrustManager.x509Certificates;
                if (x509Certificates == null) {
                    throw new RuntimeException("Unable to obtain x509 certificate from server");
                }

                for (int i = 0; i < x509Certificates.length; i++) {
                    keyStore.setCertificateEntry(endpoint.getHost() + i, x509Certificates[i]);
                }
            }

            SecureRandom random = new SecureRandom();
            File keyFile = File.createTempFile("web3j-", "" + random.nextLong());

            FileOutputStream fileOutputStream = new FileOutputStream(keyFile);
            keyStore.store(fileOutputStream, keyStorePassword);
            fileOutputStream.close();

            deleteFileOnShutdown(keyFile);

            return keyFile;

        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static CertificateChainTrustManager createCertificateChainTrustManager(
            KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {

        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        X509TrustManager defaultTrustManager =
                (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
        return new CertificateChainTrustManager(defaultTrustManager);
    }

    private static SSLSocket createSslSocket(
            URI endpoint, CertificateChainTrustManager certificateChainTrustManager)
            throws NoSuchAlgorithmException, KeyManagementException, IOException,
            URISyntaxException, KeyStoreException {

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{certificateChainTrustManager}, null);
        SSLSocketFactory factory = context.getSocketFactory();

        SSLSocket sslSocket = (SSLSocket) factory.createSocket(
                endpoint.getHost(), 443);
        sslSocket.setSoTimeout(10000);

        return sslSocket;
    }

    private static boolean isTrustedEndPoint(SSLSocket socket) throws IOException {
        try {
            socket.startHandshake();
            socket.close();
            return true;
        } catch (SSLException e) {
            return false;
        }
    }

    private static void deleteFileOnShutdown(final File file) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    deleteTempFile(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void deleteTempFile(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("Unable to remove file: " + file.getCanonicalPath());
        }
    }

    /**
     * Based on:
     * http://blogs.sun.com/andreas/resource/InstallCert.java
     */
    private static class CertificateChainTrustManager implements X509TrustManager {

        private final X509TrustManager x509TrustManager;
        private X509Certificate[] x509Certificates;

        CertificateChainTrustManager(X509TrustManager x509TrustManager) {
            this.x509TrustManager = x509TrustManager;
        }

        public X509Certificate[] getAcceptedIssuers() {
            // Called by Java 7+ see http://infposs.blogspot.kr/2013/06/installcert-and-java-7.html
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            this.x509Certificates = chain;
            x509TrustManager.checkServerTrusted(chain, authType);
        }
    }
}
