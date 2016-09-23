package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * shh_hasIdentity
 */
public class ShhHasIdentity extends Response<Boolean> {

    public boolean hasPrivateKeyForIdentity() {
        return getResult();
    }
}
