package org.web3j.utils;

import java.util.List;

/**
 * String utility functions.
 */
public class Strings {

    private Strings() {}

    public static String toCsv(List<String> src) {
        return join(src, ", ");
    }

    public static String join(List<String> src, String delimiter) {
        if (src != null) {
            StringBuilder builder = new StringBuilder();
            if(!src.isEmpty()) {
                builder.append(src.get(0));
            }
            for (int i = 1; i < src.size(); i++) {
                builder.append(delimiter).append(src.get(i));
            }
            return builder.toString();
        }
        return null;
    }

    public static String capitaliseFirstLetter(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        }
    }

    public static String lowercaseFirstLetter(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return string.substring(0, 1).toLowerCase() + string.substring(1);
        }
    }

    public static String zeros(int n) {
        return repeat('0', n);
    }

    public static String repeat(char value, int n) {
        return new String(new char[n]).replace("\0", String.valueOf(value));
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
