package org.web3j.protocol.scenarios;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.generated.Fibonacci;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test Fibonacci contract generated wrappers.
 *
 * Generated via running {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} with params:
 * <project-home>/src/test/resources/solidity/fibonacci.abi -o
 * <project-home>/src/integration-test/java -p org.web3j.generated
 */
public class FunctionWrappersIT extends Scenario {

    @Test
    public void testFibonacci() throws Exception {
        Fibonacci fibonacci = new Fibonacci(
                "0x3c05b2564139fb55820b18b72e94b2178eaace7d", Web3j.build(new HttpService()), ALICE);

        Uint256 result = fibonacci.fibonacci(new Uint256(BigInteger.valueOf(10))).get();
        assertThat(result.getValue(), equalTo(BigInteger.valueOf(55)));
    }

    @Test
    public void testFibonacciNotify() throws Exception {
        Fibonacci fibonacci = new Fibonacci(
                "0x3c05b2564139fb55820b18b72e94b2178eaace7d", Web3j.build(new HttpService()), ALICE);

        TransactionReceipt transactionReceipt = fibonacci.fibonacciNotify(
                new Uint256(BigInteger.valueOf(15))).get();

        EventValues result = fibonacci.processNotifyEvent(transactionReceipt);

        assertThat(result.getNonIndexedValues(),
                equalTo(Arrays.asList(
                        new Uint256(BigInteger.valueOf(15)),
                        new Uint256(BigInteger.valueOf(610)))));
    }
}
