package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * eth_mining
 */
public class EthMining extends Response<Boolean> {
    public boolean isMining() {
        return getResult();
    }
}
