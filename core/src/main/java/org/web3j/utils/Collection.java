package org.web3j.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility functions for working with Collections.
 */
public final class Collection {
    private static final String[] EMPTY_STRING_ARRAY = {};

    private Collection() {
    }

    public static String[] tail(String[] args) {
        return (args.length == 0) ? EMPTY_STRING_ARRAY : Arrays.copyOfRange(args, 1, args.length);
    }

    @SafeVarargs
    public static <T> T[] create(T... args) {
        return args;
    }

    public static <T> String join(List<T> list, CharSequence separator, Function<T, String> function) {
        return list.stream()
                .map(function)
                .map(String::trim)
                .collect(Collectors.joining(separator));
    }
}
