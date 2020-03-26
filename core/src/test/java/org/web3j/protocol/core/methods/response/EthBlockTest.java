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
package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EthBlockTest {

    @Test
    public void testEthBlockNullSize() {
        final EthBlock.Block ethBlock =
                new EthBlock.Block(
                        null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null, null);

        assertEquals(ethBlock.getSize(), BigInteger.ZERO);
    }

    @Test
    public void testEthBlockNotNullSize() {
        final EthBlock.Block ethBlock =
                new EthBlock.Block(
                        null, null, null, null, null, null, null, null, null, null, null, null,
                        null, null, null, "0x3e8", null, null, null, null, null, null, null);

        assertEquals(ethBlock.getSize(), BigInteger.valueOf(1000));
    }

    @Test
    public void testGetTransactionByHash() throws Exception {
        String nodeUrl =
                System.getenv()
                        .getOrDefault(
                                "WEB3J_NODE_URL",
                                "https://ropsten.infura.io/v3/5501769a6f86457faadb55a129f5cbae");
        Credentials credentials =
                Credentials.create(
                        "8bf15e7802fe7ab126ee5dea38f3c13fa40cf09d02a233448a7601850e6ef060");
        Web3j web3j = Web3j.build(new HttpService(nodeUrl));
        EthTransaction transaction =
                web3j.ethGetTransactionByHash(
                                "0xcd48e1dea9c9a17b66e938120348eeb8fa53e18100543a49fa7553ed20bd55b2")
                        .send();
        System.out.println(transaction.getTransaction().get().getChainId());
        System.out.println(transaction.getTransaction().get().getAccessList().size());
    }
}
