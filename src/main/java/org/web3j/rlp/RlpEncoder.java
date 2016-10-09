package org.web3j.rlp;

import java.util.Arrays;
import java.util.List;

import org.web3j.utils.Hex;
import org.web3j.utils.Numeric;

/**
 * <p>Recursive Length Prefix (RLP) encoder. For </p>
 */
public class RlpEncoder {

    private static final int STRING_OFFSET = 0x80;
    private static final int LIST_OFFSET = 0xc0;

    public static byte[] encode(RlpType value) {
        if (value instanceof RlpString) {
            return encodeString((RlpString) value);
        } else {
            return encodeList((RlpList) value);
        }
    }

    static byte[] encodeString(RlpString value) {
        String stringValue = value.getValue();

        byte[] bytesValue;
        if (Numeric.containsHexPrefix(stringValue)) {
            bytesValue = Hex.hexStringToByteArray(stringValue);
        } else {
            bytesValue = stringValue.getBytes();
        }

        return encode(bytesValue, STRING_OFFSET);
    }

    private static byte[] encode(byte[] bytesValue, int offset) {
        if (bytesValue.length == 1 && bytesValue[0] >= (byte) 0x00 && bytesValue[0] <= (byte) 0x7f) {
            return bytesValue;
        } else if (bytesValue.length < 55) {
            byte[] result = new byte[bytesValue.length + 1];
            result[0] = (byte) (offset + bytesValue.length);
            System.arraycopy(bytesValue, 0, result, 1, bytesValue.length);
            return result;
        } else {
            byte[] encodedStringLength = toMinimalByteArray(bytesValue.length);
            byte[] result = new byte[bytesValue.length + encodedStringLength.length + 1];

            result[0] = (byte) ( (offset + 0x37) + encodedStringLength.length);
            System.arraycopy(encodedStringLength, 0, result, 1, encodedStringLength.length);
            System.arraycopy(
                    bytesValue, 0, result, encodedStringLength.length + 1, bytesValue.length);
            return result;
        }
    }

    private static byte[] toMinimalByteArray(int value) {
        byte[] encoded = toByteArray(value);

        for (int i = 0; i < encoded.length; i++) {
            if (encoded[i] != 0) {
                return Arrays.copyOfRange(encoded, i, encoded.length);
            }
        }

        return new byte[]{ };
    }

    private static byte[] toByteArray(int value) {
        return new byte[] {
                (byte) ((value >> 24) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) (value & 0xff)
        };
    }

    static byte[] encodeList(RlpList value) {
        List<RlpType> values = value.getValues();
        if (values.isEmpty()) {
            return encode(new byte[]{ }, LIST_OFFSET);
        } else {
            byte[] result = new byte[0];
            for (RlpType entry:values) {
                result = concat(result, encode(entry));
            }
            return encode(result, LIST_OFFSET);
        }
    }

    private static byte[] concat(byte[] b1, byte[] b2) {
        byte[] result = Arrays.copyOf(b1, b1.length + b2.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        return result;
    }
}
