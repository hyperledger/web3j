package org.web3j.protocol.parity.methods.response;

import org.web3j.protocol.core.Response;

/**
 * personal_unlockAccount.
 */
public class BooleanResponse extends Response<Boolean>{
    public boolean success(){
        return getResult();
    }
}
