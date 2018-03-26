package org.web3j.protocol;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import rx.Observable;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.websocket.events.Notification;

/**
 * Services API.
 */
public interface Web3jService {
    <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException;

    <T extends Response> CompletableFuture<T> sendAsync(
            Request request, Class<T> responseType);

    <T extends Notification<?>> Observable<T> subscribe(
            Request request,
            Class<T> responseType);

    boolean supportsSubscription();
}
