package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * eth_protocolVersion
 */
public class EthProtocolVersion extends Response<String> {
    public String getProtocolVersion() {
        return getResult();
    }
}
