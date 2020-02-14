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
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import org.web3j.protocol.core.BatchRequest;
import org.web3j.protocol.core.BatchResponse;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.websocket.events.NewHeadsNotification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebSocketServiceTest {

    private static final int REQUEST_ID = 1;

    private WebSocketClient webSocketClient = mock(WebSocketClient.class);
    private WebSocketListener listener;
    private ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);

    private WebSocketService service = new WebSocketService(webSocketClient, executorService, true);

    private Request<?, Web3ClientVersion> request =
            new Request<>(
                    "web3_clientVersion",
                    Collections.<String>emptyList(),
                    service,
                    Web3ClientVersion.class);

    private Request<Object, EthSubscribe> subscribeRequest;

    @BeforeEach
    public void before() throws InterruptedException {
        when(webSocketClient.connectBlocking()).thenReturn(true);
        when(webSocketClient.reconnectBlocking()).thenReturn(true);
        request.setId(1);
    }

    @Test
    public void testThrowExceptionIfServerUrlIsInvalid() {

        assertThrows(RuntimeException.class, () -> new WebSocketService("invalid\\url", true));
    }

    @Test
    public void testConnectViaWebSocketClient() throws Exception {
        service.connect();

        verify(webSocketClient).connectBlocking();
    }

    @Test
    public void testReConnectAfterConnected() throws Exception {
        service.connect();
        service.close();
        service.connect();

        verify(webSocketClient, atMostOnce()).connectBlocking();
        verify(webSocketClient, atMostOnce()).reconnectBlocking();
    }

    @Test
    public void testInterruptCurrentThreadIfConnectionIsInterrupted() throws Exception {
        when(webSocketClient.connectBlocking()).thenThrow(new InterruptedException());
        service.connect();

        assertTrue(Thread.currentThread().isInterrupted(), "Interrupted flag was not set properly");
    }

    @Test
    public void testThrowExceptionIfConnectionFailed() throws Exception {
        when(webSocketClient.connectBlocking()).thenReturn(false);
        assertThrows(
                ConnectException.class,
                () -> {
                    service.connect();
                });
    }

    @Test
    public void testAddedWebSocketListener() throws Exception {
        doAnswer(
                        invocation -> {
                            listener = invocation.getArgument(0, WebSocketListener.class);
                            return null;
                        })
                .when(webSocketClient)
                .setListener(any());

        final CountDownLatch onMessageCountDownLatch = new CountDownLatch(1);
        final CountDownLatch onCloseCountDownLatch = new CountDownLatch(1);
        final CountDownLatch onErrorCountDownLatch = new CountDownLatch(1);

        service.connect(
                message -> onMessageCountDownLatch.countDown(),
                throwable -> onErrorCountDownLatch.countDown(),
                onCloseCountDownLatch::countDown);

        service.sendAsync(request, Web3ClientVersion.class);
        listener.onMessage(
                "{\"jsonrpc\":\"2.0\",\"method\":\"web3_clientVersion\",\"params\":[],\"id\":1}");
        assertTrue(onMessageCountDownLatch.await(2L, TimeUnit.SECONDS));

        listener.onError(new Exception());
        assertTrue(onErrorCountDownLatch.await(2L, TimeUnit.SECONDS));

        listener.onClose();
        assertTrue(onCloseCountDownLatch.await(2L, TimeUnit.SECONDS));
    }

    @Test
    public void testNotWaitingForReplyWithUnknownId() {
        assertFalse(service.isWaitingForReply(123));
    }

    @Test
    public void testWaitingForReplyToSentRequest() throws Exception {
        service.sendAsync(request, Web3ClientVersion.class);

        assertTrue(service.isWaitingForReply(request.getId()));
    }

    @Test
    public void testNoLongerWaitingForResponseAfterReply() throws Exception {
        service.sendAsync(request, Web3ClientVersion.class);
        sendGethVersionReply();

        assertFalse(service.isWaitingForReply(1));
    }

    @Test
    public void testSendWebSocketRequest() throws Exception {
        service.sendAsync(request, Web3ClientVersion.class);

        verify(webSocketClient)
                .send(
                        "{\"jsonrpc\":\"2.0\",\"method\":\"web3_clientVersion\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testBatchRequestReply() throws Exception {
        BatchRequest request = new BatchRequest(service);
        request.add(
                        new Request<>(
                                "web3_clientVersion",
                                Collections.<String>emptyList(),
                                service,
                                Web3ClientVersion.class))
                .add(
                        new Request<>(
                                "net_version",
                                Collections.<String>emptyList(),
                                service,
                                NetVersion.class));
        request.getRequests().get(0).setId(1L);
        request.getRequests().get(1).setId(1L);

        CompletableFuture<BatchResponse> reply = service.sendBatchAsync(request);

        verify(webSocketClient)
                .send(
                        "["
                                + "{\"jsonrpc\":\"2.0\",\"method\":\"web3_clientVersion\",\"params\":[],\"id\":0},"
                                + "{\"jsonrpc\":\"2.0\",\"method\":\"net_version\",\"params\":[],\"id\":1}"
                                + "]");

        sendClientNetVersionReply();

        assertTrue(reply.isDone());
        BatchResponse response = reply.get();
        assertEquals(response.getResponses().size(), 2);

        assertTrue(response.getResponses().get(0) instanceof Web3ClientVersion);
        Web3ClientVersion web3ClientVersion = (Web3ClientVersion) response.getResponses().get(0);
        assertEquals(web3ClientVersion.getWeb3ClientVersion(), "Mist/v0.9.3/darwin/go1.4.1");

        assertTrue(response.getResponses().get(1) instanceof NetVersion);
        NetVersion netVersion = (NetVersion) response.getResponses().get(1);
        assertEquals(netVersion.getNetVersion(), "59");
    }

    @Test
    public void testIgnoreInvalidReplies() {
        service.sendAsync(request, Web3ClientVersion.class);
        assertThrows(IOException.class, () -> service.onWebSocketMessage("{"));
    }

    @Test
    public void testThrowExceptionIfIdHasInvalidType() {
        service.sendAsync(request, Web3ClientVersion.class);
        assertThrows(IOException.class, () -> service.onWebSocketMessage("{\"id\":true}"));
    }

    @Test
    public void testThrowExceptionIfIdIsMissing() {
        service.sendAsync(request, Web3ClientVersion.class);
        assertThrows(IOException.class, () -> service.onWebSocketMessage("{}"));
    }

    @Test
    public void testThrowExceptionIfUnexpectedIdIsReceived() {
        service.sendAsync(request, Web3ClientVersion.class);
        assertThrows(
                IOException.class,
                () ->
                        service.onWebSocketMessage(
                                "{\"jsonrpc\":\"2.0\",\"id\":12345,\"result\":\"geth-version\"}"));
    }

    @Test
    public void testReceiveReply() throws Exception {
        CompletableFuture<Web3ClientVersion> reply =
                service.sendAsync(request, Web3ClientVersion.class);
        sendGethVersionReply();

        assertTrue(reply.isDone());
        assertEquals("geth-version", reply.get().getWeb3ClientVersion());
    }

    @Test
    public void testReceiveError() throws Exception {
        CompletableFuture<Web3ClientVersion> reply =
                service.sendAsync(request, Web3ClientVersion.class);
        sendErrorReply();

        assertTrue(reply.isDone());
        Web3ClientVersion version = reply.get();
        assertTrue(version.hasError());
        assertEquals(new Response.Error(-1, "Error message"), version.getError());
    }

    @Test
    public void testCloseRequestWhenConnectionIsClosed() {
        CompletableFuture<Web3ClientVersion> reply =
                service.sendAsync(request, Web3ClientVersion.class);
        service.onWebSocketClose();

        assertTrue(reply.isDone());
        assertThrows(ExecutionException.class, () -> reply.get());
    }

    @Test
    public void testCancelRequestAfterTimeout() {
        when(executorService.schedule(
                        any(Runnable.class),
                        eq(WebSocketService.REQUEST_TIMEOUT),
                        eq(TimeUnit.SECONDS)))
                .then(
                        invocation -> {
                            Runnable runnable = invocation.getArgument(0, Runnable.class);
                            runnable.run();
                            return null;
                        });

        CompletableFuture<Web3ClientVersion> reply =
                service.sendAsync(request, Web3ClientVersion.class);

        assertTrue(reply.isDone());
        assertThrows(ExecutionException.class, () -> reply.get());
    }

    @Test
    public void testSyncRequest() throws Exception {
        CountDownLatch requestSent = new CountDownLatch(1);

        // Wait for a request to be sent
        doAnswer(
                        invocation -> {
                            requestSent.countDown();
                            return null;
                        })
                .when(webSocketClient)
                .send(anyString());

        // Send reply asynchronously
        runAsync(
                () -> {
                    try {
                        requestSent.await(2, TimeUnit.SECONDS);
                        sendGethVersionReply();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        Web3ClientVersion reply = service.send(request, Web3ClientVersion.class);

        assertEquals(reply.getWeb3ClientVersion(), "geth-version");
    }

    @Test
    public void testCloseWebSocketOnClose() throws Exception {
        service.close();

        verify(webSocketClient).close();
        verify(executorService).shutdown();
    }

    @Test
    public void testSendSubscriptionReply() throws Exception {
        runAsync(() -> subscribeToEvents());
        sendSubscriptionConfirmation();

        verifyStartedSubscriptionHandshake();
    }

    @Test
    public void testPropagateSubscriptionEvent() throws Exception {
        CountDownLatch eventReceived = new CountDownLatch(1);
        CountDownLatch disposed = new CountDownLatch(1);
        AtomicReference<NewHeadsNotification> actualNotificationRef = new AtomicReference<>();

        runAsync(
                () -> {
                    Disposable disposable =
                            subscribeToEvents()
                                    .subscribe(
                                            newHeadsNotification -> {
                                                actualNotificationRef.set(newHeadsNotification);
                                                eventReceived.countDown();
                                            });
                    try {
                        eventReceived.await(2, TimeUnit.SECONDS);
                        disposable.dispose();
                        disposed.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        sendSubscriptionConfirmation();
        sendWebSocketEvent();

        assertTrue(disposed.await(6, TimeUnit.SECONDS));
        assertEquals(
                "0xd9263f42a87",
                actualNotificationRef.get().getParams().getResult().getDifficulty());
    }

    @Test
    public void testSendUnsubscribeRequest() throws Exception {
        CountDownLatch unsubscribed = new CountDownLatch(1);

        runAsync(
                () -> {
                    Flowable<NewHeadsNotification> flowable = subscribeToEvents();
                    flowable.subscribe().dispose();
                    unsubscribed.countDown();
                });
        sendSubscriptionConfirmation();
        sendWebSocketEvent();

        assertTrue(unsubscribed.await(2, TimeUnit.SECONDS));
        verifyUnsubscribed();
    }

    @Test
    public void testStopWaitingForSubscriptionReplyAfterTimeout() throws Exception {
        CountDownLatch errorReceived = new CountDownLatch(1);
        AtomicReference<Throwable> actualThrowable = new AtomicReference<>();

        runAsync(
                () ->
                        subscribeToEvents()
                                .subscribe(
                                        new Subscriber<NewHeadsNotification>() {
                                            @Override
                                            public void onComplete() {}

                                            @Override
                                            public void onError(Throwable e) {
                                                actualThrowable.set(e);
                                                errorReceived.countDown();
                                            }

                                            @Override
                                            public void onSubscribe(Subscription s) {}

                                            @Override
                                            public void onNext(
                                                    NewHeadsNotification newHeadsNotification) {}
                                        }));

        waitForRequestSent();
        Exception e = new IOException("timeout");
        service.closeRequest(1, e);

        assertTrue(errorReceived.await(2, TimeUnit.SECONDS));
        assertEquals(e, actualThrowable.get());
    }

    @Test
    public void testOnErrorCalledIfConnectionClosed() throws Exception {
        CountDownLatch errorReceived = new CountDownLatch(1);
        AtomicReference<Throwable> actualThrowable = new AtomicReference<>();

        runAsync(
                () ->
                        subscribeToEvents()
                                .subscribe(
                                        new Subscriber<NewHeadsNotification>() {
                                            @Override
                                            public void onComplete() {}

                                            @Override
                                            public void onError(Throwable e) {
                                                actualThrowable.set(e);
                                                errorReceived.countDown();
                                            }

                                            @Override
                                            public void onSubscribe(Subscription s) {}

                                            @Override
                                            public void onNext(
                                                    NewHeadsNotification newHeadsNotification) {}
                                        }));

        waitForRequestSent();
        sendSubscriptionConfirmation();
        service.onWebSocketClose();

        assertTrue(errorReceived.await(2, TimeUnit.SECONDS));
        assertEquals(IOException.class, actualThrowable.get().getClass());
        assertEquals("Connection was closed", actualThrowable.get().getMessage());
    }

    @Test
    public void testIfCloseObserverIfSubscriptionRequestFailed() throws Exception {
        CountDownLatch errorReceived = new CountDownLatch(1);
        AtomicReference<Throwable> actualThrowable = new AtomicReference<>();

        runAsync(
                () ->
                        subscribeToEvents()
                                .subscribe(
                                        new Subscriber<NewHeadsNotification>() {
                                            @Override
                                            public void onComplete() {}

                                            @Override
                                            public void onError(Throwable e) {
                                                actualThrowable.set(e);
                                                errorReceived.countDown();
                                            }

                                            @Override
                                            public void onSubscribe(Subscription s) {}

                                            @Override
                                            public void onNext(
                                                    NewHeadsNotification newHeadsNotification) {}
                                        }));

        waitForRequestSent();
        sendErrorReply();

        assertTrue(errorReceived.await(2, TimeUnit.SECONDS));

        Throwable throwable = actualThrowable.get();
        assertEquals(IOException.class, throwable.getClass());
        assertEquals(
                "Subscription request failed with error: Error message", throwable.getMessage());
    }

    private void runAsync(Runnable runnable) {
        Executors.newSingleThreadExecutor().execute(runnable);
    }

    private Flowable<NewHeadsNotification> subscribeToEvents() {
        subscribeRequest =
                new Request<>(
                        "eth_subscribe",
                        Arrays.asList("newHeads", Collections.emptyMap()),
                        service,
                        EthSubscribe.class);
        subscribeRequest.setId(1);

        return service.subscribe(subscribeRequest, "eth_unsubscribe", NewHeadsNotification.class);
    }

    private void sendErrorReply() throws IOException {
        service.onWebSocketMessage(
                "{"
                        + "  \"jsonrpc\":\"2.0\","
                        + "  \"id\":1,"
                        + "  \"error\":{"
                        + "    \"code\":-1,"
                        + "    \"message\":\"Error message\","
                        + "    \"data\":null"
                        + "  }"
                        + "}");
    }

    private void sendGethVersionReply() throws IOException {
        service.onWebSocketMessage(
                "{"
                        + "  \"jsonrpc\":\"2.0\","
                        + "  \"id\":1,"
                        + "  \"result\":\"geth-version\""
                        + "}");
    }

    private void sendClientNetVersionReply() throws IOException {
        service.onWebSocketMessage(
                "["
                        + "{\n"
                        + "  \"id\":0,\n"
                        + "  \"jsonrpc\":\"2.0\",\n"
                        + "  \"result\": \"Mist/v0.9.3/darwin/go1.4.1\"\n"
                        + "},"
                        + "{\n"
                        + "  \"id\":1,\n"
                        + "  \"jsonrpc\": \"2.0\",\n"
                        + "  \"result\": \"59\"\n"
                        + "}"
                        + "]");
    }

    private void verifyStartedSubscriptionHandshake() {
        verify(webSocketClient)
                .send(
                        "{\"jsonrpc\":\"2.0\",\"method\":\"eth_subscribe\","
                                + "\"params\":[\"newHeads\",{}],\"id\":1}");
    }

    private void verifyUnsubscribed() {
        verify(webSocketClient)
                .send(
                        startsWith(
                                "{\"jsonrpc\":\"2.0\",\"method\":\"eth_unsubscribe\","
                                        + "\"params\":[\"0xcd0c3e8af590364c09d0fa6a1210faf5\"]"));
    }

    private void sendSubscriptionConfirmation() throws Exception {
        waitForRequestSent();

        service.onWebSocketMessage(
                "{"
                        + "\"jsonrpc\":\"2.0\","
                        + "\"id\":1,"
                        + "\"result\":\"0xcd0c3e8af590364c09d0fa6a1210faf5\""
                        + "}");
    }

    private void waitForRequestSent() throws InterruptedException {
        while (!service.isWaitingForReply(REQUEST_ID)) {
            Thread.sleep(50);
        }
    }

    private void sendWebSocketEvent() throws IOException {
        service.onWebSocketMessage(
                "{"
                        + "  \"jsonrpc\":\"2.0\","
                        + "  \"method\":\"eth_subscription\","
                        + "  \"params\":{"
                        + "    \"subscription\":\"0xcd0c3e8af590364c09d0fa6a1210faf5\","
                        + "    \"result\":{"
                        + "      \"difficulty\":\"0xd9263f42a87\","
                        + "      \"uncles\":[]"
                        + "    }"
                        + "  }"
                        + "}");
    }
}
