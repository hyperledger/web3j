package org.web3j.tx.response;

import java.io.IOException;
import java.util.Optional;

import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.eea.response.PrivateTransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

public class PollingPrivateTransactionReceiptProcessor extends PrivateTransactionReceiptProcessor {
    private final long sleepDuration;
    private final int attempts;

    public PollingPrivateTransactionReceiptProcessor(
            Eea eea, String enclavePublicKey,
            long sleepDuration, int attempts) {
        super(eea, enclavePublicKey);
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
    }

    @Override
    public PrivateTransactionReceipt waitForTransactionReceipt(
            String transactionHash)
            throws IOException, TransactionException {

        return getTransactionReceipt(transactionHash, sleepDuration, attempts);
    }

    private PrivateTransactionReceipt getTransactionReceipt(
            String transactionHash, long sleepDuration, int attempts)
            throws IOException, TransactionException {

        Optional<PrivateTransactionReceipt> receiptOptional =
                sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    throw new TransactionException(e);
                }
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                return receiptOptional.get();
            }
        }

        throw new TransactionException("Transaction receipt was not generated after "
                + ((sleepDuration * attempts) / 1000
                + " seconds for transaction: " + transactionHash), transactionHash);
    }
}
