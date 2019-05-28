package org.web3j.abi.datatypes;

import java.math.BigInteger;

/**
 * Common integer properties.
 */
public abstract class IntType extends NumericType {

    private final int bitSize;

    public IntType(String typePrefix, int bitSize, BigInteger value) {
        super(typePrefix + bitSize, value);
        if (!valid(bitSize, value)) {
            throw new UnsupportedOperationException(
                    "Bitsize must be 8 bit aligned, and in range 0 < bitSize <= 256");
        }
        this.bitSize = bitSize;
    }

    int getBitSize() {
        return bitSize;
    }

    boolean valid(int bitSize, BigInteger value) {
        return isValidBitSize(bitSize)
                && isValidBitCount(bitSize, value);
    }

    private static boolean isValidBitSize(int bitSize) {
        return bitSize % 8 == 0
                && bitSize > 0
                && bitSize <= MAX_BIT_LENGTH;
    }

    private static boolean isValidBitCount(int bitSize, BigInteger value) {
        return value.bitLength() <= bitSize;
    }
}
