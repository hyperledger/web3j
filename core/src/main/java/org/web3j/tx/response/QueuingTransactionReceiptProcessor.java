package org.web3j.tx.response;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Async;

/**
 * Transaction receipt processor that uses a single thread to query for transaction receipts.
 *
 * <p><em>Note:</em>When initially invoked, this processor returns a transaction receipt containing
 * only the transaction hash of the submitted transaction. This is encapsulated in an
 * {@link EmptyTransactionReceipt}.
 */
public class QueuingTransactionReceiptProcessor extends TransactionReceiptProcessor {

    private final int pollingAttemptsPerTxHash;

    private final ScheduledExecutorService scheduledExecutorService;
    private final Callback callback;
    private final BlockingQueue<RequestWrapper> pendingTransactions;

    public QueuingTransactionReceiptProcessor(
            Web3j web3j, Callback callback,
            int pollingAttemptsPerTxHash, long pollingFrequency) {
        super(web3j);
        this.scheduledExecutorService = Async.defaultExecutorService();
        this.callback = callback;
        this.pendingTransactions = new LinkedBlockingQueue<>();
        this.pollingAttemptsPerTxHash = pollingAttemptsPerTxHash;

        scheduledExecutorService.scheduleAtFixedRate(
                this::sendTransactionReceiptRequests,
                pollingFrequency, pollingFrequency, TimeUnit.MILLISECONDS);
    }

    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException {
        pendingTransactions.add(new RequestWrapper(transactionHash));

        return new EmptyTransactionReceipt(transactionHash);
    }

    private void sendTransactionReceiptRequests() {
        for (RequestWrapper requestWrapper : pendingTransactions) {
            try {
                String transactionHash = requestWrapper.getTransactionHash();
                Optional<TransactionReceipt> transactionReceipt =
                        sendTransactionReceiptRequest(transactionHash);
                if (transactionReceipt.isPresent()) {
                    callback.accept(transactionReceipt.get());
                    pendingTransactions.remove(requestWrapper);
                } else {
                    if (requestWrapper.getCount() == pollingAttemptsPerTxHash) {
                        throw new TransactionException(
                                "No transaction receipt for txHash: " + transactionHash
                                        + "received after " + pollingAttemptsPerTxHash
                                        + " attempts", transactionHash);
                    } else {
                        requestWrapper.incrementCount();
                    }
                }
            } catch (IOException | TransactionException e) {
                pendingTransactions.remove(requestWrapper);
                callback.exception(e);
            }
        }
    }

    /**
     * Java doesn't provide a concurrent linked hash set, so we use a simple wrapper to store
     * details of the number of requests we've made against this specific transaction hash. This
     * is so we can preserve submission order as we interate over the outstanding transactions.
     *
     * <p>Note - the equals/hashcode methods only operate on the transactionHash field. This is
     * intentional.
     */
    private static class RequestWrapper {
        private final String transactionHash;
        private int count;

        RequestWrapper(String transactionHash) {
            this.transactionHash = transactionHash;
            this.count = 0;
        }

        String getTransactionHash() {
            return transactionHash;
        }

        int getCount() {
            return count;
        }

        void incrementCount() {
            this.count += 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RequestWrapper that = (RequestWrapper) o;

            return transactionHash.equals(that.transactionHash);
        }

        @Override
        public int hashCode() {
            return transactionHash.hashCode();
        }
    }
}
