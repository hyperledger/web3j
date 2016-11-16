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


/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
public abstract class Contract extends ManagedTransaction {

    private String contractAddress;

    protected Contract(String contractAddress, Web3j web3j, Credentials credentials,
                       BigInteger gasPrice, BigInteger gasLimit) {
        super(web3j, credentials, gasPrice, gasLimit);

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
            Function function) {
        CompletableFuture<T> result =
                new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                result.complete(executeCallSingleValueReturn(function));
            } catch (InterruptedException|ExecutionException e) {
                result.completeExceptionally(e);
            }
        });
        return result;
    }

    protected <T extends Type> Future<List<T>> executeCallMultipleValueReturnAsync(
            Function function) {
        CompletableFuture<List<T>> result =
                new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                result.complete(executeCallMultipleValueReturn(function));
            } catch (InterruptedException|ExecutionException e) {
                result.completeExceptionally(e);
            }
        });
        return result;
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
     * @return {@link Optional} containing our transaction receipt
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
                gasPrice,
                gasLimit,
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
    protected Future<TransactionReceipt> executeTransactionAsync(Function function) {
        CompletableFuture<TransactionReceipt> result =
                new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                result.complete(executeTransaction(function));
            } catch (InterruptedException|ExecutionException|TransactionTimeoutException e) {
                result.completeExceptionally(e);
            }
        });
        return result;
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
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value)
            throws InterruptedException, ExecutionException, TransactionTimeoutException {

        Contract contract = new Contract("", web3j, credentials, gasPrice, gasLimit) { };

        BigInteger nonce = contract.getNonce(contract.credentials.getAddress());
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(
                nonce,
                gasPrice,
                gasLimit,
                value,
                binary + encodedConstructor);

        TransactionReceipt transactionReceipt = contract.signAndSend(rawTransaction);
        Optional<String> contractAddress = transactionReceipt.getContractAddress();
        if (contractAddress.isPresent()) {
            return contractAddress.get();
        } else {
            throw new RuntimeException("Empty contract address returned");
        }
    }

    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) throws Exception {

        String contractAddress = create(web3j, credentials, gasPrice, gasLimit,
                binary, encodedConstructor, value);

        Constructor<T> constructor = type.getDeclaredConstructor(
                String.class, Web3j.class, Credentials.class, BigInteger.class, BigInteger.class);
        constructor.setAccessible(true);

        return constructor.newInstance(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected static <T extends Contract> CompletableFuture<T> deployAsync(
            Class<T> type, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) {
        CompletableFuture<T> result = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                result.complete(
                        deploy(type, web3j, credentials, gasPrice, gasLimit,
                                binary, encodedConstructor, value));
            } catch (Throwable e) {
                result.completeExceptionally(e);
            }
        });
        return result;
    }
}
