package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getCode
 */
public class EthGetCode extends Response<String> {
    public String getCode() {
        return getResult();
    }
}
