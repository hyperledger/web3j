package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * shh_newIdentity
 */
public class ShhNewIdentity extends Response<String> {

    public String getAddress() {
        return getResult();
    }
}
