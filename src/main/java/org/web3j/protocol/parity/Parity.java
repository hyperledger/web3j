package org.web3j.protocol.parity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.Personal;
import org.web3j.protocol.parity.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.parity.methods.response.ParityAllAccountsInfo;
import org.web3j.protocol.parity.methods.response.ParityDeriveAddress;
import org.web3j.protocol.parity.methods.response.ParityExportAccount;
import org.web3j.protocol.parity.methods.response.ParityAddressesResponse;
import org.web3j.protocol.parity.methods.response.ParityDefaultAddressResponse;
import org.web3j.protocol.parity.methods.response.ParityListRecentDapps;
import org.web3j.protocol.parity.methods.response.SignerRejectRequest;
import org.web3j.protocol.parity.methods.response.SignerRequestsToConfirm;

/**
 * JSON-RPC Request object building factory for Parity.
 */
public interface Parity extends Personal {
    static Parity build(Web3jService web3jService) {
        return new JsonRpc2_0Parity(web3jService);
    }
    
    Request<?, ParityAllAccountsInfo> parityAllAccountsInfo();
    
    Request<?, BooleanResponse> parityChangePassword(String accountId, String oldPass, String newPass);
    
    Request<?, ParityDeriveAddress> parityDeriveAddressHash(String accountId, String password, Map<String, Object> hashType, boolean toSave);
    
    Request<?, ParityDeriveAddress> parityDeriveAddressIndex(String accountId, String password, List<Map<String, Object>> indexType, boolean toSave);
    
    Request<?, ParityExportAccount> parityExportAccount(String accountId, String password);
    
    Request<?, ParityAddressesResponse> parityGetDappAddresses(String dAppId);
    
    Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(String dAppId);
    
    Request<?, ParityAddressesResponse> parityGetNewDappsAddresses();
    
    Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress();
    
    Request<?, ParityAddressesResponse> parityImportGethAccounts(ArrayList<String> gethAddresses);
    
    Request<?, BooleanResponse> parityKillAccount(String accountId, String password);
    
    Request<?, ParityAddressesResponse> parityListGethAccounts();
    
    Request<?, ParityListRecentDapps> parityListRecentDapps();
    
    Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(String phrase, String password);
    
    Request<?, NewAccountIdentifier> parityNewAccountFromSecret(String secret, String password);
    
    Request<?, NewAccountIdentifier> parityNewAccountFromWallet(WalletFile walletFile, String password);
    
    Request<?, BooleanResponse> parityRemoveAddress(String accountId);
    
    Request<?, BooleanResponse> paritySetAccountMeta(String accountId, Map<String, Object> metadata);
    
    Request<?, BooleanResponse> paritySetAccountName(String accountId, String newAccountName);
    
    Request<?, BooleanResponse> paritySetDappAddresses(String dAppId, ArrayList<String> availableAccountIds);
    
    Request<?, BooleanResponse> paritySetDappDefaultAddress(String dAppId, String defaultAddress);
    
    Request<?, BooleanResponse> paritySetNewDappsAddresses(ArrayList<String> availableAccountIds);
    
    Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(String defaultAddress);
    
    Request<?, BooleanResponse> parityTestPassword(String accountId, String password);
    
    Request<?, PersonalSign> paritySignMessage(String accountId, String password, String hexMessage);    

    Request<?, SignerRequestsToConfirm> signerRequestsToConfirm();

    Request<?, EthSendTransaction> signerConfirmRequest(
            String requestId, Transaction transaction, String password);

    Request<?, SignerRejectRequest> signerRejectRequest(String requestId);
}
