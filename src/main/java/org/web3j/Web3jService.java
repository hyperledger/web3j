package org.web3j;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import org.web3j.methods.response.*;
import org.web3j.protocol.RequestFactory;
import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.JsonRpc2_0Factory;
import org.web3j.protocol.jsonrpc20.Request;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * Service API.
 */
public class Web3jService {

    private RequestFactory requestFactory = new JsonRpc2_0Factory();
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private ObjectMapper objectMapper = Utils.getObjectMapper();

    private final String url;

    public Web3jService(String url) {
        this.url = url;
    }

    public Web3jService(String url, CloseableHttpClient httpClient) {
        this.url = url;
        this.httpclient = httpClient;
    }

    private <T extends Response> T sendRequest(
            Request jsonRpc20Request, Class<T> type) throws IOException {

        byte[] payload = objectMapper.writeValueAsBytes(jsonRpc20Request);

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

    public Web3ClientVersion web3ClientVersion() throws IOException {
        Request jsonRpc20Request = requestFactory.web3ClientVersion();
        return sendRequest(jsonRpc20Request, Web3ClientVersion.class);
    }

    public Web3Sha3 web3Sha3(String data) throws IOException {
        Request request = requestFactory.web3Sha3(data);
        return sendRequest(request, Web3Sha3.class);
    }

    public NetVersion netVersion() throws IOException {
        Request request = requestFactory.netVersion();
        return sendRequest(request, NetVersion.class);
    }

    public NetListening netListening() throws IOException {
        Request request = requestFactory.netListening();
        return sendRequest(request, NetListening.class);
    }

    public NetPeerCount netPeerCount() throws IOException {
        Request request = requestFactory.netPeerCount();
        return sendRequest(request, NetPeerCount.class);
    }

    public EthProtocolVersion ethProtocolVersion() throws IOException {
        Request request = requestFactory.ethProtocolVersion();
        return sendRequest(request, EthProtocolVersion.class);
    }

    public EthSyncing ethSyncing() throws IOException {
        Request request = requestFactory.ethSyncing();
        return sendRequest(request, EthSyncing.class);
    }

    public EthCoinbase ethCoinbase() throws IOException {
        Request request = requestFactory.ethCoinbase();
        return sendRequest(request, EthCoinbase.class);
    }
}
