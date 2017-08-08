package org.web3j.protocol.parity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.JsonRpc2_0Personal;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.parity.methods.response.BooleanResponse;
import org.web3j.protocol.parity.methods.response.ParityAddressesResponse;
import org.web3j.protocol.parity.methods.response.ParityAllAccountsInfo;
import org.web3j.protocol.parity.methods.response.ParityDefaultAddressResponse;
import org.web3j.protocol.parity.methods.response.ParityDeriveAddress;
import org.web3j.protocol.parity.methods.response.ParityExportAccount;
import org.web3j.protocol.parity.methods.response.ParityListRecentDapps;

/**
 * JSON-RPC 2.0 factory implementation for Parity.
 */
public class JsonRpc2_0Parity extends JsonRpc2_0Personal implements Parity {

    public JsonRpc2_0Parity(Web3jService web3jService) {
        super(web3jService);
    }
    
    @Override
    public Request<?, ParityAllAccountsInfo> parityAllAccountsInfo() {
        return new Request<>(
                "parity_allAccountsInfo",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityAllAccountsInfo.class);
    }

    @Override
    public Request<?, BooleanResponse> parityChangePassword(
            String accountId, String oldPassword, String newPassword) {
        return new Request<>(
                "parity_changePassword",
                Arrays.asList(accountId, oldPassword, newPassword),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressHash(
            String accountId, String password, Map<String, Object> hashType, boolean toSave) {
        return new Request<>(
                "parity_deriveAddressHash",
                Arrays.asList(accountId, password, hashType, toSave),
                ID,
                web3jService,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressIndex(
            String accountId, String password,
            List<Map<String, Object>> indexType, boolean toSave) {
        return new Request<>(
                "parity_deriveAddressIndex",
                Arrays.asList(accountId, password, indexType, toSave),
                ID,
                web3jService,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityExportAccount> parityExportAccount(
            String accountId, String password) {
        return new Request<>(
                "parity_exportAccount",
                Arrays.asList(accountId, password),
                ID,
                web3jService,
                ParityExportAccount.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetDappAddresses(String dAppId) {
        return new Request<>(
                "parity_getDappAddresses",
                Arrays.asList(dAppId),
                ID,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(String dAppId) {
        return new Request<>(
                "parity_getDappDefaultAddress",
                Arrays.asList(dAppId),
                ID,
                web3jService,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetNewDappsAddresses() {
        return new Request<>(
                "parity_getNewDappsAddresses",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress() {
        return new Request<>(
                "parity_getNewDappsDefaultAddress",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityImportGethAccounts(
            ArrayList<String> gethAddresses) {
        return new Request<>(
                "parity_importGethAccounts",
                gethAddresses,
                ID,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityKillAccount(String accountId, String password) {
        return new Request<>(
                "parity_killAccount",
                Arrays.asList(accountId, password),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityListGethAccounts() {
        return new Request<>(
                "parity_listGethAccounts",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityListRecentDapps> parityListRecentDapps() {
        return new Request<>(
                "parity_listRecentDapps",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityListRecentDapps.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(
            String phrase, String password) {
        return new Request<>(
                "parity_newAccountFromPhrase",
                Arrays.asList(phrase, password),
                ID,
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromSecret(
            String secret, String password) {
        return new Request<>(
                "parity_newAccountFromSecret",
                Arrays.asList(secret, password),
                ID,
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromWallet(
            WalletFile walletFile, String password) {
        return new Request<>(
                "parity_newAccountFromWallet",
                Arrays.asList(walletFile, password),
                ID,
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, BooleanResponse> parityRemoveAddress(String accountId) {
        return new Request<>(
                "parity_RemoveAddress",
                Arrays.asList(accountId),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountMeta(
            String accountId, Map<String, Object> metadata) {
        return new Request<>(
                "parity_setAccountMeta",
                Arrays.asList(accountId, metadata),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountName(
            String address, String name) {
        return new Request<>(
                "parity_setAccountName",
                Arrays.asList(address, name),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappAddresses(
            String dAppId, ArrayList<String> availableAccountIds) {
        return new Request<>(
                "parity_setDappAddresses",
                Arrays.asList(dAppId, availableAccountIds),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappDefaultAddress(
            String dAppId, String defaultAddress) {
        return new Request<>(
                "parity_setDappDefaultAddress",
                Arrays.asList(dAppId, defaultAddress),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsAddresses(
            ArrayList<String> availableAccountIds) {
        return new Request<>(
                "parity_setNewDappsAddresses",
                Arrays.asList(availableAccountIds),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(String defaultAddress) {
        return new Request<>(
                "parity_setNewDappsDefaultAddress",
                Arrays.asList(defaultAddress),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityTestPassword(String accountId, String password) {
        return new Request<>(
                "parity_testPassword",
                Arrays.asList(accountId, password),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> paritySignMessage(
            String accountId, String password, String hexMessage) {
        return new Request<>(
                "parity_signMessage",
                Arrays.asList(accountId,password,hexMessage),
                ID,
                web3jService,
                PersonalSign.class);
    }
}
