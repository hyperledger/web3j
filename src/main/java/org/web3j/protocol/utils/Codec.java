package org.web3j.protocol.utils;

import java.math.BigInteger;

/**
 * <p>Message codec functions.</p>
 *
 * <p>Implementation as per https://github.com/ethereum/wiki/wiki/JSON-RPC#hex-value-encoding</p>
 */
public final class Codec {
    private Codec() {
    }

    public static final String encodeQuantity(BigInteger value) {
        if (value.signum() != -1) {
            return "0x" + value.toString(16);
        } else {
            throw new MessageEncodingException("Negative values are not supported");
        }
    }

    public static final BigInteger decodeQuantity(String value) {
        if (!isValidHexQuantity(value)) {
            throw new MessageDecodingException("Value must be in format 0x[1-9]+[0-9]* or 0x0");
        }
        try {
            return new BigInteger(value.substring(2), 16);
        } catch (NumberFormatException e) {
            throw new MessageDecodingException("Negative ", e);
        }
    }

    private static boolean isValidHexQuantity(String value) {
        if (value == null) {
            return false;
        }

        if (value.length() < 3) {
            return false;
        }

        if (value.charAt(0) != '0' || value.charAt(1) != 'x') {
            return false;
        }

        if (value.length() > 3 && value.charAt(2) == '0') {
            return false;
        }

        return true;
    }
}