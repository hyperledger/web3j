package org.web3j.abi;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.utils.Numeric;


/**
 * Generic transaction manager
 */
public abstract class ManagedTransaction {

    // Sensible defaults as of November 2016...
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(50_000_000_000L);
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000);

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    protected Web3j web3j;
    protected Credentials credentials;

    protected BigInteger gasPrice;
    protected BigInteger gasLimit;

    private int sleepDuration = SLEEP_DURATION;
    private int attempts = ATTEMPTS;

    protected ManagedTransaction(Web3j web3j, Credentials credentials,
                                 BigInteger gasPrice, BigInteger gasLimit) {
        this.web3j = web3j;
        this.credentials = credentials;

        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
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

    protected TransactionReceipt signAndSend(RawTransaction rawTransaction)
            throws InterruptedException, ExecutionException, TransactionTimeoutException{
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        // This might be a good candidate for using functional composition with CompletableFutures
        EthSendTransaction transactionResponse = web3j.ethSendRawTransaction(hexValue)
                .sendAsync().get();

        String transactionHash = transactionResponse.getTransactionHash();

        return waitForTransactionReceipt(transactionHash);
    }

    protected BigInteger getNonce(String address) throws InterruptedException, ExecutionException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return ethGetTransactionCount.getTransactionCount();
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
                ((sleepDuration * attempts) / 1000 + " seconds"));
    }

    private TransactionReceipt sendTransactionReceiptRequest(
            String transactionHash) throws InterruptedException, ExecutionException {
        EthGetTransactionReceipt transactionReceipt =
                web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
    }
}
