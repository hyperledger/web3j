package org.web3j.abi;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.utils.Async;


/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
public abstract class Contract extends ManagedTransaction {

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(50_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000);

    private String contractAddress;

    protected Contract(String contractAddress, Web3j web3j, Credentials credentials) {
        super(web3j, credentials);
        this.contractAddress = contractAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * Execute constant function call - i.e. a call that does not change state of the contract
     *
     * @param function to call
     * @param <T> Generic type of return values
     * @return {@link List} of values returned by function call
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private <T extends Type> List<T> executeCall(
            Function function) throws InterruptedException, ExecutionException {
        String encodedFunction = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall = web3j.ethCall(
                Transaction.createEthCallTransaction(contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        String value = ethCall.getValue();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }

    protected <T extends Type> Future<T> executeCallSingleValueReturnAsync(
            final Function function) {
        return Async.run(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return executeCallSingleValueReturn(function);
            }
        });
    }

    protected <T extends Type> Future<List<T>> executeCallMultipleValueReturnAsync(
            final Function function) {
        return Async.run(new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                return executeCallMultipleValueReturn(function);
            }
        });
    }

    protected <T extends Type> T executeCallSingleValueReturn(
            Function function) throws InterruptedException, ExecutionException {
        List<T> values = executeCall(function);
        return values.get(0);
    }

    protected <T extends Type> List<T> executeCallMultipleValueReturn(
            Function function) throws InterruptedException, ExecutionException {
        return executeCall(function);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Contract#executeTransactionAsync}.
     *
     * @param function to transact with
     * @return the transaction receipt
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @throws TransactionTimeoutException if the transaction was not mined while waiting
     */
    protected TransactionReceipt executeTransaction(
            Function function) throws ExecutionException, InterruptedException,
            TransactionTimeoutException {
        BigInteger nonce = getNonce(credentials.getAddress());
        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction = RawTransaction.createFunctionCallTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                contractAddress,
                encodedFunction);

        return signAndSend(rawTransaction);
    }

    /**
     * Execute the provided function as a transaction asynchronously.
     *
     * @param function to transact with
     * @return {@link Future} containing executing transaction
     */
    protected Future<TransactionReceipt> executeTransactionAsync(final Function function) {
        return Async.run(new Callable<TransactionReceipt>() {
            @Override
            public TransactionReceipt call() throws Exception {
                return executeTransaction(function);
            }
        });
    }

    protected EventValues extractEventParameters(
            Event event, TransactionReceipt transactionReceipt) {

        List<Log> logs = transactionReceipt.getLogs();

        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues = new ArrayList<>();

        for (Log log:logs) {
            List<String> topics = log.getTopics();
            String encodedEventSignature = EventEncoder.encode(event);
            if (topics.get(0).equals(encodedEventSignature)) {

                nonIndexedValues = FunctionReturnDecoder.decode(
                        log.getData(), event.getNonIndexedParameters());

                List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
                for (int i = 0; i < indexedParameters.size(); i++) {
                    Type value = FunctionReturnDecoder.decodeIndexedValue(
                            topics.get(i+1), indexedParameters.get(i));
                    indexedValues.add(value);
                }
            }
        }

        return new EventValues(indexedValues, nonIndexedValues);
    }

    private static String create(
            Web3j web3j, Credentials credentials,
            String binary, String encodedConstructor, BigInteger value)
            throws InterruptedException, ExecutionException, TransactionTimeoutException {

        Contract contract = new Contract("", web3j, credentials) { };

        BigInteger nonce = contract.getNonce(contract.credentials.getAddress());
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                value,
                binary + encodedConstructor);

        TransactionReceipt transactionReceipt = contract.signAndSend(rawTransaction);
        String contractAddress = transactionReceipt.getContractAddress();
        Objects.requireNonNull(contractAddress);
        return contractAddress;
    }

    protected static <T extends Contract> T deploy(Class<T> type,
            Web3j web3j, Credentials credentials,
            String binary, String encodedConstructor, BigInteger value) throws Exception {

        String contractAddress = create(web3j, credentials, binary, encodedConstructor, value);

        Constructor<T> constructor =
                type.getConstructor(String.class, Web3j.class, Credentials.class);

        return constructor.newInstance(contractAddress, web3j, credentials);
    }

    protected static <T extends Contract> Future<T> deployAsync(
            final Class<T> type, final Web3j web3j, final Credentials credentials,
            final String binary, final String encodedConstructor, final BigInteger value) {

        return Async.run(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return deploy(type, web3j, credentials, binary, encodedConstructor, value);
            }
        });
    }
}
