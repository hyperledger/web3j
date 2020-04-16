/*
 * Copyright 2019 Web3 Labs Ltd.
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
import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class WebSocketClientTest {

    private WebSocketListener listener = mock(WebSocketListener.class);

    private WebSocketClient client;

    @BeforeEach
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
    public void testErrorBeforeListenerSet() throws Exception {
        final IOException e = new IOException("123");
        client.setListener(null);
        client.onError(e);

        verify(listener, never()).onError(e);
    }

    @Test
    public void testNotifyListenerOnClose() throws Exception {
        client.onClose(1, "reason", true);

        verify(listener).onClose();
    }
}
