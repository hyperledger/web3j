package org.web3j.protocol.core.filters;

/**
 * Filter exception wrapper
 */
public class FilterException extends RuntimeException {

    public FilterException(String message) {
        super(message);
    }

    public FilterException(String message, Throwable cause) {
        super(message, cause);
    }
}
