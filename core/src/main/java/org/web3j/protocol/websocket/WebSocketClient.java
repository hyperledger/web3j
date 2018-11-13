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

    private Optional<WebSocketListener> listenerOpt = Optional.empty();

    public WebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public WebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("Opened WebSocket connection to {}", uri);
    }

    @Override
    public void onMessage(String s) {
        log.debug("Received message {} from server {}", s, uri);
        listenerOpt.ifPresent(listener -> {
            try {
                listener.onMessage(s);
            } catch (Exception e) {
                log.error("Failed to process message '{}' from server {}", s, uri, e);
            }
        });
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("Closed WebSocket connection to {}, because of reason: '{}'."
                + "Connection closed remotely: {}", uri, reason, remote);
        listenerOpt.ifPresent(WebSocketListener::onClose);
    }

    @Override
    public void onError(Exception e) {
        log.error("WebSocket connection to {} failed with error", uri, e);
        listenerOpt.ifPresent(listener -> listener.onError(e));
    }

    /**
     * Set a listener that will be called when a new message is received by the client.
     *
     * @param listener WebSocket listener
     */
    public void setListener(WebSocketListener listener) {
        this.listenerOpt = Optional.ofNullable(listener);
    }
}
