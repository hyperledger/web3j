package org.web3j.protocol.exceptions;

/**
 * Transaction out of gas exception indicates that we have run out of gas when deploy a contract
 * or send transaction .
 */
public class TransactionOutOfGasException extends RuntimeException{
    public TransactionOutOfGasException(String message) {
        super(message);
    }

    public TransactionOutOfGasException(String message, Throwable cause) {
        super(message, cause);
    }

}
