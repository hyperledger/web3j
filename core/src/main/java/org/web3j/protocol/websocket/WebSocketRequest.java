package org.web3j.protocol.websocket;

import java.util.concurrent.CompletableFuture;

/**
 * Objects necessary to process a reply for a request sent via WebSocket protocol.
 *
 * @param <T> type of a data item that should be returned by the sent request
 */
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
