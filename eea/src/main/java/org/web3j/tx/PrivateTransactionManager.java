package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.response.PollingPrivateTransactionReceiptProcessor;
import org.web3j.tx.response.PrivateTransactionReceiptProcessor;

public abstract class PrivateTransactionManager extends TransactionManager {
    private final PrivateTransactionReceiptProcessor transactionReceiptProcessor;

    protected PrivateTransactionManager(
            PrivateTransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        super(transactionReceiptProcessor, fromAddress);
        this.transactionReceiptProcessor = transactionReceiptProcessor;
    }

    protected PrivateTransactionManager(
            Eea eea, String enclavePublicKey, String fromAddress) {
        this(new PollingPrivateTransactionReceiptProcessor(
                        eea, enclavePublicKey,
                        DEFAULT_POLLING_FREQUENCY,
                        DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH),
                fromAddress);
    }

    protected PrivateTransactionManager(
            Eea eea, String enclavePublicKey,
            int attempts, long sleepDuration, String fromAddress) {
        this(new PollingPrivateTransactionReceiptProcessor(
                eea, enclavePublicKey,
                sleepDuration, attempts), fromAddress);
    }

    @Override
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException, TransactionException {

        EthSendTransaction ethSendTransaction = sendTransaction(
                gasPrice, gasLimit, to, data, value);
        return processResponse(ethSendTransaction);
    }

    private TransactionReceipt processResponse(EthSendTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: "
                    + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }
}
