package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * shh_newGroup
 */
public class ShhNewGroup extends Response<String> {

    public String getAddress() {
        return getResult();
    }
}
