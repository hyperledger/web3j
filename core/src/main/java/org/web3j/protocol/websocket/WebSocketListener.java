package org.web3j.protocol.websocket;

import java.io.IOException;

public interface WebSocketListener {
    void onMessage(String message) throws IOException;
}
