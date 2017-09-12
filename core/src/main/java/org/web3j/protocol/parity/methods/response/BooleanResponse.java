package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * parity_changePassword
 * parity_killAccount
 * parity_removeAddress
 * parity_setAccountMeta
 * parity_setAccountName
 * parity_setDappAddresses
 * parity_setDappDefaultAddress
 * parity_setNewDappsAddresses
 * parity_setNewDappsDefaultAddress
 * parity_signMessage.
 */
public class BooleanResponse extends Response<Boolean> {
    public boolean success() {
        return getResult();
    }
}
