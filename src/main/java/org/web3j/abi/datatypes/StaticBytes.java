package org.web3j.abi.datatypes;

/**
 * Statically allocated sequence of bytes.
 */
public class StaticBytes extends Bytes {

    public StaticBytes(byte[] src) {
        super(src, "bytes" + src.length);
        if (!isValid(src)) {
            throw new UnsupportedOperationException(
                    "Input byte array must be in range 0 < M <= 32");
        };
    }

    public boolean isValid(byte[] src) {
        int length = src.length;
        return length > 0 && length <= 32;
    }
}
