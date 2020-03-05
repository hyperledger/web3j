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
package org.web3j.protocol.scenarios;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.generated.ComplexStorage;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@EVMTest(type = NodeType.BESU)
public class ComplexStorageIT {

    @Test
    public void greeterDeploys(
            Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider)
            throws Exception {

        final ComplexStorage.TupleClass1 foo = new ComplexStorage.TupleClass1("Sam", "Na");
        final ComplexStorage.TupleClass2 bar =
                new ComplexStorage.TupleClass2("First", BigInteger.valueOf(100L));

        final ComplexStorage complexStorage =
                ComplexStorage.deploy(web3j, transactionManager, gasProvider).send();

        final TransactionReceipt deployTransactionReceipt = complexStorage.setFoo(foo).send();
        assertNotNull(deployTransactionReceipt);

        // Now test the decoder.
        final TransactionReceipt send = complexStorage.getFooBar().send();

        //        Assertions.assertEquals("Hello EVM", greeting);
    }
}
