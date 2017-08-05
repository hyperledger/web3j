package org.web3j.protocol.geth.methods.response;

import org.web3j.protocol.core.Response;

/**
 * personal_importRawKey.
 */
public class PersonalImportRawKey extends Response<String>{
    public String getAccountId() {
        return getResult();
    }
}
