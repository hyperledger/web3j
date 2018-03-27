package org.web3j.protocol.core;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WebSocketEventTest {

    private WebSocketClient webSocketClient = mock(WebSocketClient.class);

    private WebSocketService webSocketService = new WebSocketService(
            webSocketClient, true
    );

    private Web3j web3j = Web3j.build(webSocketService);

    @Test
    public void testNewHeadsNotifications() {
        web3j.newHeadsNotifications();

        verify(webSocketClient).send(matches(
                "\\{\"jsonrpc\":\"2.0\",\"method\":\"eth_subscribe\","
                        + "\"params\":\\[\"newHeads\"],\"id\":[0-9]{1,}}"));
    }

    @Test
    public void testLogsNotificationsWithoutArguments() {
        web3j.logsNotifications(new ArrayList<>(), new ArrayList<>());

        verify(webSocketClient).send(matches(
                "\\{\"jsonrpc\":\"2.0\",\"method\":\"eth_subscribe\","
                        + "\"params\":\\[\"logs\",\\{}],\"id\":[0-9]{1,}}"));
    }

    @Test
    public void testLogsNotificationsWithArguments() {
        web3j.logsNotifications(
                Collections.singletonList("0x1"),
                Collections.singletonList("0x2"));

        verify(webSocketClient).send(matches(
                "\\{\"jsonrpc\":\"2.0\",\"method\":\"eth_subscribe\","
                        + "\"params\":\\[\"logs\",\\{\"address\":\\[\"0x1\"],"
                        + "\"topics\":\\[\"0x2\"]}],\"id\":[0-9]{1,}}"));
    }

    @Test
    public void testPendingTransactionsNotifications() {
        web3j.newPendingTransactionsNotifications();

        verify(webSocketClient).send(matches(
                "\\{\"jsonrpc\":\"2.0\",\"method\":\"eth_subscribe\",\"params\":"
                        + "\\[\"newPendingTransactions\"],\"id\":[0-9]{1,}}"));
    }

    @Test
    public void testSyncingStatusNotifications() {
        web3j.syncingStatusNotifications();

        verify(webSocketClient).send(matches(
                "\\{\"jsonrpc\":\"2.0\",\"method\":\"eth_subscribe\","
                        + "\"params\":\\[\"syncing\"],\"id\":[0-9]{1,}}"));
    }
}
