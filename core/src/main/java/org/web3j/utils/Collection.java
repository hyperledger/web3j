package org.web3j.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Utility functions for working with Collections.
 */
public class Collection {

    static String[] EMPTY_STRING_ARRAY = { };

    private Collection() { }

    public static String[] tail(String[] args) {
        if (args.length == 0) {
            return EMPTY_STRING_ARRAY;
        } else {
            return Arrays.copyOfRange(args, 1, args.length);
        }
    }

    @SafeVarargs
    public static <T> T[] create(T... args) {
        return args;
    }

    public static <T> String join(List<T> list, String separator, Function<T, String> function) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += function.apply(list.get(i)).trim();
            if (i + 1 < list.size()) {
                result += separator;
            }
        }
        return result;
    }

    public static String join(List<String> list, String separator) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i).trim();
            if (i + 1 < list.size()) {
                result += separator;
            }
        }
        return result;
    }

    public interface Function<R, S> {
        S apply(R r);
    }
}
