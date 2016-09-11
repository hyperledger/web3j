package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

import java.util.List;

/**
 * eth_getCompilers
 */
public class EthGetCompilers extends Response<List<String>> {
    public List<String> getCompilers() {
        return getResult();
    }
}
