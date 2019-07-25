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
import java.util.Optional;

import org.junit.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.eea.response.EeaCreatePrivacyGroup;
import org.web3j.protocol.eea.response.EeaFindPrivacyGroup;
import org.web3j.protocol.eea.response.EeaGetPrivacyPrecompileAddress;
import org.web3j.protocol.eea.response.EeaGetPrivateTransaction;
import org.web3j.protocol.eea.response.EeaGetTransactionReceipt;
import org.web3j.protocol.eea.response.PrivacyGroup;
import org.web3j.protocol.eea.response.PrivateTransactionLegacy;
import org.web3j.protocol.eea.response.PrivateTransactionReceipt;
import org.web3j.protocol.eea.response.PrivateTransactionWithPrivacyGroup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResponseTest extends ResponseTester {

    @Test
    public void testEeaSendRawTransaction() {

        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\"\n"
                        + "}");

        EthSendTransaction ethSendTransaction = deserialiseResponse(EthSendTransaction.class);
        assertThat(
                ethSendTransaction.getTransactionHash(),
                is("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331"));
    }

    @Test
    public void testEeaGetTransactionReceipt() {

        buildResponse(
                "{\n"
                        + "    \"id\":1,\n"
                        + "    \"jsonrpc\":\"2.0\",\n"
                        + "    \"result\": {\n"
                        + "        \"contractAddress\": \"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\n"
                        + "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"output\":\"myRlpEncodedOutputFromPrivateContract\",\n"
                        + "        \"logs\": [{\n"
                        + "            \"removed\": false,\n"
                        + "            \"logIndex\": \"0x1\",\n"
                        + "            \"transactionIndex\": \"0x0\",\n"
                        + "            \"transactionHash\": \"0xdf829c5a142f1fccd7d8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcf\",\n"
                        + "            \"blockHash\": \"0x8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcfdf829c5a142f1fccd7d\",\n"
                        + "            \"blockNumber\":\"0x1b4\",\n"
                        + "            \"address\": \"0x16c5785ac562ff41e2dcfdf829c5a142f1fccd7d\",\n"
                        + "            \"data\":\"0x0000000000000000000000000000000000000000000000000000000000000000\",\n"
                        + "            \"type\":\"mined\",\n"
                        + "            \"topics\": [\"0x59ebeb90bc63057b6515673c3ecf9438e5058bca0f92585014eced636878c9a5\"]"
                        + "        }]\n"
                        + "    }\n"
                        + "}");

        PrivateTransactionReceipt transactionReceipt =
                new PrivateTransactionReceipt(
                        "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                        "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                        "myRlpEncodedOutputFromPrivateContract",
                        Collections.singletonList(
                                new Log(
                                        false,
                                        "0x1",
                                        "0x0",
                                        "0xdf829c5a142f1fccd7d8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcf",
                                        "0x8216c5785ac562ff41e2dcfdf5785ac562ff41e2dcfdf829c5a142f1fccd7d",
                                        "0x1b4",
                                        "0x16c5785ac562ff41e2dcfdf829c5a142f1fccd7d",
                                        "0x0000000000000000000000000000000000000000000000000000000000000000",
                                        "mined",
                                        Collections.singletonList(
                                                "0x59ebeb90bc63057b6515673c3ecf9438e5058bca0f92585014eced636878c9a5"))));

        EeaGetTransactionReceipt eeaGetTransactionReceipt =
                deserialiseResponse(EeaGetTransactionReceipt.class);
        assertThat(
                eeaGetTransactionReceipt.getTransactionReceipt().get(),
                equalTo(transactionReceipt));
    }

    @Test
    public void testEeaGetPrivateTransactionLegacy() {
        // CHECKSTYLE:OFF
        buildResponse(
                "{\n"
                        + "    \"id\":1,\n"
                        + "    \"jsonrpc\":\"2.0\",\n"
                        + "    \"result\": {\n"
                        + "        \"hash\":\"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\n"
                        + "        \"nonce\":\"0x\",\n"
                        + "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"value\":\"0x7f110\",\n"
                        + "        \"gas\": \"0x7f110\",\n"
                        + "        \"gasPrice\":\"0x09184e72a000\",\n"
                        + "        \"input\":\"0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360\",\n"
                        + "        \"r\":\"0xf115cc4d7516dd430046504e1c888198e0323e8ded016d755f89c226ba3481dc\",\n"
                        + "        \"s\":\"0x4a2ae8ee49f1100b5c0202b37ed8bacf4caeddebde6b7f77e12e7a55893e9f62\",\n"
                        + "        \"v\":0,\n"
                        + "        \"privateFrom\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "        \"privateFor\":[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=\"],\n"
                        + "        \"restriction\":\"restricted\""
                        + "  }\n"
                        + "}");
        PrivateTransactionLegacy privateTransaction =
                new PrivateTransactionLegacy(
                        "0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
                        "0x",
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                        "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                        "0x7f110",
                        "0x7f110",
                        "0x09184e72a000",
                        "0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360",
                        "0xf115cc4d7516dd430046504e1c888198e0323e8ded016d755f89c226ba3481dc",
                        "0x4a2ae8ee49f1100b5c0202b37ed8bacf4caeddebde6b7f77e12e7a55893e9f62",
                        (byte) 0,
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        Arrays.asList(
                                "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                                "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs="),
                        "restricted");
        // CHECKSTYLE:ON

        EeaGetPrivateTransaction eeaPrivateTransaction =
                deserialiseResponse(EeaGetPrivateTransaction.class);
        assertThat(
                eeaPrivateTransaction.getPrivateTransaction().get(), equalTo(privateTransaction));
    }

    @Test
    public void testEeaGetPrivateTransactionPrivacyGroup() {
        // CHECKSTYLE:OFF
        buildResponse(
                "{\n"
                        + "    \"id\":1,\n"
                        + "    \"jsonrpc\":\"2.0\",\n"
                        + "    \"result\": {\n"
                        + "        \"hash\":\"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\n"
                        + "        \"nonce\":\"0x\",\n"
                        + "        \"from\":\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"to\":\"0x85h43d8a49eeb85d32cf465507dd71d507100c1\",\n"
                        + "        \"value\":\"0x7f110\",\n"
                        + "        \"gas\": \"0x7f110\",\n"
                        + "        \"gasPrice\":\"0x09184e72a000\",\n"
                        + "        \"input\":\"0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360\",\n"
                        + "        \"r\":\"0xf115cc4d7516dd430046504e1c888198e0323e8ded016d755f89c226ba3481dc\",\n"
                        + "        \"s\":\"0x4a2ae8ee49f1100b5c0202b37ed8bacf4caeddebde6b7f77e12e7a55893e9f62\",\n"
                        + "        \"v\":0,\n"
                        + "        \"privateFrom\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "        \"privacyGroupId\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "        \"restriction\":\"restricted\""
                        + "  }\n"
                        + "}");
        PrivateTransactionWithPrivacyGroup privateTransaction =
                new PrivateTransactionWithPrivacyGroup(
                        "0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
                        "0x",
                        "0x407d73d8a49eeb85d32cf465507dd71d507100c1",
                        "0x85h43d8a49eeb85d32cf465507dd71d507100c1",
                        "0x7f110",
                        "0x7f110",
                        "0x09184e72a000",
                        "0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360",
                        "0xf115cc4d7516dd430046504e1c888198e0323e8ded016d755f89c226ba3481dc",
                        "0x4a2ae8ee49f1100b5c0202b37ed8bacf4caeddebde6b7f77e12e7a55893e9f62",
                        (byte) 0,
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        "restricted");
        // CHECKSTYLE:ON

        EeaGetPrivateTransaction eeaPrivateTransaction =
                deserialiseResponse(EeaGetPrivateTransaction.class);
        assertThat(
                eeaPrivateTransaction.getPrivateTransaction().get(), equalTo(privateTransaction));
    }

    @Test
    public void testEeaGetPrivateTransactionNull() {
        buildResponse("{\n" + "  \"result\": null\n" + "}");

        EeaGetPrivateTransaction eeaPrivateTransaction =
                deserialiseResponse(EeaGetPrivateTransaction.class);
        assertThat(eeaPrivateTransaction.getPrivateTransaction(), is(Optional.empty()));
    }

    @Test
    public void testEeaGetPrivacyPrecompileAddress() {
        // CHECKSTYLE:OFF
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"0xb60e8dd61c5d32be8058bb8eb970870f07233155\"\n"
                        + "}");
        // CHECKSTYLE:ON

        EeaGetPrivacyPrecompileAddress eeaGetPrivacyPrecompileAddress =
                deserialiseResponse(EeaGetPrivacyPrecompileAddress.class);
        assertThat(
                eeaGetPrivacyPrecompileAddress.getAddress(),
                is("0xb60e8dd61c5d32be8058bb8eb970870f07233155"));
    }

    @Test
    public void testEeaCreatePrivacyGroup() {
        // CHECKSTYLE:OFF
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"68/Cq0mVjB8FbXDLE1tbDRAvD/srluIok137uFOaClPU/dLFW34ovZebW+PTzy9wUawTXw==\"\n"
                        + "}");
        // CHECKSTYLE:ON

        EeaCreatePrivacyGroup eeaCreatePrivacyGroup =
                deserialiseResponse(EeaCreatePrivacyGroup.class);
        assertThat(
                eeaCreatePrivacyGroup.getPrivacyGroupId(),
                is("68/Cq0mVjB8FbXDLE1tbDRAvD/srluIok137uFOaClPU/dLFW34ovZebW+PTzy9wUawTXw=="));
    }

    @Test
    public void testEeaDeletePrivacyGroup() {
        // CHECKSTYLE:OFF
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": \"true\"\n"
                        + "}");
        // CHECKSTYLE:ON

        BooleanResponse eeaDeletePrivacyGroup = deserialiseResponse(BooleanResponse.class);
        assertThat(eeaDeletePrivacyGroup.success(), is(true));
    }

    @Test
    public void testEeaFindPrivacyGroup() {
        // CHECKSTYLE:OFF
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": [\n"
                        + "         {\n"
                        + "            \"privacyGroupId\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "            \"name\":\"PrivacyGroupName\",\n"
                        + "            \"description\":\"PrivacyGroupDescription\",\n"
                        + "            \"type\":\"LEGACY\",\n"
                        + "            \"members\": [\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\"]\n"
                        + "         },\n"
                        + "         {\n"
                        + "            \"privacyGroupId\":\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\n"
                        + "            \"name\":\"PrivacyGroupName\",\n"
                        + "            \"description\":\"PrivacyGroupDescription\",\n"
                        + "            \"type\":\"PANTHEON\",\n"
                        + "            \"members\": [\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\"]\n"
                        + "         }\n"
                        + "    ]\n"
                        + "}");
        // CHECKSTYLE:ON
        PrivacyGroup privacyGroup1 =
                new PrivacyGroup(
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        PrivacyGroup.Type.LEGACY,
                        "PrivacyGroupName",
                        "PrivacyGroupDescription",
                        Collections.singletonList("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="));
        PrivacyGroup privacyGroup2 =
                new PrivacyGroup(
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        PrivacyGroup.Type.PANTHEON,
                        "PrivacyGroupName",
                        "PrivacyGroupDescription",
                        Collections.singletonList("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="));

        EeaFindPrivacyGroup eeaFindPrivacyGroup = deserialiseResponse(EeaFindPrivacyGroup.class);
        assertThat(
                eeaFindPrivacyGroup.getGroups(), is(Arrays.asList(privacyGroup1, privacyGroup2)));
    }
}
