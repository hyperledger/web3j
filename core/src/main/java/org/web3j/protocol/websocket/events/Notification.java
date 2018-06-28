package org.web3j.protocol.websocket.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Base class for WebSocket notifications.
 *
 * @param <T> type of data return by a particular subscription
 */
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

