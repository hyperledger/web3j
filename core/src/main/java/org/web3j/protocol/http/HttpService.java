package org.web3j.protocol.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.Service;
import org.web3j.protocol.exceptions.ClientConnectionException;

/**
 * HTTP implementation of our services API.
 */
public class HttpService extends Service {
    /**
     * Copied from {@link ConnectionSpec#APPROVED_CIPHER_SUITES}.
     */
    private static final CipherSuite[] INFURA_CIPHER_SUITES = new CipherSuite[] {
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,

            // Note that the following cipher suites are all on
            // HTTP/2's bad cipher suites list. We'll
            // continue to include them until better suites are
            // commonly available. For example, none
            // of the better cipher suites listed above shipped
            // with Android 4.4 or Java 7.
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
            CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
            CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA,
            CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA,

            // Additional INFURA CipherSuites
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,
            CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
            CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256,
            CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256
    };

    private static final ConnectionSpec INFURA_CIPHER_SUITE_SPEC = new ConnectionSpec
            .Builder(ConnectionSpec.MODERN_TLS).cipherSuites(INFURA_CIPHER_SUITES).build();

    public static final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    public static final String DEFAULT_URL = "http://localhost:8545/";

    private static final Logger log = LoggerFactory.getLogger(HttpService.class);

    private OkHttpClient httpClient;

    private String[] urls;

    private int counter = 0;

    private boolean includeRawResponse;

    private HashMap<String, String> headers = new HashMap<>();

    public HttpService(OkHttpClient httpClient, boolean includeRawResponses, String... urls) {
        super(includeRawResponses);
        this.httpClient = httpClient;
        this.includeRawResponse = includeRawResponses;
        this.urls = urls;
    }

    public HttpService(OkHttpClient httpClient, boolean includeRawResponses) {
        this(httpClient, includeRawResponses, DEFAULT_URL);
    }

    private HttpService(OkHttpClient httpClient, String... urls) {
        this(httpClient, false, urls);
    }

    public HttpService(String... urls) {
        this(createOkHttpClient(), urls);
    }

    public HttpService(boolean includeRawResponse, String... urls) {
        this(createOkHttpClient(), includeRawResponse, urls);
    }

    public HttpService(OkHttpClient httpClient) {
        this(httpClient, DEFAULT_URL);
    }

    public HttpService(boolean includeRawResponse) {
        this(includeRawResponse, DEFAULT_URL);
    }

    public HttpService() {
        this(DEFAULT_URL);
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(INFURA_CIPHER_SUITE_SPEC));
        configureLogging(builder);
        return builder.build();
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        if (log.isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(log::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
    }

    @Override
    protected InputStream performIO(String request) throws IOException {

        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, request);
        Headers headers = buildHeaders();
        okhttp3.Request httpRequest;
        String url = urls[counter];
        try {
            httpRequest = buildHttpRequest(url, headers, requestBody);
        } catch (IllegalStateException e) {
            log.error("Node connection has failed", e);
            url = nextUrl();
            if (url != null) {
                httpRequest = buildHttpRequest(url, headers, requestBody);
            } else {
                throw e;
            }
        }
        okhttp3.Response response = httpClient.newCall(httpRequest).execute();
        ResponseBody responseBody = response.body();
        if (response.isSuccessful()) {
            if (responseBody != null) {
                return buildInputStream(responseBody);
            } else {
                return null;
            }
        } else {
            int code = response.code();
            String text = responseBody == null ? "N/A" : responseBody.string();

            throw new ClientConnectionException("Invalid response received: " + code + "; " + text);
        }
    }

    private InputStream buildInputStream(ResponseBody responseBody) throws IOException {
        InputStream inputStream = responseBody.byteStream();

        if (includeRawResponse) {
            // we have to buffer the entire input payload, so that after processing
            // it can be re-read and used to populate the rawResponse field.

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body
            Buffer buffer = source.buffer();

            long size = buffer.size();
            if (size > Integer.MAX_VALUE) {
                throw new UnsupportedOperationException(
                        "Non-integer input buffer size specified: " + size);
            }

            int bufferSize = (int) size;
            BufferedInputStream bufferedinputStream =
                    new BufferedInputStream(inputStream, bufferSize);

            bufferedinputStream.mark(inputStream.available());
            return bufferedinputStream;

        } else {
            return inputStream;
        }
    }

    private Headers buildHeaders() {
        return Headers.of(headers);
    }

    private okhttp3.Request buildHttpRequest(String url, okhttp3.Headers headers,
                                             RequestBody requestBody) {
        okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();

        return httpRequest;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addHeaders(Map<String, String> headersToAdd) {
        headers.putAll(headersToAdd);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    private String nextUrl() {
        ++counter;
        if (counter == urls.length) {
            counter = 0;
        }
        return urls[counter];
    }

    @Override
    public void close() throws IOException {

    }
}
