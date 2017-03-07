package org.web3j.tx;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.utils.Async;


/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
public abstract class Contract extends ManagedTransaction {

    // https://www.reddit.com/r/ethereum/comments/5g8ia6/attention_miners_we_recommend_raising_gas_limit/
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4300000);

    private String contractAddress;
    private final BigInteger gasPrice;
    private final BigInteger gasLimit;
    private TransactionReceipt transactionReceipt;

    protected Contract(String contractAddress, Web3j web3j, TransactionManager transactionManager,
                       BigInteger gasPrice, BigInteger gasLimit) {
        super(web3j, transactionManager);

        this.contractAddress = contractAddress;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    protected Contract(String contractAddress, Web3j web3j, Credentials credentials,
                       BigInteger gasPrice, BigInteger gasLimit) {
        this(contractAddress, web3j, new RawTransactionManager(web3j, credentials),
                gasPrice, gasLimit);
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setTransactionReceipt(TransactionReceipt transactionReceipt) {
        this.transactionReceipt = transactionReceipt;
    }

    /**
     * If this Contract instance was created at deployment, the TransactionReceipt associated
     * with the initial creation will be provided, e.g. via a <em>deploy</em> method. This will not persist
     * for Contracts instances constructed via a <em>load</em> method.
     *
     * @return the TransactionReceipt generated at contract deployment
     */
    public TransactionReceipt getTransactionReceipt() {
        return transactionReceipt;
    }

    /**
     * Execute constant function call - i.e. a call that does not change state of the contract
     *
     * @param function to call
     * @return {@link List} of values returned by function call
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private List<Type> executeCall(
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

    protected Future<List<Type>> executeCallMultipleValueReturnAsync(
            final Function function) {
        return Async.run(new Callable<List<Type>>() {
            @Override
            public List<Type> call() throws Exception {
                return executeCallMultipleValueReturn(function);
            }
        });
    }

    protected <T extends Type> T executeCallSingleValueReturn(
            Function function) throws InterruptedException, ExecutionException {
        List<Type> values = executeCall(function);
        return (T) values.get(0);
    }

    protected List<Type> executeCallMultipleValueReturn(
            Function function) throws InterruptedException, ExecutionException {
        return executeCall(function);
    }

    protected TransactionReceipt executeTransaction(Function function) throws InterruptedException,
            ExecutionException, TransactionTimeoutException {
        return executeTransaction(FunctionEncoder.encode(function), BigInteger.ZERO);
    }


    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Contract#executeTransactionAsync}.
     *
     * @param data  to send in transaction
     * @param value in Wei to send in transaction
     * @return transaction receipt
     * @throws ExecutionException          if the computation threw an
     *                                     exception
     * @throws InterruptedException        if the current thread was interrupted
     *                                     while waiting
     * @throws TransactionTimeoutException if the transaction was not mined while waiting
     */
    protected TransactionReceipt executeTransaction(
            String data, BigInteger value)
            throws ExecutionException, InterruptedException, TransactionTimeoutException {

        return send(contractAddress, data, value, gasPrice, gasLimit);
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
            Event event, Log log) {

        List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (!topics.get(0).equals(encodedEventSignature)) {
            return null;
        }

        List<Type> indexedValues = new ArrayList<Type>();
        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(
                log.getData(), event.getNonIndexedParameters());

        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                    topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
    }

    protected List<EventValues> extractEventParameters(
            Event event, TransactionReceipt transactionReceipt) {

        List<Log> logs = transactionReceipt.getLogs();
        List<EventValues> values = new ArrayList<EventValues>();
        for (Log log : logs) {
            EventValues eventValues = extractEventParameters(event, log);
            if (eventValues != null) {
                values.add(eventValues);
            }
        }

        return values;
    }

    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) throws Exception {

        Constructor<T> constructor = type.getDeclaredConstructor(
                String.class, Web3j.class, Credentials.class,
                BigInteger.class, BigInteger.class);
        constructor.setAccessible(true);

        // we want to use null here to ensure that "to" parameter on message is not populated
        T contract = constructor.newInstance(null, web3j, credentials, gasPrice, gasLimit);

        return create(contract, binary, encodedConstructor, value);
    }

    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) throws Exception {

        Constructor<T> constructor = type.getDeclaredConstructor(
                String.class, Web3j.class, TransactionManager.class,
                BigInteger.class, BigInteger.class);
        constructor.setAccessible(true);

        // we want to use null here to ensure that "to" parameter on message is not populated
        T contract = constructor.newInstance(null, web3j, transactionManager, gasPrice, gasLimit);

        return create(contract, binary, encodedConstructor, value);
    }

    private static <T extends Contract> T create(
            T contract, String binary, String encodedConstructor, BigInteger value)
            throws InterruptedException, ExecutionException, TransactionTimeoutException {
        TransactionReceipt transactionReceipt =
                contract.executeTransaction(binary + encodedConstructor, value);

        String contractAddress = transactionReceipt.getContractAddress();
        if (contractAddress == null) {
            throw new RuntimeException("Empty contract address returned");
        }
        contract.setContractAddress(contractAddress);
        contract.setTransactionReceipt(transactionReceipt);

        return contract;
    }

    protected static <T extends Contract> Future<T> deployAsync(
            final Class<T> type, final Web3j web3j, final Credentials credentials,
            final BigInteger gasPrice, final BigInteger gasLimit,
            final String binary, final String encodedConstructor, final BigInteger value) {

        return Async.run(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return deploy(type, web3j, credentials, gasPrice, gasLimit,
                        binary, encodedConstructor, value);
            }
        });
    }

    protected static <T extends Contract> Future<T> deployAsync(
            final Class<T> type, final Web3j web3j, final TransactionManager transactionManager,
            final BigInteger gasPrice, final BigInteger gasLimit,
            final String binary, final String encodedConstructor, final BigInteger value) {

        return Async.run(
                new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        return deploy(type, web3j, transactionManager, gasPrice, gasLimit,
                                binary, encodedConstructor, value);
                    }
                });
    }
}
