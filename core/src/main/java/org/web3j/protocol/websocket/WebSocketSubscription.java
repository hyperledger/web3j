package org.web3j.protocol.websocket;

import rx.subjects.BehaviorSubject;

/**
 * Objects necessary to process a new item received via a WebSocket subscription.
 *
 * @param <T> type of a data item that should be returned by a WebSocket subscription.
 */
public class WebSocketSubscription<T> {
    private BehaviorSubject<T> subject;
    private Class<T> responseType;

    /**
     * Creates WebSocketSubscription.
     *
     * @param subject used to send new data items to listeners
     * @param responseType type of a data item returned by a WebSocket subscription
     */
    public WebSocketSubscription(BehaviorSubject<T> subject, Class<T> responseType) {
        this.subject = subject;
        this.responseType = responseType;
    }

    public BehaviorSubject<T> getSubject() {
        return subject;
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}
