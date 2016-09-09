package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * net_listening
 */
public class NetListening extends Response<Boolean> {
    public boolean isListening() {
        return getResult();
    }
}
