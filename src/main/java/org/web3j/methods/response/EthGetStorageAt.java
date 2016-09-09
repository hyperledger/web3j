package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getStorageAt
 */
public class EthGetStorageAt extends Response<String> {
    public String getData() {
        return getResult();
    }
}
