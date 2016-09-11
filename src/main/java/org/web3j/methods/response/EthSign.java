package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_sign
 */
public class EthSign extends Response<String> {
    public String getSignature() {
        return getResult();
    }
}
