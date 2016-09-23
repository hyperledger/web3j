package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * <p>Account id object returned by:
 * <ul>
 * <li>personal_newAccount</li>
 * <li>personal_newAccountFromPhrase</li>
 * <li>personal_newAccountFromWallet</li>
 * </ul>
 * </p>
 */
public class NewAccountIdentifier extends Response<String> {
    public String getAccountId() {
        return getResult();
    }
}
