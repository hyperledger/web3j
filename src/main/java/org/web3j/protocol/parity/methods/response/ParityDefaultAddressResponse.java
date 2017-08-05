package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * parity_getDappDefaultAddress
 * parity_getNewDappsDefaultAddress.
 */
public class ParityDefaultAddressResponse extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
