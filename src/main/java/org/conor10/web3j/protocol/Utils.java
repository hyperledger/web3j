package org.conor10.web3j.protocol;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Utility functions.
 *
 * Implementation as per https://github.com/ethereum/wiki/wiki/JSON-RPC#hex-value-encoding
 */
public final class Utils {
    private Utils() { }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static ObjectReader getObjectReader() {
        return objectMapper.reader();
    }

    public static final String encodeQuantity(int value) {
        return "0x" + Integer.toHexString(value);
    }

    public static final long decodeQuantity(String value) {
        if (!isValidHexQuantity(value)) {
            throw new MessageEncodingException("Value must be in format 0x[1-9]+[0-9]* or 0x0");
        }
        try {
            return Long.parseLong(value.substring(2), 16);  // see http://bit.ly/2c7guWx
        } catch (NumberFormatException e) {
            throw new MessageEncodingException("Negative ", e);
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
