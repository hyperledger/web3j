/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.protocol.pantheon;

import java.util.List;
import java.util.Map;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.MinerStartResponse;
import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.pantheon.response.PantheonEthAccountsMapResponse;
import org.web3j.protocol.pantheon.response.PantheonFullDebugTraceResponse;
import org.web3j.protocol.pantheon.response.privacy.PrivCreatePrivacyGroup;
import org.web3j.protocol.pantheon.response.privacy.PrivFindPrivacyGroup;
import org.web3j.protocol.pantheon.response.privacy.PrivGetPrivacyPrecompileAddress;
import org.web3j.protocol.pantheon.response.privacy.PrivGetPrivateTransaction;
import org.web3j.protocol.pantheon.response.privacy.PrivGetTransactionReceipt;
import org.web3j.utils.Base64String;

public interface Pantheon extends Eea {
    static Pantheon build(Web3jService web3jService) {
        return new JsonRpc2_0Pantheon(web3jService);
    }

    Request<?, MinerStartResponse> minerStart();

    Request<?, BooleanResponse> minerStop();

    /** @deprecated This is deprecated as the method name is wrong. */
    default Request<?, BooleanResponse> clicqueDiscard(String address) {
        return cliqueDiscard(address);
    }

    /** @deprecated This is deprecated as the method name is wrong. */
    default Request<?, EthAccounts> clicqueGetSigners(DefaultBlockParameter defaultBlockParameter) {
        return cliqueGetSigners(defaultBlockParameter);
    }

    /** @deprecated This is deprecated as the method name is wrong. */
    default Request<?, EthAccounts> clicqueGetSignersAtHash(String blockHash) {
        return cliqueGetSignersAtHash(blockHash);
    }

    Request<?, BooleanResponse> cliqueDiscard(String address);

    Request<?, EthAccounts> cliqueGetSigners(DefaultBlockParameter defaultBlockParameter);

    Request<?, EthAccounts> cliqueGetSignersAtHash(String blockHash);

    Request<?, BooleanResponse> cliquePropose(String address, Boolean signerAddition);

    Request<?, PantheonEthAccountsMapResponse> cliqueProposals();

    Request<?, PantheonFullDebugTraceResponse> debugTraceTransaction(
            String transactionHash, Map<String, Boolean> options);

    Request<?, EthGetTransactionCount> privGetTransactionCount(
            final String address, final Base64String privacyGroupId);

    Request<?, PrivGetPrivateTransaction> privGetPrivateTransaction(final String transactionHash);

    Request<?, PrivGetPrivacyPrecompileAddress> privGetPrivacyPrecompileAddress();

    Request<?, PrivCreatePrivacyGroup> privCreatePrivacyGroup(
            final List<Base64String> addresses, final String name, final String description);

    Request<?, PrivFindPrivacyGroup> privFindPrivacyGroup(final List<Base64String> addresses);

    Request<?, BooleanResponse> privDeletePrivacyGroup(final Base64String privacyGroupId);

    Request<?, PrivGetTransactionReceipt> privGetTransactionReceipt(final String transactionHash);
}
