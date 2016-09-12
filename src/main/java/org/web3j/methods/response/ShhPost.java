package org.web3j.methods.response;

import org.web3j.protocol.jsonrpc20.Response;

/**
 * shh_post
 */
public class ShhPost extends Response<Boolean> {

    public boolean messageSent() {
        return getResult();
    }
}
