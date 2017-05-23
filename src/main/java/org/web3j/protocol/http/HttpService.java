package org.web3j.protocol.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicHeader;

import org.web3j.protocol.Service;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Async;

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
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Content-Type", "application/json; charset=UTF-8"));
        addHeaders(headers);
        return headers.toArray(new Header[0]);
    }

    protected void addHeaders(List<Header> headers) { }


    public <T extends Response> ResponseHandler<T> getResponseHandler(final Class<T> type) {
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
