package org.web3j.protocol.parity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.parity.methods.request.Derivation;
import org.web3j.protocol.parity.methods.request.TraceFilter;
import org.web3j.protocol.parity.methods.response.ParityAddressesResponse;
import org.web3j.protocol.parity.methods.response.ParityAllAccountsInfo;
import org.web3j.protocol.parity.methods.response.ParityDefaultAddressResponse;
import org.web3j.protocol.parity.methods.response.ParityDeriveAddress;
import org.web3j.protocol.parity.methods.response.ParityExportAccount;
import org.web3j.protocol.parity.methods.response.ParityFullTraceResponse;
import org.web3j.protocol.parity.methods.response.ParityListRecentDapps;
import org.web3j.protocol.parity.methods.response.ParityTraceGet;
import org.web3j.protocol.parity.methods.response.ParityTracesResponse;
import org.web3j.utils.Numeric;

/**
 * JSON-RPC 2.0 factory implementation for Parity.
 */
public class JsonRpc2_0Parity extends JsonRpc2_0Admin implements Parity {

    public JsonRpc2_0Parity(Web3jService web3jService) {
        super(web3jService);
    }
    
    @Override
    public Request<?, ParityAllAccountsInfo> parityAllAccountsInfo() {
        return new Request<String, ParityAllAccountsInfo>(
                "parity_allAccountsInfo",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityAllAccountsInfo.class);
    }

    @Override
    public Request<?, BooleanResponse> parityChangePassword(
            String accountId, String oldPassword, String newPassword) {
        return new Request<String, BooleanResponse>(
                "parity_changePassword",
                Arrays.asList(accountId, oldPassword, newPassword),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressHash(
            String accountId, String password, Derivation hashType, boolean toSave) {
        return new Request<Object, ParityDeriveAddress>(
                "parity_deriveAddressHash",
                Arrays.asList(accountId, password, hashType, toSave),
                ID,
                web3jService,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressIndex(
            String accountId, String password,
            List<Derivation> indicesType, boolean toSave) {
        return new Request<Object, ParityDeriveAddress>(
                "parity_deriveAddressIndex",
                Arrays.asList(accountId, password, indicesType, toSave),
                ID,
                web3jService,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityExportAccount> parityExportAccount(
            String accountId, String password) {
        return new Request<String, ParityExportAccount>(
                "parity_exportAccount",
                Arrays.asList(accountId, password),
                ID,
                web3jService,
                ParityExportAccount.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetDappAddresses(String dAppId) {
        return new Request<String, ParityAddressesResponse>(
                "parity_getDappAddresses",
                Arrays.asList(dAppId),
                ID,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(String dAppId) {
        return new Request<String, ParityDefaultAddressResponse>(
                "parity_getDappDefaultAddress",
                Arrays.asList(dAppId),
                ID,
                web3jService,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetNewDappsAddresses() {
        return new Request<String, ParityAddressesResponse>(
                "parity_getNewDappsAddresses",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress() {
        return new Request<String, ParityDefaultAddressResponse>(
                "parity_getNewDappsDefaultAddress",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityImportGethAccounts(
            ArrayList<String> gethAddresses) {
        return new Request<String, ParityAddressesResponse>(
                "parity_importGethAccounts",
                gethAddresses,
                ID,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityKillAccount(String accountId, String password) {
        return new Request<String, BooleanResponse>(
                "parity_killAccount",
                Arrays.asList(accountId, password),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityListGethAccounts() {
        return new Request<String, ParityAddressesResponse>(
                "parity_listGethAccounts",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityListRecentDapps> parityListRecentDapps() {
        return new Request<String, ParityListRecentDapps>(
                "parity_listRecentDapps",
                Collections.<String>emptyList(),
                ID,
                web3jService,
                ParityListRecentDapps.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(
            String phrase, String password) {
        return new Request<String, NewAccountIdentifier>(
                "parity_newAccountFromPhrase",
                Arrays.asList(phrase, password),
                ID,
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromSecret(
            String secret, String password) {
        return new Request<String, NewAccountIdentifier>(
                "parity_newAccountFromSecret",
                Arrays.asList(secret, password),
                ID,
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromWallet(
            WalletFile walletFile, String password) {
        return new Request<Object, NewAccountIdentifier>(
                "parity_newAccountFromWallet",
                Arrays.asList(walletFile, password),
                ID,
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, BooleanResponse> parityRemoveAddress(String accountId) {
        return new Request<String, BooleanResponse>(
                "parity_RemoveAddress",
                Arrays.asList(accountId),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountMeta(
            String accountId, Map<String, Object> metadata) {
        return new Request<Object, BooleanResponse>(
                "parity_setAccountMeta",
                Arrays.asList(accountId, metadata),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountName(
            String address, String name) {
        return new Request<String, BooleanResponse>(
                "parity_setAccountName",
                Arrays.asList(address, name),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappAddresses(
            String dAppId, ArrayList<String> availableAccountIds) {
        return new Request<java.io.Serializable, BooleanResponse>(
                "parity_setDappAddresses",
                Arrays.asList(dAppId, availableAccountIds),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappDefaultAddress(
            String dAppId, String defaultAddress) {
        return new Request<String, BooleanResponse>(
                "parity_setDappDefaultAddress",
                Arrays.asList(dAppId, defaultAddress),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsAddresses(
            ArrayList<String> availableAccountIds) {
        return new Request<ArrayList<String>, BooleanResponse>(
                "parity_setNewDappsAddresses",
                Arrays.asList(availableAccountIds),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(String defaultAddress) {
        return new Request<String, BooleanResponse>(
                "parity_setNewDappsDefaultAddress",
                Arrays.asList(defaultAddress),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityTestPassword(String accountId, String password) {
        return new Request<String, BooleanResponse>(
                "parity_testPassword",
                Arrays.asList(accountId, password),
                ID,
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> paritySignMessage(
            String accountId, String password, String hexMessage) {
        return new Request<String, PersonalSign>(
                "parity_signMessage",
                Arrays.asList(accountId,password,hexMessage),
                ID,
                web3jService,
                PersonalSign.class);
    }
    
    // TRACE API
    
    @Override
    public Request<?, ParityFullTraceResponse> traceCall(
            Transaction transaction, List<String> traces, DefaultBlockParameter blockParameter) {
        return new Request<Object, ParityFullTraceResponse>(
            "trace_call",
            Arrays.asList(transaction, traces, blockParameter),
            ID,
            web3jService,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityFullTraceResponse> traceRawTransaction(
            String data, List<String> traceTypes) {
        return new Request<Object, ParityFullTraceResponse>(
            "trace_rawTransaction",
            Arrays.asList(data, traceTypes),
            ID,
            web3jService,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityFullTraceResponse> traceReplayTransaction(
            String hash, List<String> traceTypes) {
        return new Request<Object, ParityFullTraceResponse>(
            "trace_replayTransaction",
            Arrays.asList(hash, traceTypes),
            ID,
            web3jService,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityTracesResponse> traceBlock(DefaultBlockParameter blockParameter) {
        return new Request<DefaultBlockParameter, ParityTracesResponse>(
            "trace_block",
            Arrays.asList(blockParameter),
            ID,
            web3jService,
            ParityTracesResponse.class);
    }
    
    @Override
    public Request<?, ParityTracesResponse> traceFilter(TraceFilter traceFilter) {
        return new Request<TraceFilter, ParityTracesResponse>(
            "trace_filter",
            Arrays.asList(traceFilter),
            ID,
            web3jService,
            ParityTracesResponse.class);
    }
    
    @Override
    public Request<?, ParityTraceGet> traceGet(String hash, List<BigInteger> indices) {

        List<String> encodedIndices = new ArrayList<String>(indices.size());
        for (BigInteger index : indices) {
            encodedIndices.add(Numeric.encodeQuantity(index));
        }
        return new Request<Object, ParityTraceGet>(
            "trace_get",
            Arrays.asList(hash, encodedIndices),
            ID,
            web3jService,
            ParityTraceGet.class);
    }

    @Override
    public Request<?, ParityTracesResponse> traceTransaction(String hash) {
        return new Request<String, ParityTracesResponse>(
            "trace_transaction",
            Arrays.asList(hash),
            ID,
            web3jService,
            ParityTracesResponse.class);
    }
}
