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
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.generated.ComplexStorage;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

@EVMTest(type = NodeType.BESU)
public class ComplexStorageIT {

    @Test
    public void greeterDeploys(
            Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider)
            throws Exception {

        DynamicStruct foo =
                new DynamicStruct(new Utf8String("some_id"), new Utf8String("some_name"));
        DynamicStruct bar = new DynamicStruct(new Utf8String("other"), new Uint(BigInteger.TEN));

        ComplexStorage storage =
                ComplexStorage.deploy(web3j, transactionManager, gasProvider).send();
        storage.setFoo(foo).send();

        DynamicStruct result = storage.getFoo().send();
        System.out.println(result);
        //        Assertions.assertEquals("Hello EVM", greeting);
    }
}
