package org.web3j.protocol.websocket.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Generic class for a notification param. Contains a subscription id and a data item.
 *
 * @param <T> type of data return by a particular subscription
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationParams<T> {
    private T result;
    private String subsciption;

    public T getResult() {
        return result;
    }

    public String getSubsciption() {
        return subsciption;
    }
}
