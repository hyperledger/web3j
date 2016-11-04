package org.web3j.protocol.parity;

import java.math.BigInteger;
import java.util.Map;

import org.web3j.crypto.WalletFile;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.parity.methods.response.*;

/**
 * JSON-RPC Request object building factory for Parity.
 */
public interface Parity extends Web3j {
    static Parity build(Web3jService web3jService) {
        return new JsonRpc2_0Parity(web3jService);
    }

    Request<?, PersonalSignerEnabled> personalSignerEnabled();

    Request<?, PersonalListAccounts> personalListAccounts();

    Request<?, NewAccountIdentifier> personalNewAccount(String password);

    Request<?, NewAccountIdentifier> personalNewAccountFromPhrase(String phrase, String password);

    Request<?, NewAccountIdentifier> personalNewAccountFromWallet(WalletFile walletFile, String password);

    Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String accountId, String password, BigInteger duration);

    Request<?, PersonalUnlockAccount> personalUnlockAccount(String accountId, String password);

    Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> personalSignAndSendTransaction(
            Transaction transaction, String password);

    Request<?, VoidResponse> personalSetAccountName(
            String accountId, String newAccountName);

    Request<?, VoidResponse> personalSetAccountMeta(String accountId, Map<String, Object> metadata);

    Request<?, PersonalAccountsInfo> personalAccountsInfo();

    Request<?, PersonalRequestsToConfirm> personalRequestsToConfirm();

    Request<?, org.web3j.protocol.core.methods.response.EthSendTransaction> personalConfirmRequest(
            String requestId, Transaction transaction, String password);

    Request<?, PersonalRejectRequest> personalRejectRequest(String requestId);
}
