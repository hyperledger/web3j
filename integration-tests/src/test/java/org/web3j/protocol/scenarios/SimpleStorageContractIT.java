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
package org.web3j.protocol.scenarios;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.protocol.Web3j;
import org.web3j.test.contract.SimpleStorage;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EVMTest(type = NodeType.GETH)
public class SimpleStorageContractIT extends Scenario {

    @BeforeAll
    public static void setUp(Web3j web3j) {
        Scenario.web3j = web3j;
    }

    @Test
    public void testSimpleStorageContract() throws Exception {
        BigInteger value = BigInteger.valueOf(1000L);
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        SimpleStorage simpleStorage =
                SimpleStorage.deploy(web3j, ALICE, contractGasProvider).send();
        assertNotNull(simpleStorage.set(value).send());
        assertEquals(simpleStorage.get().send(), (value));
    }
}
