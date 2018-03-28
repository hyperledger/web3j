package org.web3j.protocol.websocket;

import rx.subjects.BehaviorSubject;

public class WebSocketSubscription<T> {
    private BehaviorSubject<T> subject;
    private Class<T> responseType;

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
