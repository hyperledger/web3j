package org.web3j.abi;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Async;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ContractTest {

    private static final String CONTRACT_ADDRESS = "0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a";
    private static final String TRANSACTION_HASH = "0xHASH";

    private Web3j web3j;
    private TestContract contract;

    @Before
    public void setUp() {
        web3j = mock(Web3j.class);
        contract = new TestContract(
                CONTRACT_ADDRESS, web3j, SampleKeys.CREDENTIALS,
                Contract.GAS_PRICE, Contract.GAS_LIMIT);
    }

    @Test
    public void testGetContractAddress() {
        assertThat(contract.getContractAddress(), is(CONTRACT_ADDRESS));
    }

    @Test
    public void testDeploy() throws Exception {

        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setContractAddress(CONTRACT_ADDRESS);

        prepareTransaction(transactionReceipt);

        String encodedConstructor = FunctionEncoder.encodeConstructor(
                Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        TestContract deployedContract = TestContract.deployAsync(
                TestContract.class, web3j, SampleKeys.CREDENTIALS,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                "0xcafed00d" , encodedConstructor, BigInteger.ZERO).get();
        assertThat(deployedContract.getContractAddress(), is(CONTRACT_ADDRESS));
    }

    private void prepareTransaction(TransactionReceipt transactionReceipt) {

        // Respond to nonce request
        EthGetTransactionCount ethGetTransactionCount = new EthGetTransactionCount();
        ethGetTransactionCount.setResult("0x1");

        Request transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.sendAsync())
                .thenReturn(Async.run(() -> ethGetTransactionCount));
        when(web3j.ethGetTransactionCount(SampleKeys.ADDRESS, DefaultBlockParameterName.LATEST))
                .thenReturn(transactionCountRequest);

        // Send transaction request
        EthSendTransaction ethSendTransaction = new EthSendTransaction();
        ethSendTransaction.setResult(TRANSACTION_HASH);

        Request rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.sendAsync()).thenReturn(Async.run(() -> ethSendTransaction));
        when(web3j.ethSendRawTransaction(any(String.class)))
                .thenReturn(rawTransactionRequest);

        // Query for transaction receipt
        EthGetTransactionReceipt ethGetTransactionReceipt = new EthGetTransactionReceipt();
        ethGetTransactionReceipt.setResult(transactionReceipt);

        Request getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.sendAsync())
                .thenReturn(Async.run(() -> ethGetTransactionReceipt));
        when(web3j.ethGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn(getTransactionReceiptRequest);
    }

    @Test
    public void testCallSingleValue() throws Exception {
        // Example taken from FunctionReturnDecoderTest

        EthCall ethCall = new EthCall();
        ethCall.setResult("0x0000000000000000000000000000000000000000000000000000000000000020" +
                "0000000000000000000000000000000000000000000000000000000000000000");
        prepareCall(ethCall);

        assertThat(contract.callSingleValue().get(), equalTo(new Utf8String("")));
    }

    @Test
    public void testCallMultipleValue() throws Exception {
        EthCall ethCall = new EthCall();
        ethCall.setResult("0x0000000000000000000000000000000000000000000000000000000000000037" +
                        "0000000000000000000000000000000000000000000000000000000000000007");
        prepareCall(ethCall);

        assertThat(contract.callMultipleValue().get(),
                equalTo(Arrays.asList(
                        new Uint256(BigInteger.valueOf(55)),
                        new Uint256(BigInteger.valueOf(7)))));
    }

    private void prepareCall(EthCall ethCall) {
        Request request = mock(Request.class);
        when(request.sendAsync()).thenReturn(Async.run(() -> ethCall));

        when(web3j.ethCall(any(Transaction.class), eq(DefaultBlockParameterName.LATEST)))
                .thenReturn(request);
    }

    @Test
    public void testTransaction() throws Exception {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);

        prepareTransaction(transactionReceipt);

        assertThat(contract.performTransaction(
                        new Address(BigInteger.TEN), new Uint256(BigInteger.ONE)).get(),
                is(transactionReceipt));
    }

    @Test
    public void testProcessEvent() {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        Log log = new Log();
        log.setTopics(Arrays.asList(
                "0xfceb437c298f40d64702ac26411b2316e79f3c28ffa60edfc891ad4fc8ab82ca",  // encoded function
                "0000000000000000000000003d6cb163f7c72d20b0fcd6baae5889329d138a4a")); // indexed value
        log.setData("0000000000000000000000000000000000000000000000000000000000000001");  // non-indexed value

        transactionReceipt.setLogs(Arrays.asList(log));

        EventValues eventValues = contract.processEvent(transactionReceipt);

        assertThat(eventValues.getIndexedValues(),
                equalTo(Collections.singletonList(
                        new Address("0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a"))));
        assertThat(eventValues.getNonIndexedValues(),
                equalTo(Collections.singletonList(new Uint256(BigInteger.ONE))));
    }

    @Test(expected = ExecutionException.class)
    public void testTimeout() throws Exception {
        prepareTransaction(null);

        contract.setAttempts(1);
        contract.setSleepDuration(1);

        contract.performTransaction(
                new Address(BigInteger.TEN), new Uint256(BigInteger.ONE)).get();
    }

    private static class TestContract extends Contract {
        public TestContract(String contractAddress, Web3j web3j, Credentials credentials,
                            BigInteger gasPrice, BigInteger gasLimit) {
            super(contractAddress, web3j, credentials, gasPrice, gasLimit);
        }

        public Future<Utf8String> callSingleValue() {
            Function function = new Function("call",
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
            return executeCallSingleValueReturnAsync(function);
        }

        public Future<List<Type>> callMultipleValue() throws ExecutionException, InterruptedException {
            Function function = new Function("call",
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Uint256>() {},
                            new TypeReference<Uint256>() {}));
            return executeCallMultipleValueReturnAsync(function);
        }

        public Future<TransactionReceipt> performTransaction(Address address, Uint256 amount) {
            Function function = new Function("approve",
                    Arrays.<Type>asList(address, amount), Collections.<TypeReference<?>>emptyList());
            return executeTransactionAsync(function);
        }

        public EventValues processEvent(TransactionReceipt transactionReceipt) {
            Event event = new Event("Event",
                    Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
            return extractEventParameters(event, transactionReceipt);
        }
    }
}
