package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_protocolVersion
 */
public class EthProtocolVersion extends Response<String> {
    public String getProtocolVersion() {
        return getResult();
    }
}
