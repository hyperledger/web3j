package org.web3j.protocol.core;

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
                        + "\"params\":\\[\"newHeads\",\\{}],\"id\":[0-9]{1,}}"));
    }
}
