package org.web3j.protocol.websocket.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification<T> {
    private String jsonrpc;
    private String method;
    private NotificationParams<T> params;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public NotificationParams<T> getParams() {
        return params;
    }
}

