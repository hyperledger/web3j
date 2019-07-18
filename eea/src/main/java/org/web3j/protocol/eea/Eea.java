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
package org.web3j.protocol.eea;

import java.util.List;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.response.EeaCreatePrivacyGroup;
import org.web3j.protocol.eea.response.EeaGetPrivacyPrecompileAddress;
import org.web3j.protocol.eea.response.EeaGetTransactionReceipt;
import org.web3j.protocol.eea.response.EeaGetPrivateTransaction;


public interface Eea extends Web3j {
    static Eea build(Web3jService web3jService) {
        return new JsonRpc2_0Eea(web3jService);
    }

    Request<?, EthSendTransaction> eeaSendRawTransaction(final String signedTransactionData);

    Request<?, EeaGetTransactionReceipt> eeaGetTransactionReceipt(final String transactionHash);

    Request<?, EthGetTransactionCount> eeaGetTransactionCount(
            final String address, final String privacyGroupId);

    Request<?, EeaGetPrivateTransaction> eeaGetPrivateTransaction(
            final String enclaveKey);

    Request<?, EeaGetPrivacyPrecompileAddress> eeaGetPrivacyPrecompileAddress();

    Request<?, EeaCreatePrivacyGroup> eeaCreatePrivacyGroup(
            final String from, final String name,
            final String description, final List<String> addresses);

    Request<?, BooleanResponse> eeaDeletePrivacyGroup(
            final String from, final String privacyGroupId);
}
