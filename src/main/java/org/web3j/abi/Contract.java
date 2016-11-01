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
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.utils.Numeric;


/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
public abstract class Contract {

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(50_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000);

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    private String contractAddress;
    private Web3j web3j;
    private Credentials credentials;

    public Contract(String contractAddress, Web3j web3j, Credentials credentials) {
        this.contractAddress = contractAddress;
        this.web3j = web3j;
        this.credentials = credentials;
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
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TransactionTimeoutException
     */
    protected TransactionReceipt executeTransaction(
            Function function) throws InterruptedException, ExecutionException,
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

    private TransactionReceipt signAndSend(RawTransaction rawTransaction)
            throws InterruptedException, ExecutionException, TransactionTimeoutException{
        byte[] signedMessage = TransactionEncoder.signMessage(
                rawTransaction, credentials.getEcKeyPair());
        String hexValue = Numeric.toHexString(signedMessage);

        // This might be a good candidate for using functional composition with CompletableFutures
        EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue)
                .sendAsync().get();

        String transactionHash = transactionResponse.getTransactionHash();

        return waitForTransactionReceipt(transactionHash);
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

    private BigInteger getNonce(String address) throws InterruptedException, ExecutionException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return ethGetTransactionCount.getTransactionCount();
    }

    private TransactionReceipt waitForTransactionReceipt(
            String transactionHash) throws InterruptedException, ExecutionException,
            TransactionTimeoutException {

        return getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);
    }

    private TransactionReceipt getTransactionReceipt(
            String transactionHash, int sleepDuration, int attempts)
            throws InterruptedException, ExecutionException, TransactionTimeoutException {

        Optional<TransactionReceipt> receiptOptional =
                sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                return receiptOptional.get();
            }
        }

        throw new TransactionTimeoutException("Transaction receipt was not generated after " +
                ((sleepDuration * attempts) / 1000 + " seconds"));
    }

    private Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws InterruptedException, ExecutionException {
        EthGetTransactionReceipt transactionReceipt =
                web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
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
        Optional<String> contractAddress = transactionReceipt.getContractAddress();
        if (contractAddress.isPresent()) {
            return contractAddress.get();
        } else {
            throw new RuntimeException("Empty contract address returned");
        }
    }

    protected static <T extends Contract> T deploy(Class<T> type,
            Web3j web3j, Credentials credentials,
            String binary, String encodedConstructor, BigInteger value) throws Exception {

        String contractAddress = create(web3j, credentials, binary, encodedConstructor, value);

        Constructor<T> constructor =
                type.getConstructor(String.class, Web3j.class, Credentials.class);

        return constructor.newInstance(contractAddress, web3j, credentials);
    }

    protected static <T extends Contract> CompletableFuture<T> deployAsync(
            Class<T> type, Web3j web3j, Credentials credentials,
            String binary, String encodedConstructor, BigInteger value) {
        CompletableFuture<T> result = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                result.complete(
                        deploy(type, web3j, credentials, binary, encodedConstructor, value));
            } catch (Throwable e) {
                result.completeExceptionally(e);
            }
        });
        return result;
    }
}
