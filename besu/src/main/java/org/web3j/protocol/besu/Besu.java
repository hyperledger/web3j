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
package org.web3j.protocol.besu;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.besu.response.BesuEthAccountsMapResponse;
import org.web3j.protocol.besu.response.BesuFullDebugTraceResponse;
import org.web3j.protocol.besu.response.BesuSignerMetrics;
import org.web3j.protocol.besu.response.privacy.PrivCreatePrivacyGroup;
import org.web3j.protocol.besu.response.privacy.PrivFindPrivacyGroup;
import org.web3j.protocol.besu.response.privacy.PrivGetPrivacyPrecompileAddress;
import org.web3j.protocol.besu.response.privacy.PrivGetPrivateTransaction;
import org.web3j.protocol.besu.response.privacy.PrivGetTransactionReceipt;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthUninstallFilter;
import org.web3j.protocol.core.methods.response.MinerStartResponse;
import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Base64String;

public interface Besu extends Eea, BesuRx {

    /**
     * Construct a new Besu instance.
     *
     * @param web3jService web3j service instance - i.e. HTTP
     * @return new Besu instance
     */
    static Besu build(Web3jService web3jService) {
        return new JsonRpc2_0Besu(web3jService);
    }

    /**
     * Construct a new Besu instance.
     *
     * @param web3jService web3j service instance - i.e. HTTP
     * @param pollingInterval polling interval for responses from network nodes
     * @param scheduledExecutorService executor service to use for scheduled tasks. <strong>You are
     *     responsible for terminating this thread pool</strong>
     * @return new Besu instance
     */
    static Besu build(
            Web3jService web3jService,
            long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Besu(web3jService, pollingInterval, scheduledExecutorService);
    }

    Request<?, MinerStartResponse> minerStart();

    Request<?, BooleanResponse> minerStop();

    Request<?, BooleanResponse> cliqueDiscard(String address);

    Request<?, EthAccounts> cliqueGetSigners(DefaultBlockParameter defaultBlockParameter);

    Request<?, EthAccounts> cliqueGetSignersAtHash(String blockHash);

    Request<?, BooleanResponse> cliquePropose(String address, Boolean signerAddition);

    Request<?, BesuEthAccountsMapResponse> cliqueProposals();

    Request<?, BesuFullDebugTraceResponse> debugTraceTransaction(
            String transactionHash, Map<String, Boolean> options);

    Request<?, BooleanResponse> ibftDiscardValidatorVote(String address);

    Request<?, BesuEthAccountsMapResponse> ibftGetPendingVotes();

    Request<?, BesuSignerMetrics> ibftGetSignerMetrics();

    Request<?, EthAccounts> ibftGetValidatorsByBlockNumber(
            DefaultBlockParameter defaultBlockParameter);

    Request<?, EthAccounts> ibftGetValidatorsByBlockHash(String blockHash);

    Request<?, BooleanResponse> ibftProposeValidatorVote(String address, Boolean validatorAddition);

    Request<?, EthGetTransactionCount> privGetTransactionCount(
            final String address, final Base64String privacyGroupId);

    Request<?, PrivGetPrivateTransaction> privGetPrivateTransaction(final String transactionHash);

    Request<?, PrivGetPrivacyPrecompileAddress> privGetPrivacyPrecompileAddress();

    Request<?, PrivCreatePrivacyGroup> privCreatePrivacyGroup(
            final List<Base64String> addresses, final String name, final String description);

    Request<?, EthSendTransaction> privOnChainSetGroupLockState(
            final Base64String privacyGroupId,
            final Credentials credentials,
            final Base64String enclaveKey,
            final Boolean lock)
            throws IOException;

    Request<?, EthSendTransaction> privOnChainAddToPrivacyGroup(
            Base64String privacyGroupId,
            Credentials credentials,
            Base64String enclaveKey,
            List<Base64String> participants)
            throws IOException, TransactionException;

    Request<?, EthSendTransaction> privOnChainCreatePrivacyGroup(
            Base64String privacyGroupId,
            Credentials credentials,
            Base64String enclaveKey,
            List<Base64String> participants)
            throws IOException;

    Request<?, EthSendTransaction> privOnChainRemoveFromPrivacyGroup(
            final Base64String privacyGroupId,
            final Credentials credentials,
            final Base64String enclaveKey,
            final Base64String participant)
            throws IOException;

    Request<?, PrivFindPrivacyGroup> privOnChainFindPrivacyGroup(
            final List<Base64String> addresses);

    Request<?, PrivFindPrivacyGroup> privFindPrivacyGroup(final List<Base64String> addresses);

    Request<?, BooleanResponse> privDeletePrivacyGroup(final Base64String privacyGroupId);

    Request<?, PrivGetTransactionReceipt> privGetTransactionReceipt(final String transactionHash);

    Request<?, EthGetCode> privGetCode(
            String privacyGroupId, String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, org.web3j.protocol.core.methods.response.EthCall> privCall(
            String privacyGroupId,
            org.web3j.protocol.core.methods.request.Transaction transaction,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, EthLog> privGetLogs(
            String privacyGroupId, org.web3j.protocol.core.methods.request.EthFilter ethFilter);

    Request<?, EthFilter> privNewFilter(
            String privacyGroupId, org.web3j.protocol.core.methods.request.EthFilter ethFilter);

    Request<?, EthUninstallFilter> privUninstallFilter(String privacyGroupId, String filterId);

    Request<?, EthLog> privGetFilterChanges(String privacyGroupId, String filterId);

    Request<?, EthLog> privGetFilterLogs(String privacyGroupId, String filterId);
}
