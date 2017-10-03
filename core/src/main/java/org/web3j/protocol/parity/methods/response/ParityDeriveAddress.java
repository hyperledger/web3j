package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * parity_deriveAddressHash
 * parity_deriveAddressIndex.
 */
public class ParityDeriveAddress extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
