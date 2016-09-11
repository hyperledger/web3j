package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

import java.util.List;

/**
 * eth_getWork
 */
public class EthGetWork extends Response<List<String>> {

    public String getCurrentBlockHeaderPowHash() {
        return getResult().get(0);
    }

    public String getSeedHashForDag() {
        return getResult().get(1);
    }

    public String getBoundaryCondition() {
        return getResult().get(2);
    }
}
