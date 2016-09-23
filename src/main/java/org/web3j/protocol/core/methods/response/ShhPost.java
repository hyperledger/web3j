package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * shh_post
 */
public class ShhPost extends Response<Boolean> {

    public boolean messageSent() {
        return getResult();
    }
}
