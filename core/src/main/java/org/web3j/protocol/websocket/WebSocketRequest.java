package org.web3j.protocol.websocket;

import org.web3j.TempCompletableFuture;

import java.util.concurrent.Future;

/**
 * Objects necessary to process a reply for a request sent via WebSocket protocol.
 *
 * @param <T> type of a data item that should be returned by the sent request
 */
class WebSocketRequest<T> {
    private TempCompletableFuture<T> onReply;
    private Class<T> responseType;

    public WebSocketRequest(TempCompletableFuture<T> onReply, Class<T> responseType) {
        this.onReply = onReply;
        this.responseType = responseType;
    }

    public TempCompletableFuture<T> getOnReply() {
        return onReply;
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}
