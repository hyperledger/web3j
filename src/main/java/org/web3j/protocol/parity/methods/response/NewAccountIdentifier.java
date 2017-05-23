package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * Account id object returned by below methods.
 * <ul>
 * <li>personal_newAccount</li>
 * <li>personal_newAccountFromPhrase</li>
 * <li>personal_newAccountFromWallet</li>
 * </ul>
 */
public class NewAccountIdentifier extends Response<String> {
    public String getAccountId() {
        return getResult();
    }
}
