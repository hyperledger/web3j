package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * web3_clientVersion
 */
public class Web3ClientVersion extends Response<String> {

    public String getWeb3ClientVersion() {
        return getResult();
    }
}
