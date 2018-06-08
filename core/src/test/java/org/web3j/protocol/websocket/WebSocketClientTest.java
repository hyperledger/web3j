package org.web3j.protocol.websocket;

import java.io.IOException;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WebSocketClientTest {

    private WebSocketListener listener = mock(WebSocketListener.class);

    private WebSocketClient client;

    @Before
    public void before() throws Exception {
        client = new WebSocketClient(new URI("ws://localhost/"));
        client.setListener(listener);
    }

    @Test
    public void testNotifyListenerOnMessage() throws Exception {
        client.onMessage("message");

        verify(listener).onMessage("message");
    }

    @Test
    public void testNotifyListenerOnError() throws Exception {
        IOException e = new IOException("123");
        client.onError(e);

        verify(listener).onError(e);
    }

    @Test
    public void testNotifyListenerOnClose() throws Exception {
        client.onClose(1, "reason", true);

        verify(listener).onClose();
    }
}