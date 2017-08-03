package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * signer_rejectRequest.
 */
public class SignerRejectRequest extends Response<Boolean> {
    public boolean isRejected() {
        return getResult();
    }
}
