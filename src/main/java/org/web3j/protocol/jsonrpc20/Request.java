package org.web3j.protocol.jsonrpc20;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.web3j.protocol.Web3jService;


public class Request<S, T extends Response> {
    private String jsonRpc = "2.0";
    private String method;
    private List<S> params;
    private long id;

    private Web3jService web3jService;

    // Unfortunately require an instance of the type too, see
    // http://stackoverflow.com/a/3437930/3211687
    private Class<T> responseType;

    public Request() {
    }

    public Request(String method, List<S> params, long id,
                   Web3jService web3jService, Class<T> type) {
        this.method = method;
        this.params = params;
        this.id = id;
        this.web3jService = web3jService;
        this.responseType = type;
    }

    public String getJsonRpc() {
        return jsonRpc;
    }

    public void setJsonRpc(String jsonRpc) {
        this.jsonRpc = jsonRpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<S> getParams() {
        return params;
    }

    public void setParams(List<S> params) {
        this.params = params;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T sendRequest() throws IOException {
        return web3jService.sendRequest(this, responseType);
    }

    public CompletableFuture<T> sendRequestAsync() {
        return  web3jService.sendRequestAsync(this, responseType);
    }

}
