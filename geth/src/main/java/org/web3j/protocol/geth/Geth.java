package org.web3j.protocol.geth;

import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.geth.response.PersonalEcRecover;
import org.web3j.protocol.geth.response.PersonalImportRawKey;

/**
 * JSON-RPC Request object building factory for Geth. 
 */
public interface Geth extends Admin {
    public Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password);
    
    public Request<?, BooleanResponse> personalLockAccount(String accountId);
    
    public Request<?, PersonalSign> personalSign(String message, String accountId, String password);
    
    public Request<?, PersonalEcRecover> personalEcRecover(String message, String signiture);
    
}
