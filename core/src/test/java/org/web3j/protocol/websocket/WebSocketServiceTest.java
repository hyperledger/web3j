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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSubscribe;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.websocket.events.NewHeadsNotification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class WebSocketServiceTest {

    private WebSocketClient webSocketClient = mock(WebSocketClient.class);
    private ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);

    private WebSocketService service = new WebSocketService(webSocketClient, executorService, true);

    private Request<?, Web3ClientVersion> request = new Request<>(
            "web3_clientVersion",
            Collections.<String>emptyList(),
            service,
            Web3ClientVersion.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Request<Object, EthSubscribe> subscribeRequest;

    @Before
    public void before() throws InterruptedException {
        when(webSocketClient.connectBlocking()).thenReturn(true);
        request.setId(1);
    }

    @Test
    public void testThrowExceptionIfServerUrlIsInvalid() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Failed to parse URL: 'invalid\\url'");
        new WebSocketService("invalid\\url", true);
    }

    @Test
    public void testConnectViaWebSocketClient() throws Exception {
        service.connect();

        verify(webSocketClient).connectBlocking();
    }

    @Test
    public void testInterruptCurrentThreadIfConnectionIsInterrupted() throws Exception {
        when(webSocketClient.connectBlocking()).thenThrow(new InterruptedException());
        service.connect();

        assertTrue("Interrupted flag was not set properly",
                Thread.currentThread().isInterrupted());
    }

    @Test
    public void testThrowExceptionIfConnectionFailed() throws Exception {
        thrown.expect(ConnectException.class);
        thrown.expectMessage("Failed to connect to WebSocket");
        when(webSocketClient.connectBlocking()).thenReturn(false);
        service.connect();
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

        verify(webSocketClient).send(
                "{\"jsonrpc\":\"2.0\",\"method\":\"web3_clientVersion\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testIgnoreInvalidReplies() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Failed to parse incoming WebSocket message");
        service.sendAsync(request, Web3ClientVersion.class);
        service.onMessage("{");
    }

    @Test
    public void testThrowExceptionIfIdHasInvalidType() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("'id' expected to be long, but it is: 'true'");
        service.sendAsync(request, Web3ClientVersion.class);
        service.onMessage("{\"id\":true}");
    }

    @Test
    public void testThrowExceptionIfIdIsMissing() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Unknown message type");
        service.sendAsync(request, Web3ClientVersion.class);
        service.onMessage("{}");
    }

    @Test
    public void testThrowExceptionIfUnexpectedIdIsReceived() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Received reply for unexpected request id: 12345");
        service.sendAsync(request, Web3ClientVersion.class);
        service.onMessage("{\"jsonrpc\":\"2.0\",\"id\":12345,\"result\":\"geth-version\"}");
    }

    @Test
    public void testReceiveReply() throws Exception {
        CompletableFuture<Web3ClientVersion> reply = service.sendAsync(
                request,
                Web3ClientVersion.class);
        sendGethVersionReply();

        assertTrue(reply.isDone());
        assertEquals("geth-version", reply.get().getWeb3ClientVersion());
    }

    @Test
    public void testReceiveError() throws Exception {
        CompletableFuture<Web3ClientVersion> reply = service.sendAsync(
                request,
                Web3ClientVersion.class);
        sendErrorReply();

        assertTrue(reply.isDone());
        Web3ClientVersion version = reply.get();
        assertTrue(version.hasError());
        assertEquals(
                new Response.Error(-1, "Error message"),
                version.getError());
    }

    @Test(expected = ExecutionException.class)
    public void testCancelRequestAfterTimeout() throws Exception {
        when(executorService.schedule(
                any(Runnable.class),
                eq(WebSocketService.REQUEST_TIMEOUT),
                eq(TimeUnit.SECONDS)))
                .then(invocation -> {
                    Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                    runnable.run();
                    return null;
                });

        CompletableFuture<Web3ClientVersion> reply = service.sendAsync(
                request,
                Web3ClientVersion.class);

        assertTrue(reply.isDone());
        reply.get();
    }

    @Test
    public void testSyncRequest() throws Exception {
        CountDownLatch requestSent = new CountDownLatch(1);

        doAnswer(invocation -> {
            requestSent.countDown();
            return null;
        }).when(webSocketClient).send(anyString());

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                requestSent.await();
                sendGethVersionReply();
            } catch (Exception e) {
                e.printStackTrace();
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
        subscribeToEvents();

        verifyStartedSubscriptionHadnshake();
    }

    @Test
    public void testPropagateSubscriptionEvent() throws Exception {
        CountDownLatch eventReceived = new CountDownLatch(1);
        CountDownLatch completedCalled = new CountDownLatch(1);
        AtomicReference<NewHeadsNotification> actualNotificationRef = new AtomicReference<>();

        final Subscription subscription = subscribeToEvents()
                .subscribe(new Subscriber<NewHeadsNotification>() {
                    @Override
                    public void onCompleted() {
                        completedCalled.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewHeadsNotification newHeadsNotification) {
                        actualNotificationRef.set(newHeadsNotification);
                        eventReceived.countDown();
                    }
                });

        sendSubscriptionConfirmation();
        sendWebSocketEvent();

        assertTrue(eventReceived.await(2, TimeUnit.SECONDS));
        subscription.unsubscribe();

        assertTrue(completedCalled.await(2, TimeUnit.SECONDS));
        assertEquals(
                "0xd9263f42a87",
                actualNotificationRef.get().getParams().getResult().getDifficulty());
    }

    @Test
    public void testSendUnsubscribeRequest() throws Exception {
        Observable<NewHeadsNotification> obserable = subscribeToEvents();
        sendSubscriptionConfirmation();
        sendWebSocketEvent();

        obserable.subscribe().unsubscribe();

        verifyUnsubscribed();
    }

    @Test
    public void testDoSendUnsubscribeRequestIfUnsubscribedBeforeConfirmation() throws Exception {
        Observable<NewHeadsNotification> obserable = subscribeToEvents();
        obserable.subscribe().unsubscribe();

        verifyStartedSubscriptionHadnshake();
        verifyNoMoreInteractions(webSocketClient);
    }

    @Test
    public void testStopWaitingForSubscriptionReplyAfterTimeout() throws Exception {
        Observable<NewHeadsNotification> obserable = subscribeToEvents();
        CountDownLatch errorReceived = new CountDownLatch(1);
        AtomicReference<Throwable> actualThrowable = new AtomicReference<>();

        obserable.subscribe(new Observer<NewHeadsNotification>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                actualThrowable.set(e);
                errorReceived.countDown();
            }

            @Override
            public void onNext(NewHeadsNotification newHeadsNotification) {

            }
        });

        Exception e = new IOException("timeout");
        service.closeRequest(1, e);

        assertTrue(errorReceived.await(2, TimeUnit.SECONDS));
        assertEquals(
                actualThrowable.get(),
                e);
    }

    private Observable<NewHeadsNotification> subscribeToEvents() throws IOException {
        subscribeRequest = new Request<>(
                "eth_subscribe",
                Arrays.asList("newHeads", Collections.emptyMap()),
                service,
                EthSubscribe.class);
        subscribeRequest.setId(1);

        return service.subscribe(
                subscribeRequest,
                NewHeadsNotification.class
        );
    }

    private void sendErrorReply() throws IOException {
        service.onMessage(
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
        service.onMessage(
                "{"
                        + "  \"jsonrpc\":\"2.0\","
                        + "  \"id\":1,"
                        + "  \"result\":\"geth-version\""
                        + "}");
    }

    private void verifyStartedSubscriptionHadnshake() {
        verify(webSocketClient).send(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eth_subscribe\","
                        + "\"params\":[\"newHeads\",{}],\"id\":1}");
    }

    private void verifyUnsubscribed() {
        verify(webSocketClient).send(startsWith(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eth_unsubscribe\","
                        + "\"params\":[\"0xcd0c3e8af590364c09d0fa6a1210faf5\"]"));
    }

    private void sendSubscriptionConfirmation() throws IOException {
        service.onMessage(
                "{"
                        + "\"jsonrpc\":\"2.0\","
                        + "\"id\":1,"
                        + "\"result\":\"0xcd0c3e8af590364c09d0fa6a1210faf5\""
                        + "}");
    }

    private void sendWebSocketEvent() throws IOException {
        service.onMessage(
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