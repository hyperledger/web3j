package org.web3j.protocol.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import org.web3j.protocol.Web3jService;

public class Request<S, T extends Response> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Request.class);
    private static AtomicLong nextId = new AtomicLong(0);

    private String jsonrpc = "2.0";
    private String method;
    private List<S> params;
    private long id;

    private Web3jService[] web3jServices;
    private int counter = 0;

    // Unfortunately require an instance of the type too, see
    // http://stackoverflow.com/a/3437930/3211687
    private Class<T> responseType;

    public Request() {
    }

    public Request(String method, List<S> params,
                   Web3jService[] web3jServices, Class<T> type) {
        this.method = method;
        this.params = params;
        this.id = nextId.getAndIncrement();
        this.web3jServices = web3jServices;
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
        try{
            return web3jServices[counter].send(this, responseType);
        } catch (IOException e) {
            Web3jService fallback = next();
            if (fallback != null) {
                return fallback.send(this, responseType);
            } else {
                throw e;
            }
        }
    }

    public CompletableFuture<T> sendAsync() {
        return web3jServices[0].sendAsync(this, responseType).handle((v, e) -> {
            if (e != null && e.getClass().isAssignableFrom(IOException.class)) {
                Web3jService fallback = next();
                if (fallback != null) {
                    return fallback.sendAsync(this, responseType).join();
                }
            }
            return v;
        });
    }

    public Observable<T> observable() {
        return new RemoteCall<>(this::send).observable();
    }

    public Web3jService next() {
        ++counter;
        if (counter == web3jServices.length - 1) counter = 0;
        return web3jServices[counter];
    }
}
