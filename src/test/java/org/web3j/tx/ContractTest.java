package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;

import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.utils.Async;
import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContractTest extends ManagedTransactionTester {

    private static final String TEST_CONTRACT_BINARY = "12345";

    private TestContract contract;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        contract = new TestContract(
                ADDRESS, web3j, SampleKeys.CREDENTIALS,
                Contract.GAS_PRICE, Contract.GAS_LIMIT);
    }

    @Test
    public void testGetContractAddress() {
        assertThat(contract.getContractAddress(), is(ADDRESS));
    }

    @Test
    public void testGetContractTransactionReceipt() {
        assertFalse(contract.getTransactionReceipt().isPresent());
    }

    @Test
    public void testDeploy() throws Exception {
        TransactionReceipt transactionReceipt = createTransactionReceipt();
        Contract deployedContract = deployContract(transactionReceipt);

        assertThat(deployedContract.getContractAddress(), is(ADDRESS));
        assertTrue(deployedContract.getTransactionReceipt().isPresent());
        assertThat(deployedContract.getTransactionReceipt().get(), equalTo(transactionReceipt));
    }

    private TransactionReceipt createTransactionReceipt() {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setContractAddress(ADDRESS);
        return transactionReceipt;
    }

    @Test
    public void testIsValid() throws Exception {
        prepareEthGetCode(TEST_CONTRACT_BINARY);

        Contract contract = deployContract(createTransactionReceipt());
        assertTrue(contract.isValid());
    }

    @Test
    public void testIsValidDifferentCode() throws Exception {
        prepareEthGetCode(TEST_CONTRACT_BINARY + "0");

        Contract contract = deployContract(createTransactionReceipt());
        assertFalse(contract.isValid());
    }

    @Test
    public void testIsValidEmptyCode() throws Exception {
        prepareEthGetCode("");

        Contract contract = deployContract(createTransactionReceipt());
        assertFalse(contract.isValid());
    }

    @Test(expected = RuntimeException.class)
    public void testDeployInvalidContractAddress() throws Throwable {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);

        prepareTransaction(transactionReceipt);

        String encodedConstructor = FunctionEncoder.encodeConstructor(
                Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        try {
            TestContract.deployAsync(
                    TestContract.class, web3j, SampleKeys.CREDENTIALS,
                    ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                    "0xcafed00d", encodedConstructor, BigInteger.ZERO).get();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testCallSingleValue() throws Exception {
        // Example taken from FunctionReturnDecoderTest

        EthCall ethCall = new EthCall();
        ethCall.setResult("0x0000000000000000000000000000000000000000000000000000000000000020"
                + "0000000000000000000000000000000000000000000000000000000000000000");
        prepareCall(ethCall);

        assertThat(contract.callSingleValue().get(), equalTo(new Utf8String("")));
    }

    @Test
    public void testCallSingleValueEmpty() throws Exception {
        // Example taken from FunctionReturnDecoderTest

        EthCall ethCall = new EthCall();
        ethCall.setResult("0x");
        prepareCall(ethCall);

        assertNull(contract.callSingleValue().get());
    }

    @Test
    public void testCallMultipleValue() throws Exception {
        EthCall ethCall = new EthCall();
        ethCall.setResult("0x0000000000000000000000000000000000000000000000000000000000000037"
                + "0000000000000000000000000000000000000000000000000000000000000007");
        prepareCall(ethCall);

        assertThat(contract.callMultipleValue().get(),
                equalTo(Arrays.asList(
                        new Uint256(BigInteger.valueOf(55)),
                        new Uint256(BigInteger.valueOf(7)))));
    }

    @Test
    public void testCallMultipleValueEmpty() throws Exception {
        EthCall ethCall = new EthCall();
        ethCall.setResult("0x");
        prepareCall(ethCall);

        assertThat(contract.callMultipleValue().get(),
                equalTo(Collections.emptyList()));
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
                // encoded function
                "0xfceb437c298f40d64702ac26411b2316e79f3c28ffa60edfc891ad4fc8ab82ca",
                // indexed value
                "0000000000000000000000003d6cb163f7c72d20b0fcd6baae5889329d138a4a"));
        // non-indexed value
        log.setData("0000000000000000000000000000000000000000000000000000000000000001");

        transactionReceipt.setLogs(Arrays.asList(log));

        EventValues eventValues = contract.processEvent(transactionReceipt).get(0);

        assertThat(eventValues.getIndexedValues(),
                equalTo(Collections.singletonList(
                        new Address("0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a"))));
        assertThat(eventValues.getNonIndexedValues(),
                equalTo(Collections.singletonList(new Uint256(BigInteger.ONE))));
    }

    @Test(expected = TransactionTimeoutException.class)
    public void testTimeout() throws Throwable {
        prepareTransaction(null);

        TransactionManager transactionManager = new RawTransactionManager(
                web3j, SampleKeys.CREDENTIALS, 1, 1);

        contract = new TestContract(
                ADDRESS, web3j, transactionManager,
                Contract.GAS_PRICE, Contract.GAS_LIMIT);

        testErrorScenario();
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidTransactionResponse() throws Throwable {
        prepareNonceRequest();

        EthSendTransaction ethSendTransaction = new EthSendTransaction();
        ethSendTransaction.setError(new Response.Error(1, "Invalid transaction"));

        Request rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.sendAsync()).thenReturn(Async.run(() -> ethSendTransaction));
        when(web3j.ethSendRawTransaction(any(String.class)))
                .thenReturn(rawTransactionRequest);

        testErrorScenario();
    }


    @Test(expected = RuntimeException.class)
    public void testInvalidTransactionReceipt() throws Throwable {
        prepareNonceRequest();
        prepareTransactionRequest();

        EthGetTransactionReceipt ethGetTransactionReceipt = new EthGetTransactionReceipt();
        ethGetTransactionReceipt.setError(new Response.Error(1, "Invalid transaction receipt"));

        Request getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.sendAsync())
                .thenReturn(Async.run(() -> ethGetTransactionReceipt));
        when(web3j.ethGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn(getTransactionReceiptRequest);

        testErrorScenario();
    }

    void testErrorScenario() throws Throwable {
        try {
            contract.performTransaction(
                    new Address(BigInteger.TEN), new Uint256(BigInteger.ONE)).get();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    private Contract deployContract(TransactionReceipt transactionReceipt)
            throws ExecutionException, InterruptedException, IOException {

        prepareTransaction(transactionReceipt);

        String encodedConstructor = FunctionEncoder.encodeConstructor(
                Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        return TestContract.deployAsync(
                TestContract.class, web3j, SampleKeys.CREDENTIALS,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                "0xcafed00d", encodedConstructor, BigInteger.ZERO).get();
    }

    private void prepareEthGetCode(String binary) throws IOException {
        EthGetCode ethGetCode = new EthGetCode();
        ethGetCode.setResult(Numeric.prependHexPrefix(binary));

        Request ethGetCodeRequest = mock(Request.class);
        when(ethGetCodeRequest.send())
                .thenReturn(ethGetCode);
        when(web3j.ethGetCode(ADDRESS, DefaultBlockParameterName.LATEST))
                .thenReturn(ethGetCodeRequest);
    }

    private static class TestContract extends Contract {
        public TestContract(
                String contractAddress, Web3j web3j, Credentials credentials,
                BigInteger gasPrice, BigInteger gasLimit) {
            super(TEST_CONTRACT_BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
        }

        public TestContract(
                String contractAddress,
                Web3j web3j, TransactionManager transactionManager,
                BigInteger gasPrice, BigInteger gasLimit) {
            super(TEST_CONTRACT_BINARY, contractAddress, web3j, transactionManager, gasPrice,
                    gasLimit);
        }

        public Future<Utf8String> callSingleValue() {
            Function function = new Function("call",
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                    }));
            return executeCallSingleValueReturnAsync(function);
        }

        public Future<List<Type>> callMultipleValue()
                throws ExecutionException, InterruptedException {
            Function function = new Function("call",
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Uint256>() { },
                            new TypeReference<Uint256>() { }));
            return executeCallMultipleValueReturnAsync(function);
        }

        public Future<TransactionReceipt> performTransaction(
                Address address, Uint256 amount) {
            Function function = new Function("approve",
                    Arrays.<Type>asList(address, amount),
                    Collections.<TypeReference<?>>emptyList());
            return executeTransactionAsync(function);
        }

        public List<EventValues> processEvent(TransactionReceipt transactionReceipt) {
            Event event = new Event("Event",
                    Arrays.<TypeReference<?>>asList(new TypeReference<Address>() { }),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() { }));
            return extractEventParameters(event, transactionReceipt);
        }
    }
}
