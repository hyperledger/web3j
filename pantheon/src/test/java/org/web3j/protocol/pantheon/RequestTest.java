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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.web3j.protocol.RequestTester;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.http.HttpService;

public class RequestTest extends RequestTester {
    private Pantheon web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Pantheon.build(httpService);
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
        web3j.clicqueDiscard(accountId).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"clique_discard\","
                        + "\"params\":[\"0xFE3B557E8Fb62b89F4916B721be55cEb828dBd73\"],\"id\":1}");
    }

    @Test
    public void testClicqueGetSigners() throws Exception {
        final DefaultBlockParameter blockParameter = DefaultBlockParameter.valueOf("latest");
        web3j.clicqueGetSigners(blockParameter).send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"clique_getSigners\","
                        + "\"params\":[\"latest\"],\"id\":1}");
    }

    @Test
    public void testClicqueGetSignersAtHash() throws Exception {
        final String blockHash =
                "0x98b2ddb5106b03649d2d337d42154702796438b3c74fd25a5782940e84237a48";
        web3j.clicqueGetSignersAtHash(blockHash).send();

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
    public void testEeaGetTransactionCount() throws Exception {
        web3j.privGetTransactionCount("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "0x0").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_getTransactionCount\","
                        + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"0x0\"],"
                        + "\"id\":1}");
    }

    @Test
    public void testEeaGetPrivateTransaction() throws Exception {
        web3j.privGetPrivateTransaction("EnclaveKey").send();

        // CHECKSTYLE:OFF
        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_getPrivateTransaction\","
                        + "\"params\":[\"EnclaveKey\"],"
                        + "\"id\":1}");
        // CHECKSTYLE:ON
    }

    @Test
    public void testEeaGetPrivacyPrecompileAddress() throws Exception {
        web3j.privGetPrivacyPrecompileAddress().send();

        // CHECKSTYLE:OFF
        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_getPrivacyPrecompileAddress\","
                        + "\"params\":[],"
                        + "\"id\":1}");
        // CHECKSTYLE:ON
    }

    @Test
    public void testEeaCreatePrivacyGroup() throws Exception {
        web3j.privCreatePrivacyGroup(
                        Arrays.asList(
                                "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                                "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs="),
                        "testName",
                        "testDesc")
                .send();

        // CHECKSTYLE:OFF
        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_createPrivacyGroup\","
                        + "\"params\":[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"testName\",\"testDesc\",[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=\"]],"
                        + "\"id\":1}");
        // CHECKSTYLE:ON
    }

    @Test
    public void testEeaFindPrivacyGroup() throws Exception {
        web3j.privFindPrivacyGroup(
                        Arrays.asList(
                                "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                                "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="))
                .send();

        // CHECKSTYLE:OFF
        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_findPrivacyGroup\","
                        + "\"params\":[[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\"]],"
                        + "\"id\":1}");
        // CHECKSTYLE:ON
    }

    @Test
    public void testEeaDeletePrivacyGroup() throws Exception {
        web3j.privDeletePrivacyGroup(
                        "68/Cq0mVjB8FbXDLE1tbDRAvD/srluIok137uFOaClPU/dLFW34ovZebW+PTzy9wUawTXw==")
                .send();

        // CHECKSTYLE:OFF
        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_deletePrivacyGroup\","
                        + "\"params\":[\"68/Cq0mVjB8FbXDLE1tbDRAvD/srluIok137uFOaClPU/dLFW34ovZebW+PTzy9wUawTXw==\"],"
                        + "\"id\":1}");
        // CHECKSTYLE:ON
    }
}
