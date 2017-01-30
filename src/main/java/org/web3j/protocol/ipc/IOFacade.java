package org.web3j.protocol.ipc;

import java.io.IOException;

/**
 * Simple IO facade for the &#42nix & Windows IPC implementations.
 */
public interface IOFacade {
    void write(String payload) throws IOException;
    String read() throws IOException;
}
