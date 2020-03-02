/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.protocol.core;

import org.junit.jupiter.api.Test;

import org.web3j.protocol.BatchTester;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.Web3Sha3;
import org.web3j.protocol.http.HttpService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BatchTest extends BatchTester {

    private Web3j web3j;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        web3j = Web3j.build(httpService);
    }

    @Test
    public void testBatchRequest() throws Exception {
        web3j.newBatch()
                .add(web3j.web3ClientVersion())
                .add(web3j.web3Sha3("0x68656c6c6f20776f726c64"))
                .add(web3j.netVersion())
                .send();

        verifyResult(
                new String[] {
                    "{\"jsonrpc\":\"2.0\",\"method\":\"web3_clientVersion\",\"params\":[],\"id\":1}",
                    "{\"jsonrpc\":\"2.0\",\"method\":\"web3_sha3\","
                            + "\"params\":[\"0x68656c6c6f20776f726c64\"],\"id\":1}",
                    "{\"jsonrpc\":\"2.0\",\"method\":\"net_version\",\"params\":[],\"id\":1}"
                });
    }

    @Test
    public void testBatchResponse() throws Exception {
        buildResponse(
                "["
                        + "{\n"
                        + "  \"id\":67,\n"
                        + "  \"jsonrpc\":\"2.0\",\n"
                        + "  \"result\": \"Mist/v0.9.3/darwin/go1.4.1\"\n"
                        + "},"
                        + "{\n"
                        + "  \"id\":64,\n"
                        + "  \"jsonrpc\": \"2.0\",\n"
                        + "  \"result\": "
                        + "\"0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad\"\n"
                        + "},"
                        + "{\n"
                        + "  \"id\":67,\n"
                        + "  \"jsonrpc\": \"2.0\",\n"
                        + "  \"result\": \"59\"\n"
                        + "}"
                        + "]");

        BatchResponse response =
                web3j.newBatch()
                        .add(web3j.web3ClientVersion())
                        .add(web3j.web3Sha3("0x68656c6c6f20776f726c64"))
                        .add(web3j.netVersion())
                        .send();

        assertTrue(response.getResponses().get(0) instanceof Web3ClientVersion);
        Web3ClientVersion web3ClientVersion = (Web3ClientVersion) response.getResponses().get(0);
        assertEquals(web3ClientVersion.getWeb3ClientVersion(), "Mist/v0.9.3/darwin/go1.4.1");

        assertTrue(response.getResponses().get(1) instanceof Web3Sha3);
        Web3Sha3 web3Sha3 = (Web3Sha3) response.getResponses().get(1);
        assertEquals(
                web3Sha3.getResult(),
                "0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad");

        assertTrue(response.getResponses().get(2) instanceof NetVersion);
        NetVersion netVersion = (NetVersion) response.getResponses().get(2);
        assertEquals(netVersion.getNetVersion(), "59");
    }
}
