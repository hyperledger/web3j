package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * web3_clientVersion
 */
public class Web3ClientVersion extends Response<String> {

    public String getWeb3ClientVersion() {
        return getResult();
    }
}
