package org.web3j.abi;

import java.math.BigInteger;
import java.util.Optional;
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

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(50_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000);

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    protected Web3j web3j;
    protected Credentials credentials;

    protected ManagedTransaction(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
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
}
