package org.web3j.tx;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;

/**
 * Transaction manager abstraction for executing transactions with Ethereum client via
 * various mechanisms.
 */
public abstract class TransactionManager {

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    private final int sleepDuration;
    private final int attempts;

    private final Web3j web3j;

    protected TransactionManager(Web3j web3j) {
        this.web3j = web3j;
        this.attempts = ATTEMPTS;
        this.sleepDuration = SLEEP_DURATION;
    }

    protected TransactionManager(Web3j web3j, int attempts, int sleepDuration) {
        this.web3j = web3j;
        this.attempts = attempts;
        this.sleepDuration = sleepDuration;
    }

    TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws ExecutionException, InterruptedException, TransactionTimeoutException {

        EthSendTransaction ethSendTransaction = sendTransaction(gasPrice, gasLimit, to, data, value);
        return processResponse(ethSendTransaction);
    }

    abstract EthSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws ExecutionException, InterruptedException, TransactionTimeoutException;


    abstract String getFromAddress();

    private TransactionReceipt processResponse(EthSendTransaction transactionResponse)
            throws InterruptedException, ExecutionException, TransactionTimeoutException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: " +
                    transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return waitForTransactionReceipt(transactionHash);
    }

    private TransactionReceipt waitForTransactionReceipt(
            String transactionHash) throws InterruptedException, ExecutionException,
            TransactionTimeoutException {

        return getTransactionReceipt(transactionHash, sleepDuration, attempts);
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
                ((sleepDuration * attempts) / 1000 +
                        " seconds for transaction: " + transactionHash));
    }

    private Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws InterruptedException, ExecutionException {
        EthGetTransactionReceipt transactionReceipt =
                web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();
        if (transactionReceipt.hasError()) {
            throw new RuntimeException("Error processing request: " +
                    transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }
}
