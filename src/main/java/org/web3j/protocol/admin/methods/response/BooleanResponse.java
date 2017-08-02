package org.web3j.protocol.admin.methods.response;

import org.web3j.protocol.core.Response;

/**
 * personal_unlockAccount.
 */
public class BooleanResponse extends Response<Boolean>{
    public boolean success(){
        return getResult();
    }
}
