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
package org.web3j.protocol.eea;

import org.junit.jupiter.api.Test;

import org.web3j.protocol.RequestTester;
import org.web3j.protocol.http.HttpService;

public class RequestTest extends RequestTester {
    private Eea web3j;

    @Override
    protected void initWeb3Client(final HttpService httpService) {
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
}
