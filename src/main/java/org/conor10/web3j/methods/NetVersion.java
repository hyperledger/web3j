package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * net_version
 */
public class NetVersion extends Response<String> {
    public String getNetVersion() {
        return getResult();
    }
}
