package org.web3j.utils;

import java.util.List;

/**
 * Utility functions for working with Collections.
 */
public class Collection {

    private Collection() { }

    public static <T> String join(List<T> list, String separator, Function<T, String> function) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += function.apply(list.get(i));
            if (i + 1 < list.size()) {
                result += separator;
            }
        }
        return result;
    }

    public static String join(List<String> list, String separator) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
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
