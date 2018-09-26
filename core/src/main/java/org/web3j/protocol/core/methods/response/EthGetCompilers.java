package org.web3j.protocol.core.methods.response;

import java.util.List;

import org.web3j.protocol.core.Response;

/**
 * eth_getCompilers.
 */
public class EthGetCompilers extends Response<List<String>> {
    public List<String> getCompilers() {
        return getResult();
    }
}
