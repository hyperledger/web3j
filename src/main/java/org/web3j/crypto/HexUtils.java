package org.web3j.crypto;

/**
 * Crypto library utility functions.
 */
public final class HexUtils {
    private HexUtils() {
    }

    public static byte[] hexStringToByteArray(String input) {
        String cleanInput = cleanHexPrefix(input);

        int len = cleanInput.length();

        if (len == 0) {
            return new byte[] {};
        }
        if (len == 1) {
            return new byte[] { (byte) Character.digit(cleanInput.charAt(0), 16) };
        }

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(cleanInput.charAt(i), 16) << 4)
                    + Character.digit(cleanInput.charAt(i+1), 16));
        }
        return data;
    }

    static String cleanHexPrefix(String input) {
        if (input.length()  > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x') {
            return input.substring(2);
        } else {
            return input;
        }
    }

    public static String toHexString(byte[] input) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("0x");
        for (byte b:input) {
            stringBuffer.append(String.format("%02x", b & 0xFF));
        }

        return stringBuffer.toString();
    }

    static byte b(int m, int n) {
        return (byte) ( (m << 4) | n);
    }
}
