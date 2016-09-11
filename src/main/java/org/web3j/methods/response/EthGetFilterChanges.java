package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

import java.util.List;

/**
 * eth_getFilterChanges
 */
public class EthGetFilterChanges extends Response<List<Log>> {

    public List<Log> getLogObjects() {
        return getResult();
    }
}
