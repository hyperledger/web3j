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
        String result = "";
        for (int i = 0; i < src.size(); i++) {
            result += src.get(i);
            if (i + 1 < src.size()) {
                result += delimiter;
            }
        }
        return result;
    }

    public static String capitaliseFirstLetter(String string) {
        if (string == null || string.length() == 0) {
            return string;
        } else {
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        }
    }
}
