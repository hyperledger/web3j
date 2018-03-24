package org.web3j.protocol.websocket;

import java.util.concurrent.CompletableFuture;

class WebSocketRequest<T> {
    private CompletableFuture<T> completableFuture;
    private Class<T> responseType;

    public WebSocketRequest(CompletableFuture<T> completableFuture, Class<T> responseType) {
        this.completableFuture = completableFuture;
        this.responseType = responseType;
    }

    public CompletableFuture<T> getCompletableFuture() {
        return completableFuture;
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}
