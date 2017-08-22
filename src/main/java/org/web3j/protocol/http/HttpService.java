package org.web3j.protocol.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.Service;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.exceptions.ClientConnectionException;

/**
 * HTTP implementation of our services API.
 */
public class HttpService extends Service {

    public static final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    public static final String DEFAULT_URL = "http://localhost:8545/";

    private static final Logger log = LoggerFactory.getLogger(HttpService.class);

    private OkHttpClient httpClient;

    private final String url;

    private final boolean includeRawResponse;

    public HttpService(String url, OkHttpClient httpClient, boolean includeRawResponses) {
        super(includeRawResponses);
        this.url = url;
        this.httpClient = httpClient;
        this.includeRawResponse = includeRawResponses;
    }

    public HttpService(OkHttpClient httpClient, boolean includeRawResponses) {
        this(DEFAULT_URL, httpClient, includeRawResponses);
    }

    private HttpService(String url, OkHttpClient httpClient) {
        this(url, httpClient, false);
    }

    public HttpService(String url) {
        this(url, createOkHttpClient());
    }

    public HttpService(OkHttpClient httpClient) {
        this(DEFAULT_URL, httpClient);
    }

    public HttpService() {
        this(DEFAULT_URL);
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
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
    public <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException {

        byte[] payload = objectMapper.writeValueAsBytes(request);

        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, payload);
        Headers headers = buildHeaders();

        okhttp3.Request httpRequest = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();

        okhttp3.Response response = httpClient.newCall(httpRequest).execute();
        try {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream inputStream = buildInputStream(responseBody.byteStream());
                    return objectMapper.readValue(inputStream, responseType);
                } else {
                    return null;
                }
            } else {
                throw new ClientConnectionException(
                        "Invalid response received: " + response.body());
            }
        } finally {
            if (response.body() != null) {
                response.close();
            }
        }
    }

    private InputStream buildInputStream(InputStream inputStream) throws IOException {
        if (includeRawResponse) {
            BufferedInputStream bufferedinputStream =
                    new BufferedInputStream(inputStream);
            bufferedinputStream.mark(inputStream.available());
            return bufferedinputStream;
        } else {
            return inputStream;
        }
    }

    private Headers buildHeaders() {
        Map<String, String> headers = new HashMap<>();
        addHeaders(headers);
        return Headers.of(headers);
    }

    protected void addHeaders(Map<String, String> headers) { }
}
