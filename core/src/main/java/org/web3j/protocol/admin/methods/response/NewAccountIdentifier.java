package org.web3j.protocol.admin.methods.response;

import org.web3j.protocol.core.Response;

/**
 * personal_newAccount
 * parity_newAccountFromPhrase
 * parity_newAccountFromSecret
 * parity_newAccountFromWallet.
 */
public class NewAccountIdentifier extends Response<String> {
    public String getAccountId() {
        return getResult();
    }    
}
