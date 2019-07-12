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

import org.junit.Test;

import org.web3j.protocol.RequestTester;
import org.web3j.protocol.http.HttpService;

public class RequestTest extends RequestTester {
    private Eea web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Eea.build(httpService);
    }

    @Test
    public void testEthSendRawTransaction() throws Exception {
        web3j.eeaSendRawTransaction(
                        "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f"
                                + "072445675058bb8eb970870f072445675")
                .send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_sendRawTransaction\",\"params\":[\"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"],\"id\":1}");
    }

    @Test
    public void testEeaGetTransactionReceipt() throws Exception {
        web3j.eeaGetTransactionReceipt("0x123").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_getTransactionReceipt\","
                        + "\"params\":[\"0x123\"],\"id\":1}");
    }

    @Test
    public void testEeaGetTransactionCount() throws Exception {
        web3j.eeaGetTransactionCount("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "0x0").send();

        verifyResult(
                "{\"jsonrpc\":\"2.0\",\"method\":\"eea_getTransactionCount\","
                        + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"0x0\"],"
                        + "\"id\":1}");
    }

    @Test
    public void testEeaGetPrivateTransaction() throws Exception {
        web3j.eeaGetPrivateTransaction("EnclaveKey")
                .send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eea_getPrivateTransaction\","
                + "\"params\":[\"EnclaveKey\"],"
                + "\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testEeaGetPrivacyPrecompileAddress() throws Exception {
        web3j.eeaGetPrivacyPrecompileAddress()
                .send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eea_getPrivacyPrecompileAddress\","
                + "\"params\":[],"
                + "\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testEeaCreatePrivacyGroup() throws Exception {
        web3j.eeaCreatePrivacyGroup(
                "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                "testName",
                "testDesc",
                Arrays.asList(
                        "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                        "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs="))
                .send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eea_createPrivacyGroup\","
                + "\"params\":[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"testName\",\"testDesc\",[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs=\"]],"
                + "\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testEeaDeletePrivacyGroup() throws Exception {
        web3j.eeaDeletePrivacyGroup(
                "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=",
                "68/Cq0mVjB8FbXDLE1tbDRAvD/srluIok137uFOaClPU/dLFW34ovZebW+PTzy9wUawTXw==")
                .send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"eea_deletePrivacyGroup\","
                + "\"params\":[\"A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=\",\"68/Cq0mVjB8FbXDLE1tbDRAvD/srluIok137uFOaClPU/dLFW34ovZebW+PTzy9wUawTXw==\"],"
                + "\"id\":1}");
        //CHECKSTYLE:ON
    }

}
