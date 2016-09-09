package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_coinbase
 */
public class EthCoinbase extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
