package org.web3j.protocol.core;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import rx.Observable;

import org.web3j.protocol.Web3jService;

public class Request<S, T extends Response> {
    private String jsonrpc = "2.0";
    private String method;
    private List<S> params;
    private UUID id;

    private Web3jService web3jService;

    // Unfortunately require an instance of the type too, see
    // http://stackoverflow.com/a/3437930/3211687
    private Class<T> responseType;

    public Request() {
    }

    public Request(String method, List<S> params,
                   Web3jService web3jService, Class<T> type) {
        this.method = method;
        this.params = params;
        this.id = UUID.randomUUID();
        this.web3jService = web3jService;
        this.responseType = type;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public T send() throws IOException {
        return web3jService.send(this, responseType);
    }

    public CompletableFuture<T> sendAsync() {
        return  web3jService.sendAsync(this, responseType);
    }

    public Observable<T> observable() {
        return new RemoteCall<>(this::send).observable();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request<?, ?> request = (Request<?, ?>) o;
        return Objects.equals(jsonrpc, request.jsonrpc)
                && Objects.equals(method, request.method)
                && Objects.equals(params, request.params)
                && Objects.equals(web3jService, request.web3jService)
                && Objects.equals(responseType, request.responseType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonrpc, method, params, web3jService, responseType);
    }
}
