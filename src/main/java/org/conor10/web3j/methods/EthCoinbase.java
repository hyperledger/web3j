package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * eth_coinbase
 */
public class EthCoinbase extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
