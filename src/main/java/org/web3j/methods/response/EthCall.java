package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_call
 */
public class EthCall extends Response<String> {
    public String getValue() {
        return getResult();
    }
}
