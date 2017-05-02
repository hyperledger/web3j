package org.web3j.protocol.http;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import org.web3j.protocol.Service;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

/**
 * HTTP implementation of our services API.
 */
public class HttpService extends Service {

    public static final String DEFAULT_URL = "http://localhost:8545/";

    private CloseableHttpClient httpClient;

    private final String url;

    public HttpService(String url, CloseableHttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    public HttpService(String url) {
        this(url, HttpClients.custom().setConnectionManagerShared(true).build());
    }

    public HttpService() {
        this(DEFAULT_URL);
    }

    protected void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException {

        byte[] payload = objectMapper.writeValueAsBytes(request);

        HttpPost httpPost = new HttpPost(this.url);
        httpPost.setEntity(new ByteArrayEntity(payload));
        Header[] headers = buildHeaders();
        httpPost.setHeaders(headers);

        ResponseHandler<T> responseHandler = getResponseHandler(responseType);
        try {
            return httpClient.execute(httpPost, responseHandler);
        } finally {
            httpClient.close();
        }
    }

    private Header[] buildHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Content-Type", "application/json; charset=UTF-8"));
        addHeaders(headers);
        return headers.toArray(new Header[0]);
    }

    protected void addHeaders(List<Header> headers) { }

    public <T extends Response> ResponseHandler<T> getResponseHandler(Class<T> type) {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    return objectMapper.readValue(response.getEntity().getContent(), type);
                } else {
                    return null;
                }
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }
}
