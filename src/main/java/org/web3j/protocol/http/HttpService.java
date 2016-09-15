package org.web3j.protocol.http;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.jsonrpc20.Request;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * HTTP implementation of our services API.
 */
public class HttpService implements Web3jService {

    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    private final String url;

    public HttpService() {
        this.url = "http://localhost:8545/";
    }

    public HttpService(String url) {
        this.url = url;
    }

    public HttpService(String url, CloseableHttpClient httpClient) {
        this.url = url;
        this.httpclient = httpClient;
    }

    @Override
    public <T extends Response> T sendRequest(
            Request request, Class<T> type) throws IOException {

        byte[] payload = objectMapper.writeValueAsBytes(request);

        HttpPost httpPost = new HttpPost(this.url);
        httpPost.setEntity(new ByteArrayEntity(payload));
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");

        ResponseHandler<T> responseHandler = getResponseHandler(type);
        try {
            return httpclient.execute(httpPost, responseHandler);
        } finally {
            httpclient.close();
        }
    }

    public <T> ResponseHandler<T> getResponseHandler(Class<T> type) {
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

    @Override
    public <T extends Response> CompletableFuture<T> sendRequestAsync(
            Request jsonRpc20Request, Class<T> type) {
        CompletableFuture<T> result = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                result.complete(sendRequest(jsonRpc20Request, type));
            } catch (IOException e) {
                result.completeExceptionally(e);
            }
        });
        return result;
    }
}
