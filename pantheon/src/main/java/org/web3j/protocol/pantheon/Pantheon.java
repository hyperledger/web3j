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

import java.util.Map;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.MinerStartResponse;
import org.web3j.protocol.pantheon.response.PantheonEthAccountsMapResponse;
import org.web3j.protocol.pantheon.response.PantheonFullDebugTraceResponse;

public interface Pantheon extends Web3j {
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
}
