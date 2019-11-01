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

import org.junit.jupiter.api.Test;

import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.generated.Fibonacci;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test Fibonacci contract generated wrappers.
 *
 * <p>Generated via running org.web3j.codegen.SolidityFunctionWrapperGenerator with params:
 * <em>project-home</em>/src/test/resources/solidity/fibonacci.abi -o
 * <em>project-home</em>/src/integration-test/java -p org.web3j.generated
 */
public class FunctionWrappersIT extends Scenario {

    @Test
    public void testFibonacci() throws Exception {
        Fibonacci fibonacci =
                Fibonacci.load(
                        "0x3c05b2564139fb55820b18b72e94b2178eaace7d",
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
                        "0x3c05b2564139fb55820b18b72e94b2178eaace7d",
                        Web3j.build(new HttpService()),
                        ALICE,
                        STATIC_GAS_PROVIDER);

        TransactionReceipt transactionReceipt =
                fibonacci.fibonacciNotify(BigInteger.valueOf(15)).send();

        Fibonacci.NotifyEventResponse result = fibonacci.getNotifyEvents(transactionReceipt).get(0);

        assertEquals(result.input, (new Uint256(BigInteger.valueOf(15))));

        assertEquals(result.result, (new Uint256(BigInteger.valueOf(610))));
    }
}
