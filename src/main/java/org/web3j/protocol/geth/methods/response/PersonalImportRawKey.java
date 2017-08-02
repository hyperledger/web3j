package org.web3j.protocol.geth.methods.response;

import org.web3j.protocol.core.Response;

/**
 *
 * @author ivaylo
 */
public class PersonalImportRawKey extends Response<String>{
    public String getAccountId() {
        return getResult();
    }
}
