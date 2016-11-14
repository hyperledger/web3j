package org.web3j.protocol.http;


import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Async;

/**
 * HTTP implementation of our services API.
 */
public class HttpService implements Web3jService {

    private CloseableHttpClient httpClient =
            HttpClients.custom().setConnectionManagerShared(true).build();

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
        this.httpClient = httpClient;
    }

    @Override
    public <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException {

        byte[] payload = objectMapper.writeValueAsBytes(request);

        HttpPost httpPost = new HttpPost(this.url);
        httpPost.setEntity(new ByteArrayEntity(payload));
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");

        ResponseHandler<T> responseHandler = getResponseHandler(responseType);
        try {
            return httpClient.execute(httpPost, responseHandler);
        } finally {
            httpClient.close();
        }
    }

    public <T> ResponseHandler<T> getResponseHandler(final Class<T> type) {
        return new ResponseHandler<T>() {
            @Override
            public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
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
            }
        };
    }

    @Override
    public <T extends Response> Future<T> sendAsync(
            final Request jsonRpc20Request, final Class<T> responseType) {

        return Async.run(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return send(jsonRpc20Request, responseType);
            }
        });
    }
}
