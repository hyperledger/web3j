package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * net_listening
 */
public class NetListening extends Response<Boolean> {
    public boolean isListening() {
        return getResult();
    }
}
