package org.web3j.protocol.geth;

import java.util.Arrays;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.JsonRpc2_0Personal;
import org.web3j.protocol.parity.methods.response.BooleanResponse;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.geth.methods.response.PersonalEcRecover;
import org.web3j.protocol.geth.methods.response.PersonalImportRawKey;
import org.web3j.protocol.admin.methods.response.PersonalSign;

/**
 * JSON-RPC 2.0 factory implementation for Geth.
 */
class JsonRpc2_0Geth extends JsonRpc2_0Personal implements Geth {

    public JsonRpc2_0Geth(Web3jService web3jService) {
        super(web3jService);
    }
    
    @Override
    public Request<?, PersonalImportRawKey> personalImportRawKey(
            String keydata, String password) {
        return new Request<>(
                "personal_importRawKey",
                Arrays.asList(keydata, password),
                ID,
                web3jService,
                PersonalImportRawKey.class);
    }

    @Override
    public Request<?, BooleanResponse> personalLockAccount(String accountId) {
        return new Request<>(
                "personal_lockAccount",
                Arrays.asList(accountId),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> personalSign(
            String message, String accountId, String password) {
        return new Request<>(
                "personal_sign",
                Arrays.asList(message,accountId,password),
                ID,
                web3jService,
                PersonalSign.class);
    }

    @Override
    public Request<?, PersonalEcRecover> personalEcRecover(
            String hexMessage, String signedMessage) {
        return new Request<>(
                "personal_ecRecover",
                Arrays.asList(hexMessage,signedMessage),
                ID,
                web3jService,
                PersonalEcRecover.class);
    } 
    
}
