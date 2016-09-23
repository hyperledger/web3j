package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * <p>Null response object returned by:
 * <ul>
 * <li>personal_setAccountName</li>
 * <li>personal_setAccountMeta</li>
 * </ul>
 * </p>
 */
public class VoidResponse extends Response<Void> {
    public boolean isValid() {
        return !hasError();
    }
}
