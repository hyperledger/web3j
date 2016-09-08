package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * web3_clientVersion
 */
public class Web3ClientVersion extends Response<String> {

    public String getWeb3ClientVersion() {
        return getResult();
    }
}
