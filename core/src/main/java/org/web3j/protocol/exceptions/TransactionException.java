package org.web3j.protocol.exceptions;

/**
 * Transaction timeout exception indicates that we have breached some threshold waiting for a
 * transaction to execute.
 */
public class TransactionException extends Exception {
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
