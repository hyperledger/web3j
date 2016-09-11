package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * shh_version
 */
public class SshVersion extends Response<String> {

    public String getVersion() {
        return getResult();
    }
}
