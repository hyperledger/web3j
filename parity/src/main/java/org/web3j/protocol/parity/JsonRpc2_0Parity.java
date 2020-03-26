/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.parity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

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

/** JSON-RPC 2.0 factory implementation for Parity. */
public class JsonRpc2_0Parity extends JsonRpc2_0Admin implements Parity {

    public JsonRpc2_0Parity(final Web3jService web3jService) {
        super(web3jService);
    }

    public JsonRpc2_0Parity(
            final Web3jService web3jService,
            final long pollingInterval,
            final ScheduledExecutorService scheduledExecutorService) {
        super(web3jService, pollingInterval, scheduledExecutorService);
    }

    @Override
    public Request<?, ParityAllAccountsInfo> parityAllAccountsInfo() {
        return new Request<>(
                "parity_allAccountsInfo",
                Collections.<String>emptyList(),
                web3jService,
                ParityAllAccountsInfo.class);
    }

    @Override
    public Request<?, BooleanResponse> parityChangePassword(
            final String accountId, final String oldPassword, final String newPassword) {
        return new Request<>(
                "parity_changePassword",
                Arrays.asList(accountId, oldPassword, newPassword),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressHash(
            final String accountId,
            final String password,
            final Derivation hashType,
            final boolean toSave) {
        return new Request<>(
                "parity_deriveAddressHash",
                Arrays.asList(accountId, password, hashType, toSave),
                web3jService,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressIndex(
            final String accountId,
            final String password,
            final List<Derivation> indicesType,
            final boolean toSave) {
        return new Request<>(
                "parity_deriveAddressIndex",
                Arrays.asList(accountId, password, indicesType, toSave),
                web3jService,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityExportAccount> parityExportAccount(
            final String accountId, final String password) {
        return new Request<>(
                "parity_exportAccount",
                Arrays.asList(accountId, password),
                web3jService,
                ParityExportAccount.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetDappAddresses(final String dAppId) {
        return new Request<>(
                "parity_getDappAddresses",
                Arrays.asList(dAppId),
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(
            final String dAppId) {
        return new Request<>(
                "parity_getDappDefaultAddress",
                Arrays.asList(dAppId),
                web3jService,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetNewDappsAddresses() {
        return new Request<>(
                "parity_getNewDappsAddresses",
                Collections.<String>emptyList(),
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress() {
        return new Request<>(
                "parity_getNewDappsDefaultAddress",
                Collections.<String>emptyList(),
                web3jService,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityImportGethAccounts(
            final ArrayList<String> gethAddresses) {
        return new Request<>(
                "parity_importGethAccounts",
                gethAddresses,
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityKillAccount(
            final String accountId, final String password) {
        return new Request<>(
                "parity_killAccount",
                Arrays.asList(accountId, password),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityListAccounts(
            final BigInteger quantity,
            final String accountId,
            final DefaultBlockParameter blockParameter) {
        if (blockParameter == null) {
            return new Request<>(
                    "parity_listAccounts",
                    Arrays.asList(quantity, accountId),
                    web3jService,
                    ParityAddressesResponse.class);
        } else {
            return new Request<>(
                    "parity_listAccounts",
                    Arrays.asList(quantity, accountId, blockParameter),
                    web3jService,
                    ParityAddressesResponse.class);
        }
    }

    @Override
    public Request<?, ParityAddressesResponse> parityListGethAccounts() {
        return new Request<>(
                "parity_listGethAccounts",
                Collections.<String>emptyList(),
                web3jService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityListRecentDapps> parityListRecentDapps() {
        return new Request<>(
                "parity_listRecentDapps",
                Collections.<String>emptyList(),
                web3jService,
                ParityListRecentDapps.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(
            final String phrase, final String password) {
        return new Request<>(
                "parity_newAccountFromPhrase",
                Arrays.asList(phrase, password),
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromSecret(
            final String secret, final String password) {
        return new Request<>(
                "parity_newAccountFromSecret",
                Arrays.asList(secret, password),
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromWallet(
            final WalletFile walletFile, final String password) {
        return new Request<>(
                "parity_newAccountFromWallet",
                Arrays.asList(walletFile, password),
                web3jService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, BooleanResponse> parityRemoveAddress(final String accountId) {
        return new Request<>(
                "parity_RemoveAddress",
                Arrays.asList(accountId),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountMeta(
            final String accountId, final Map<String, Object> metadata) {
        return new Request<>(
                "parity_setAccountMeta",
                Arrays.asList(accountId, metadata),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountName(
            final String address, final String name) {
        return new Request<>(
                "parity_setAccountName",
                Arrays.asList(address, name),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappAddresses(
            final String dAppId, final ArrayList<String> availableAccountIds) {
        return new Request<>(
                "parity_setDappAddresses",
                Arrays.asList(dAppId, availableAccountIds),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappDefaultAddress(
            final String dAppId, final String defaultAddress) {
        return new Request<>(
                "parity_setDappDefaultAddress",
                Arrays.asList(dAppId, defaultAddress),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsAddresses(
            final ArrayList<String> availableAccountIds) {
        return new Request<>(
                "parity_setNewDappsAddresses",
                Arrays.asList(availableAccountIds),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(
            final String defaultAddress) {
        return new Request<>(
                "parity_setNewDappsDefaultAddress",
                Arrays.asList(defaultAddress),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityTestPassword(
            final String accountId, final String password) {
        return new Request<>(
                "parity_testPassword",
                Arrays.asList(accountId, password),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> paritySignMessage(
            final String accountId, final String password, final String hexMessage) {
        return new Request<>(
                "parity_signMessage",
                Arrays.asList(accountId, password, hexMessage),
                web3jService,
                PersonalSign.class);
    }

    // TRACE API

    @Override
    public Request<?, ParityFullTraceResponse> traceCall(
            final Transaction transaction,
            final List<String> traces,
            final DefaultBlockParameter blockParameter) {
        return new Request<>(
                "trace_call",
                Arrays.asList(transaction, traces, blockParameter),
                web3jService,
                ParityFullTraceResponse.class);
    }

    @Override
    public Request<?, ParityFullTraceResponse> traceRawTransaction(
            final String data, final List<String> traceTypes) {
        return new Request<>(
                "trace_rawTransaction",
                Arrays.asList(data, traceTypes),
                web3jService,
                ParityFullTraceResponse.class);
    }

    @Override
    public Request<?, ParityFullTraceResponse> traceReplayTransaction(
            final String hash, final List<String> traceTypes) {
        return new Request<>(
                "trace_replayTransaction",
                Arrays.asList(hash, traceTypes),
                web3jService,
                ParityFullTraceResponse.class);
    }

    @Override
    public Request<?, ParityTracesResponse> traceBlock(final DefaultBlockParameter blockParameter) {
        return new Request<>(
                "trace_block",
                Arrays.asList(blockParameter),
                web3jService,
                ParityTracesResponse.class);
    }

    @Override
    public Request<?, ParityTracesResponse> traceFilter(final TraceFilter traceFilter) {
        return new Request<>(
                "trace_filter",
                Arrays.asList(traceFilter),
                web3jService,
                ParityTracesResponse.class);
    }

    @Override
    public Request<?, ParityTraceGet> traceGet(final String hash, final List<BigInteger> indices) {
        final List<String> encodedIndices =
                indices.stream().map(Numeric::encodeQuantity).collect(Collectors.toList());
        return new Request<>(
                "trace_get",
                Arrays.asList(hash, encodedIndices),
                web3jService,
                ParityTraceGet.class);
    }

    @Override
    public Request<?, ParityTracesResponse> traceTransaction(final String hash) {
        return new Request<>(
                "trace_transaction", Arrays.asList(hash), web3jService, ParityTracesResponse.class);
    }
}
