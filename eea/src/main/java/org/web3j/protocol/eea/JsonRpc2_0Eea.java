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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.response.EeaCreatePrivacyGroup;
import org.web3j.protocol.eea.response.EeaGetPrivacyPrecompileAddress;
import org.web3j.protocol.eea.response.EeaGetTransactionReceipt;
import org.web3j.protocol.eea.response.EeaPrivateTransaction;

public class JsonRpc2_0Eea extends JsonRpc2_0Web3j implements Eea {
    public JsonRpc2_0Eea(Web3jService web3jService) {
        super(web3jService);
    }

    @Override
    public Request<?, EthSendTransaction> eeaSendRawTransaction(
            final String signedTransactionData) {
        return new Request<>(
                "eea_sendRawTransaction",
                Collections.singletonList(signedTransactionData),
                web3jService,
                EthSendTransaction.class);
    }

    @Override
    public Request<?, EeaGetTransactionReceipt> eeaGetTransactionReceipt(
            final String transactionHash) {
        return new Request<>(
                "eea_getTransactionReceipt",
                Collections.singletonList(transactionHash),
                web3jService,
                EeaGetTransactionReceipt.class);
    }

    @Override
    public Request<?, EthGetTransactionCount> eeaGetTransactionCount(
            final String address, final String privacyGroupId) {
        return new Request<>(
                "eea_getTransactionCount",
                Arrays.asList(address, privacyGroupId),
                web3jService,
                EthGetTransactionCount.class);
    }

    @Override
    public Request<?, EeaPrivateTransaction> eeaGetPrivateTransaction(
            final String enclaveKey) {
        return new Request<>(
                "eea_getPrivateTransaction",
                Collections.singletonList(enclaveKey),
                web3jService,
                EeaPrivateTransaction.class);
    }

    @Override
    public Request<?, EeaGetPrivacyPrecompileAddress> eeaGetPrivacyPrecompileAddress() {
        return new Request<>(
                "eea_getPrivacyPrecompileAddress",
                Collections.emptyList(),
                web3jService,
                EeaGetPrivacyPrecompileAddress.class);
    }

    @Override
    public Request<?, EeaCreatePrivacyGroup> eeaCreatePrivacyGroup(
            final String from,
            final String name,
            final String description,
            final List<String> addresses) {
        return new Request<>(
                "eea_createPrivacyGroup",
                Arrays.asList(from, name, description, addresses),
                web3jService,
                EeaCreatePrivacyGroup.class);
    }

    @Override
    public Request<?, BooleanResponse> eeaDeletePrivacyGroup(
            final String from,
            final String privacyGroupId) {
        return new Request<>(
                "eea_deletePrivacyGroup",
                Arrays.asList(from, privacyGroupId),
                web3jService,
                BooleanResponse.class);
    }
}
