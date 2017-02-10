package org.web3j.tx;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;


/**
 * Generic transaction manager.
 */
public abstract class ManagedTransaction {

    // https://www.reddit.com/r/ethereum/comments/5g8ia6/attention_miners_we_recommend_raising_gas_limit/
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    protected Web3j web3j;

    private TransactionManager transactionManager;

    private int sleepDuration = SLEEP_DURATION;
    private int attempts = ATTEMPTS;

    protected ManagedTransaction(Web3j web3j, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.web3j = web3j;
    }

    // In case anyone wishes to override the defaults
    public int getSleepDuration() {
        return sleepDuration;
    }

    public void setSleepDuration(int sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public BigInteger getGasPrice() throws InterruptedException, ExecutionException {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();

        return ethGasPrice.getGasPrice();
    }

    protected TransactionReceipt send(
            String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit)
            throws InterruptedException, ExecutionException, TransactionTimeoutException {

        EthSendTransaction transaction = transactionManager.executeTransaction(
                gasPrice, gasLimit, to, data, value);
        return processResponse(transaction);
    }

    protected TransactionReceipt processResponse(EthSendTransaction transactionResponse)
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

        TransactionReceipt receiptOptional =
                sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (receiptOptional == null) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                return receiptOptional;
            }
        }

        throw new TransactionTimeoutException("Transaction receipt was not generated after " +
                        ((sleepDuration * attempts) / 1000 +
                        " seconds for transaction: " + transactionHash));
    }

    private TransactionReceipt sendTransactionReceiptRequest(
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
