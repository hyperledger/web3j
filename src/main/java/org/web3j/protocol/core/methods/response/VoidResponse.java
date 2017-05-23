package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * Null response object returned by the below methods.
 * <ul>
 * <li>personal_setAccountName</li>
 * <li>personal_setAccountMeta</li>
 * </ul>
 */
public class VoidResponse extends Response<Void> {
    public boolean isValid() {
        return !hasError();
    }
}
