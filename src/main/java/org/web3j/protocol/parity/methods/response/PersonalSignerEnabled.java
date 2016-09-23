package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * personal_signerEnabled
 */
public class PersonalSignerEnabled extends Response<Boolean> {
    public boolean isSignerEnabled() {
        return getResult();
    }
}
