package org.web3j.utils;

import java.util.Arrays;

/**
 * Utility functions for working with Collections.
 */
public class Collection {
    static String[] EMPTY_STRING_ARRAY = { };

    public static String[] tail(String[] args) {
        if (args.length == 0) {
            return EMPTY_STRING_ARRAY;
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }

    public static <T> T[] create(T... args) {
        return args;
    }
}
