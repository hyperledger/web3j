/*
 * Copyright 2020 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.websocket;

import java.io.IOException;
import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.websocket.events.Notification;

/**
 * Web socket service that allows to interact with JSON-RPC via WebSocket protocol.
 *
 * <p>Built-in reconnection mechanism
 *
 * <p>Allows to interact with JSON-RPC either by sending individual requests or by subscribing to a
 * stream of notifications. To subscribe to a notification it first sends a special JSON-RPC request
 * that returns a unique subscription id. A subscription id is used to identify events for a single
 * notifications stream.
 *
 * <p>To unsubscribe from a stream of notifications it should send another JSON-RPC request.
 */
public class ReconnectWebSocketService extends WebSocketService {
    private static final Logger log = LoggerFactory.getLogger(ReconnectWebSocketService.class);
    private final Set<Request> subscribeRequestSet = ConcurrentHashMap.newKeySet();

    protected final ThreadPoolExecutor reconnectExecutor;

    // Wait time for reconnecting
    final int reconnectLatencySeconds;

    volatile boolean closed = false;

    public ReconnectWebSocketService(
            String serverUrl, boolean includeRawResponses, int reconnectLatencySeconds) {
        this(
                new WebSocketClient(parseURI(serverUrl)),
                Executors.newScheduledThreadPool(1),
                null,
                includeRawResponses,
                reconnectLatencySeconds);
    }

    public ReconnectWebSocketService(String serverUrl, boolean includeRawResponses) {
        this(new WebSocketClient(parseURI(serverUrl)), includeRawResponses);
    }

    public ReconnectWebSocketService(
            WebSocketClient webSocketClient,
            boolean includeRawResponses,
            int reconnectLatencySeconds) {
        this(
                webSocketClient,
                Executors.newScheduledThreadPool(1),
                null,
                includeRawResponses,
                reconnectLatencySeconds);
    }

    public ReconnectWebSocketService(WebSocketClient webSocketClient, boolean includeRawResponses) {
        this(webSocketClient, Executors.newScheduledThreadPool(1), null, includeRawResponses, 5);
    }

    ReconnectWebSocketService(
            WebSocketClient webSocketClient,
            ScheduledExecutorService executor,
            ThreadPoolExecutor reconnectExecutor,
            boolean includeRawResponses,
            int reconnectLatencySeconds) {
        super(webSocketClient, executor, includeRawResponses);
        this.reconnectLatencySeconds = reconnectLatencySeconds;
        if (null == reconnectExecutor) {
            this.reconnectExecutor =
                    new ThreadPoolExecutor(
                            1,
                            1,
                            1,
                            TimeUnit.MINUTES,
                            new ArrayBlockingQueue<>(1),
                            new ThreadPoolExecutor.DiscardPolicy());
            this.reconnectExecutor.allowCoreThreadTimeOut(true);
        } else {
            this.reconnectExecutor = reconnectExecutor;
        }
    }

    @Override
    public void connect(Consumer<String> onMessage, Consumer<Throwable> onError, Runnable onClose)
            throws ConnectException {
        try {
            setWebSocketListener(onMessage, onError, onClose);
            connectToWebSocket();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while connecting via WebSocket protocol");
        }
    }

    @Override
    protected void connectToWebSocket() throws InterruptedException, ConnectException {

        if (shouldReConnect) {
            webSocketClient.reconnectBlocking();
        } else {
            shouldReConnect = true;
            webSocketClient.connectBlocking();
        }
    }

    @Override
    protected void setWebSocketListener(
            Consumer<String> onMessage, Consumer<Throwable> onError, Runnable onClose) {
        webSocketClient.setListener(
                new WebSocketListener() {
                    @Override
                    public void onMessage(String message) throws IOException {
                        onWebSocketMessage(message);
                        onMessage.accept(message);
                    }

                    @Override
                    public void onError(Exception e) {
                        log.error("Received error from a WebSocket connection", e);
                        onError.accept(e);
                    }

                    @Override
                    public void onClose() {
                        if (!closed) {
                            reconnect();
                            return;
                        }
                        onWebSocketClose();
                        onClose.run();
                    }
                });
    }

    @Override
    public <T extends Notification<?>> Flowable<T> subscribe(
            Request request, String unsubscribeMethod, Class<T> responseType) {
        // We can't use usual Observer since we can call "onError"
        // before first client is subscribed and we need to
        // preserve it
        BehaviorSubject<T> subject = BehaviorSubject.create();

        // We need to subscribe synchronously, since if we return
        // an Flowable to a client before we got a reply
        // a client can unsubscribe before we know a subscription
        // id and this can cause a race condition
        subscribeToEventsStream(request, subject, responseType);

        return subject.doOnDispose(
                        () -> {
                            closeSubscription(subject, unsubscribeMethod);
                            rmSubscribeRequest(request);
                        })
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    protected <T extends Notification<?>> void subscribeToEventsStream(
            Request request, BehaviorSubject<T> subject, Class<T> responseType) {
        subscriptionRequestForId.put(
                request.getId(), new WebSocketSubscription<>(subject, responseType));

        addSubscribeRequest(request);
        try {
            send(request, EthSubscribe.class);
        } catch (WebsocketNotConnectedException ignored) {
            log.warn(
                    "The request is send request again when reconnected. requestId: {}",
                    request.getId());
        } catch (IOException e) {
            log.error("Failed to subscribe to RPC events with request id {}", request.getId());
            subject.onError(e);
        }
    }

    private synchronized void reSubscribeRequest() {
        for (Request request : subscribeRequestSet) {
            try {
                send(request, EthSubscribe.class);
                log.info("Resending request. {}", request);
            } catch (Exception e) {
                log.warn("resubscribe ", e);
            }
        }
    }

    private synchronized void addSubscribeRequest(Request request) {
        subscribeRequestSet.add(request);
    }

    private synchronized void rmSubscribeRequest(Request request) {
        subscribeRequestSet.remove(request);
    }

    protected void reconnect() {
        this.reconnectExecutor.submit(this::internalReconnect);
    }

    protected void internalReconnect() {
        if (closed || webSocketClient.isOpen()) return;
        try {
            closeOutstandingRequests();
            log.debug("[RECONNECT] try reconnect to {}", webSocketClient.getURI());
            TimeUnit.SECONDS.sleep(reconnectLatencySeconds);
            if (webSocketClient.reconnectBlocking()) {
                reSubscribeRequest();
                log.debug("[RECONNECT] reconnected to {}", webSocketClient.getURI());
            } else {
                log.warn("[RECONNECT] [FAILED] reconnected to {}", webSocketClient.getURI());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (closed && webSocketClient.isOpen()) {
                webSocketClient.close();
                log.debug("[RECONNECT] close connection: {}", webSocketClient.getURI());
            }
        }
    }

    @Override
    public void close() {
        closed = true;
        super.close();
        reconnectExecutor.shutdown();
    }
}
