package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * net_version
 */
public class NetVersion extends Response<String> {
    public String getNetVersion() {
        return getResult();
    }
}
