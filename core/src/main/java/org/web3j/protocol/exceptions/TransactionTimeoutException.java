package org.web3j.protocol.exceptions;

/**
 * Transaction timeout exception indicates that we have breached some threshold waiting for a
 * transaction to execute.
 */
public class TransactionTimeoutException extends Exception {
    public TransactionTimeoutException(String message) {
        super(message);
    }

    public TransactionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
