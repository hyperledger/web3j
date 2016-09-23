package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * eth_compileLLL
 */
public class EthCompileLLL extends Response<String> {
    public String getCompiledSourceCode() {
        return getResult();
    }
}
