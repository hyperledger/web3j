package org.web3j.protocol.geth;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.Personal;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.geth.methods.response.PersonalEcRecover;
import org.web3j.protocol.geth.methods.response.PersonalImportRawKey;
import org.web3j.protocol.parity.methods.response.BooleanResponse;

/**
 * JSON-RPC Request object building factory for Geth. 
 */
public interface Geth extends Personal {
    static Geth build(Web3jService web3jService) {
        return new JsonRpc2_0Geth(web3jService);
    }
        
    public Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password);
    
    public Request<?, BooleanResponse> personalLockAccount(String accountId);
    
    public Request<?, PersonalSign> personalSign(String message, String accountId, String password);
    
    public Request<?, PersonalEcRecover> personalEcRecover(String message, String signiture);
    
}
