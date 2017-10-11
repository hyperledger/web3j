package org.web3j.protocol.admin.methods.response;

import org.web3j.protocol.core.Response;

/**
 * Boolean response type.
 */
public class BooleanResponse extends Response<Boolean> {
    public boolean success() {
        return getResult();
    }
}
