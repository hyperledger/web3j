package org.web3j.protocol.websocket;

import java.net.URI;

import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(WebSocketClient.class);

    private WebSocketListener listener;

    public WebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("Opened WebSocket connection to {}", uri);
    }

    @Override
    public void onMessage(String s) {
        try {
            log.debug("Received message {} from server {}", s, uri);
            listener.onMessage(s);
            log.debug("Processed message {} from server {}", s, uri);
        } catch (Exception e) {
            log.error("Failed to process message '{}' from server {}", s, uri);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("Closed WebSocket connection to {}", uri);
    }

    @Override
    public void onError(Exception e) {
        log.error(String.format("WebSocket connection to {} failed with error", uri), e);
    }

    public void setListener(WebSocketListener listener) {
        this.listener = listener;
    }
}
