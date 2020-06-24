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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.web3j.protocol.RequestTester;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Base64String;

public class RequestTest extends RequestTester {

    private static final Base64String MOCK_ENCLAVE_KEY =
            Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
    private static final Base64String MOCK_ENCLAVE_KEY_2 =
            Base64String.wrap("Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=");
    private static final Base64String MOCK_PRIVACY_GROUP_ID =
            Base64String.wrap("DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=");
    private static final List<Base64String> BASE_64_STRINGS =
            Arrays.asList(MOCK_ENCLAVE_KEY, MOCK_ENCLAVE_KEY_2);

    private Besu web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Besu.build(httpService);
    }

    @Test
    public void testMinerStart() throws Exception {
        web3j.minerStart().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"miner_start\"," + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testMinerStop() throws Exception {
        web3j.minerStop().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"miner_stop\"," + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testClicqueDiscard() throws Exception {
        final String accountId = "0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73";
        web3j.cliqueDiscard(accountId).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"clique_discard\","
                        + "\"params\":[\"0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73\"],\"id\":1}");
    }

    @Test
    public void testClicqueGetSigners() throws Exception {
        final DefaultBlockParameter blockParameter = DefaultBlockParameter.valueOf("latest");
        web3j.cliqueGetSigners(blockParameter).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"clique_getSigners\","
                        + "\"params\":[\"latest\"],\"id\":1}");
    }

    @Test
    public void testClicqueGetSignersAtHash() throws Exception {
        final String blockHash =
                "0x98b2ddb5106b03649d2d337d42154702796438b3c74fd25a5782940e84237a48";
        web3j.cliqueGetSignersAtHash(blockHash).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"clique_getSignersAtHash\",\"params\":"
                        + "[\"0x98b2ddb5106b03649d2d337d42154702796438b3c74fd25a5782940e84237a48\"]"
                        + ",\"id\":1}");
    }

    @Test
    public void testClicquePropose() throws Exception {
        final String signerAddress = "0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73";
        web3j.cliquePropose(signerAddress, true).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"clique_propose\","
                        + "\"params\":[\"0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73\",true],\"id\":1}");
    }

    @Test
    public void testClicqueProposals() throws Exception {
        web3j.cliqueProposals().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"clique_proposals\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testDebugTraceTransaction() throws Exception {
        final String transactionHash = "0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e";

        Map<String, Boolean> options = new HashMap<>();
        options.put("disableStorage", false);
        options.put("disableStack", false);
        options.put("disableMemory", true);

        web3j.debugTraceTransaction(transactionHash, options).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"debug_traceTransaction\","
                        + "\"params\":[\"0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e\","
                        + "{\"disableMemory\":true,"
                        + "\"disableStorage\":false,"
                        + "\"disableStack\":false}],"
                        + "\"id\":1}");
    }

    @Test
    public void testibftDiscardValidatorVote() throws Exception {
        final String accountId = "0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73";
        web3j.ibftDiscardValidatorVote(accountId).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"ibft_discardValidatorVote\","
                        + "\"params\":[\"0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73\"],\"id\":1}");
    }

    @Test
    public void testIbftGetPendingVotes() throws Exception {
        web3j.ibftGetPendingVotes().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"ibft_getPendingVotes\","
                        + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testIbftGetValidatorsByBlockNumber() throws Exception {
        final DefaultBlockParameter blockParameter = DefaultBlockParameter.valueOf("latest");
        web3j.ibftGetValidatorsByBlockNumber(blockParameter).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"ibft_getValidatorsByBlockNumber\","
                        + "\"params\":[\"latest\"],\"id\":1}");
    }

    @Test
    public void testIbftGetValidatorsByBlockHash() throws Exception {
        final String blockHash =
                "0x98b2ddb5106b03649d2d337d42154702796438b3c74fd25a5782940e84237a48";
        web3j.ibftGetValidatorsByBlockHash(blockHash).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"ibft_getValidatorsByBlockHash\",\"params\":"
                        + "[\"0x98b2ddb5106b03649d2d337d42154702796438b3c74fd25a5782940e84237a48\"]"
                        + ",\"id\":1}");
    }

    @Test
    public void testIbftProposeValidatorVote() throws Exception {
        final String validatorAddress = "0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73";
        web3j.ibftProposeValidatorVote(validatorAddress, true).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"ibft_proposeValidatorVote\","
                        + "\"params\":[\"0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73\",true],\"id\":1}");
    }

    @Test
    public void testPrivGetTransactionCount() throws Exception {
        web3j.privGetTransactionCount(
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1", MOCK_PRIVACY_GROUP_ID)
                .send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_getTransactionCount\","
                        + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\"],"
                        + "\"id\":1}");
    }

    @Test
    public void testPrivDistributeRawTransaction() throws Exception {
        final String rawTx =
                "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f"
                        + "072445675058bb8eb970870f072445675";
        web3j.privDistributeRawTransaction(rawTx).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_distributeRawTransaction\","
                        + "\"params\":[\"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"],\"id\":1}");
    }

    @Test
    public void testPrivGetPrivateTransaction() throws Exception {
        web3j.privGetPrivateTransaction("EnclaveKey").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_getPrivateTransaction\","
                        + "\"params\":[\"EnclaveKey\"],"
                        + "\"id\":1}");
    }

    @Test
    public void testPrivGetPrivacyPrecompileAddress() throws Exception {
        web3j.privGetPrivacyPrecompileAddress().send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_getPrivacyPrecompileAddress\","
                        + "\"params\":[],"
                        + "\"id\":1}");
    }

    @Test
    public void testPrivCreatePrivacyGroup() throws Exception {
        web3j.privCreatePrivacyGroup(BASE_64_STRINGS, "testName", "testDesc").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_createPrivacyGroup\","
                        + "\"params\":[{\"addresses\":[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=\"],\"name\":\"testName\",\"description\":\"testDesc\"}],"
                        + "\"id\":1}");
    }

    @Test
    public void testPrivFindPrivacyGroup() throws Exception {
        web3j.privFindPrivacyGroup(BASE_64_STRINGS).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_findPrivacyGroup\","
                        + "\"params\":[[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=\"]],"
                        + "\"id\":1}");
    }

    @Test
    public void testPrivDeletePrivacyGroup() throws Exception {
        web3j.privDeletePrivacyGroup(MOCK_PRIVACY_GROUP_ID).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_deletePrivacyGroup\","
                        + "\"params\":[\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\"],"
                        + "\"id\":1}");
    }

    @Test
    public void testPrivGetTransactionReceipt() throws Exception {
        web3j.privGetTransactionReceipt("0x123").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_getTransactionReceipt\","
                        + "\"params\":[\"0x123\"],\"id\":1}");
    }

    @Test
    public void testPrivGetCode() throws Exception {
        web3j.privGetCode(
                        MOCK_PRIVACY_GROUP_ID.toString(),
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        DefaultBlockParameterName.LATEST)
                .send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_getCode\","
                        + "\"params\":[\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\",\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"latest\"],\"id\":1}");
    }

    @Test
    public void testPrivCall() throws Exception {
        web3j.privCall(
                        MOCK_PRIVACY_GROUP_ID.toString(),
                        Transaction.createEthCallTransaction(
                                "0xa70e8dd61c5d32be8058bb8eb970870f07233155",
                                "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                                "0x0"),
                        DefaultBlockParameter.valueOf("latest"))
                .send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_call\","
                        + "\"params\":[\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\",{\"from\":\"0xa70e8dd61c5d32be8058bb8eb970870f07233155\","
                        + "\"to\":\"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\"data\":\"0x0\"},"
                        + "\"latest\"],\"id\":1}");
    }

    @Test
    public void testPrivGetLogs() throws Exception {
        web3j.privGetLogs(
                        MOCK_PRIVACY_GROUP_ID.toString(),
                        new EthFilter()
                                .addSingleTopic(
                                        "0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b"))
                .send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_getLogs\","
                        + "\"params\":[\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\""
                        + ",{\"topics\":[\"0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b\"]}],"
                        + "\"id\":1}");
    }

    @Test
    public void testPrivNewFilter() throws Exception {
        EthFilter ethFilter = new EthFilter().addSingleTopic("0x12341234");

        web3j.privNewFilter(MOCK_PRIVACY_GROUP_ID.toString(), ethFilter).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_newFilter\","
                        + "\"params\":[\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\",{\"topics\":[\"0x12341234\"]}],\"id\":1}");
    }

    @Test
    public void testPrivUninstallFilter() throws Exception {
        web3j.privUninstallFilter(
                        MOCK_PRIVACY_GROUP_ID.toString(), "0x13e9b67497fa859338ecba166752591b")
                .send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_uninstallFilter\","
                        + "\"params\":[\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\",\"0x13e9b67497fa859338ecba166752591b\"],\"id\":1}");
    }

    @Test
    public void testPrivGetFilterChanges() throws Exception {
        web3j.privGetFilterChanges(
                        MOCK_PRIVACY_GROUP_ID.toString(), "0x13e9b67497fa859338ecba166752591b")
                .send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_getFilterChanges\","
                        + "\"params\":[\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\",\"0x13e9b67497fa859338ecba166752591b\"],\"id\":1}");
    }

    @Test
    public void testPrivGetFilterLogs() throws Exception {
        web3j.privGetFilterLogs(
                        MOCK_PRIVACY_GROUP_ID.toString(), "0x13e9b67497fa859338ecba166752591b")
                .send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"priv_getFilterLogs\","
                        + "\"params\":[\"DyAOiF/ynpc+JXa2YAGB0bCitSlOMNm+ShmB/7M6C4w=\",\"0x13e9b67497fa859338ecba166752591b\"],\"id\":1}");
    }
}
