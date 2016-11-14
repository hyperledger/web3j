package org.web3j.utils;

/**
 * Command line utility classes.
 */
public class Console {
    public static void exitError(String message) {
        System.err.println(message);
        System.exit(1);
    }

    public static void exitError(Throwable throwable) {
        exitError(throwable.getMessage());
    }
}
