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

import org.com.test.contract.Fibonacci;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Fibonacci contract generated wrappers.
 *
 * <p>Generated via running org.web3j.codegen.SolidityFunctionWrapperGenerator with params:
 * <em>project-home</em>/src/test/resources/solidity/fibonacci.abi -o
 * <em>project-home</em>/src/integration-test/java -p org.web3j.generated
 */
@EVMTest(type = NodeType.GETH)
public class FunctionWrappersIT extends Scenario {

    private static Fibonacci fib;

    @BeforeAll
    public static void setUp(Web3j web3j, ContractGasProvider contractGasProvider)
            throws Exception {
        Scenario.web3j = web3j;
        FunctionWrappersIT.fib = Fibonacci.deploy(web3j, ALICE, contractGasProvider).send();
    }

    @Test
    public void testFibonacci() throws Exception {

        Fibonacci fibonacci =
                Fibonacci.load(
                        fib.getContractAddress(),
                        Web3j.build(new HttpService()),
                        ALICE,
                        STATIC_GAS_PROVIDER);

        BigInteger result = fibonacci.fibonacci(BigInteger.valueOf(10)).send();
        assertEquals(result, (BigInteger.valueOf(55)));
    }

    @Test
    public void testFibonacciNotify() throws Exception {
        Fibonacci fibonacci =
                Fibonacci.load(
                        fib.getContractAddress(),
                        Web3j.build(new HttpService()),
                        ALICE,
                        STATIC_GAS_PROVIDER);

        TransactionReceipt transactionReceipt =
                fibonacci.fibonacciNotify(BigInteger.valueOf(15)).send();

        Fibonacci.NotifyEventResponse result = fibonacci.getNotifyEvents(transactionReceipt).get(0);

        assertEquals(BigInteger.valueOf(15), result.input);

        assertEquals(BigInteger.valueOf(610), result.result);
    }
}
