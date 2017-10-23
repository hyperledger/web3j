package org.web3j.protocol.core;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import rx.Observable;
import rx.Subscriber;

import org.web3j.protocol.Web3jService;

public class Request<S, T extends Response> {
    private String jsonrpc = "2.0";
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T send() throws IOException {
        return web3jService.send(this, responseType);
    }

    public Future<T> sendAsync() {
        return  web3jService.sendAsync(this, responseType);
    }

    public Observable<T> observable() {
        return new RemoteCall<T>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return Request.this.send();
            }
        }).observable();
    }
}
