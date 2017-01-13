package org.web3j.abi;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

    // https://www.reddit.com/r/ethereum/comments/5g8ia6/attention_miners_we_recommend_raising_gas_limit/
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    private String contractAddress;

    protected Contract(String contractAddress, Web3j web3j, Credentials credentials,
                       BigInteger gasPrice, BigInteger gasLimit) {
        super(web3j, credentials, gasPrice, gasLimit);

        this.contractAddress = contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContractAddress() {
        return contractAddress;
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

    protected <T extends Type> CompletableFuture<T> executeCallSingleValueReturnAsync(
            Function function) {
        return Async.run(() -> executeCallSingleValueReturn(function));
    }

    protected CompletableFuture<List<Type>> executeCallMultipleValueReturnAsync(
            Function function) {
        return Async.run(() -> executeCallMultipleValueReturn(function));
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
     * @param data to send in transaction
     * @return {@link Optional} containing our transaction receipt
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @throws TransactionTimeoutException if the transaction was not mined while waiting
     */
    protected TransactionReceipt executeTransaction(
            String data, BigInteger value)
            throws ExecutionException, InterruptedException, TransactionTimeoutException {

        BigInteger nonce = getNonce(credentials.getAddress());

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                contractAddress,
                value,
                data);

        return signAndSend(rawTransaction);
    }

    /**
     * Execute the provided function as a transaction asynchronously.
     *
     * @param function to transact with
     * @return {@link Future} containing executing transaction
     */
    protected CompletableFuture<TransactionReceipt> executeTransactionAsync(Function function) {
        return Async.run(() -> executeTransaction(function));
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

    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) throws Exception {

        Constructor<T> constructor = type.getDeclaredConstructor(
                String.class, Web3j.class, Credentials.class, BigInteger.class, BigInteger.class);
        constructor.setAccessible(true);

        T contract = constructor.newInstance("", web3j, credentials, gasPrice, gasLimit);
        TransactionReceipt transactionReceipt =
                contract.executeTransaction(binary + encodedConstructor, value);

        String contractAddress = transactionReceipt.getContractAddress();
        if (contractAddress == null) {
            throw new RuntimeException("Empty contract address returned");
        }
        contract.setContractAddress(contractAddress);

        return contract;
    }

    protected static <T extends Contract> CompletableFuture<T> deployAsync(
            Class<T> type, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) {

        return Async.run(() -> deploy(type, web3j, credentials, gasPrice, gasLimit,
                binary, encodedConstructor, value));
    }
}
