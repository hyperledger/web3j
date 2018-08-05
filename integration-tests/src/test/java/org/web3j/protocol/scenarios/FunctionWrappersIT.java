package org.web3j.protocol.scenarios;

import java.math.BigInteger;

import org.junit.Test;

import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.generated.Fibonacci;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.TestParameters;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

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
        Fibonacci fibonacci = Fibonacci.load(
                "0x581f4ef871aef69324cff1df9708678e2bff9870",
                Web3j.build(new HttpService(TestParameters.TEST_RINKEBY_URL)),
                ALICE, GAS_PRICE, GAS_LIMIT);

        BigInteger result = fibonacci.fibonacci(BigInteger.valueOf(10)).send();
        assertThat(result, equalTo(BigInteger.valueOf(55)));
    }

    @Test
    public void testFibonacciNotify() throws Exception {
        Fibonacci fibonacci = Fibonacci.load(
                "0x581f4ef871aef69324cff1df9708678e2bff9870",
                Web3j.build(new HttpService(TestParameters.TEST_RINKEBY_URL)),
                ALICE, GAS_PRICE, GAS_LIMIT);

        TransactionReceipt transactionReceipt = fibonacci.fibonacciNotify(
                BigInteger.valueOf(15)).send();

        Fibonacci.NotifyEventResponse result = fibonacci.getNotifyEvents(transactionReceipt).get(0);

        assertThat(result.input,
                equalTo(BigInteger.valueOf(15)));

        assertThat(result.result,
                equalTo(BigInteger.valueOf(610)));
    }
}
