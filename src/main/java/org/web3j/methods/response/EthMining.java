package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_mining
 */
public class EthMining extends Response<Boolean> {
    public boolean isMining() {
        return getResult();
    }
}
