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

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web socket client implementation that connects to a specify URI. Allows to provide a listener
 * that will be called when a new message is received by the client.
 */
public class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(WebSocketClient.class);

    private WebSocketListener listener = null;

    public WebSocketClient(final URI serverUri) {
        super(serverUri);
    }

    public WebSocketClient(final URI serverUri, final Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(final ServerHandshake serverHandshake) {
        log.debug("Opened WebSocket connection to {}", uri);
    }

    @Override
    public void onMessage(final String s) {
        log.debug("Received message {} from server {}", s, uri);
        if (listener != null) {
            try {
                listener.onMessage(s);
            } catch (final Exception e) {
                log.error("Failed to process message '{}' from server {}", s, uri, e);
            }
        }
    }

    @Override
    public void onClose(final int code, final String reason, final boolean remote) {
        log.debug(
                "Closed WebSocket connection to {}, because of reason: '{}'."
                        + "Connection closed remotely: {}",
                uri,
                reason,
                remote);
        if (listener != null) {
            listener.onClose();   
        }
    }

    @Override
    public void onError(final Exception e) {
        log.error("WebSocket connection to {} failed with error", uri, e);
        if (listener != null) {
            listener.onError(e);
        }
    }

    /**
     * Set a listener that will be called when a new message is received by the client.
     *
     * @param listener WebSocket listener
     */
    public void setListener(final WebSocketListener listener) {
        this.listener = listener;
    }
}
