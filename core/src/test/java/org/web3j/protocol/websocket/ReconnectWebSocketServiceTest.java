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

import java.util.concurrent.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class ReconnectWebSocketServiceTest {

    private WebSocketClient webSocketClient = mock(WebSocketClient.class);
    private ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
    private ThreadPoolExecutor reconnectExecutor = mock(ThreadPoolExecutor.class);

    private ReconnectWebSocketService service;

    @BeforeEach
    public void before() {
        service =
                new ReconnectWebSocketService(
                        webSocketClient, executorService, reconnectExecutor, true, 5);
    }

    @Test
    public void testReconnect() throws InterruptedException {
        when(webSocketClient.isOpen()).thenReturn(false, true);
        when(webSocketClient.reconnectBlocking()).thenReturn(true);
        service.internalReconnect();
        then(webSocketClient).should(times(1)).reconnectBlocking();
        then(webSocketClient).should(times(1)).isOpen();

        service.internalReconnect();
        then(webSocketClient).should(times(2)).isOpen();

        service.close();
        service.internalReconnect();
        then(reconnectExecutor).should(times(1)).shutdown();
        then(webSocketClient).should(times(2)).isOpen();
    }
}
