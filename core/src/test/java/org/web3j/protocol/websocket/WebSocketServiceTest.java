package org.web3j.protocol.websocket;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        service.onReply("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":\"geth-version\"}");

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
        service.onReply("{");
    }

    @Test
    public void testThrowExceptionIfIdHasInvalidType() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("'id' expected to be long, but it is: 'true'");
        service.sendAsync(request, Web3ClientVersion.class);
        service.onReply("{\"id\":true}");
    }

    @Test
    public void testThrowExceptionIfIdIsMissing() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("'id' field is missing in the reply");
        service.sendAsync(request, Web3ClientVersion.class);
        service.onReply("{}");
    }

    @Test
    public void testThrowExceptionIfUnexpectedIdIsReceived() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectMessage("Received reply for unexpected request id: 12345");
        service.sendAsync(request, Web3ClientVersion.class);
        service.onReply("{\"jsonrpc\":\"2.0\",\"id\":12345,\"result\":\"geth-version\"}");
    }

    @Test
    public void testReceiveReply() throws Exception {
        CompletableFuture<Web3ClientVersion> reply = service.sendAsync(
                request,
                Web3ClientVersion.class);
        service.onReply("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":\"geth-version\"}");

        assertTrue(reply.isDone());
        assertEquals("geth-version", reply.get().getWeb3ClientVersion());
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
                service.onReply("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":\"geth-version\"}");
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

}