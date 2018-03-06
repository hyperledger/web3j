package org.web3j.utils;

import java.util.Collections;
import java.util.List;

/**
 * String utility functions.
 */
public class Strings {

    private Strings() {}

    public static String toCsv(List<String> src) {
        // return src == null ? null : String.join(", ", src.toArray(new String[0]));
        return join(src, ", ");
    }

    public static String join(List<String> src, String delimiter) {
        return src == null ? null : String.join(delimiter, src.toArray(new String[0]));
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

    public static byte[] asciiToHex(String asciiValue, int length) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        String hexStr = hex.toString() + "".join("", Collections.nCopies(length - (hex.length()/2), "00"));
        return Numeric.hexStringToByteArray(hexStr);
    }

    public static String hexStringToAscii(String hexStr) {
        assert(hexStr.length() % 2 == 0);
        StringBuilder asciiStr = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            if (str.equals("00")) {
                break;
            }
            asciiStr.append((char)Integer.parseInt(str, 16));
        }
        return asciiStr.toString();
    }
}
