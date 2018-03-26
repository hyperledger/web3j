package org.web3j.protocol.websocket;

import rx.subjects.PublishSubject;

public class WebSocketSubscription<T> {
    private PublishSubject<T> subject;
    private Class<T> responseType;

    public WebSocketSubscription(PublishSubject<T> subject, Class<T> responseType) {
        this.subject = subject;
        this.responseType = responseType;
    }

    public PublishSubject<T> getSubject() {
        return subject;
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}
