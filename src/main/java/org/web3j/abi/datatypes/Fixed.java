package org.web3j.abi.datatypes;

import java.math.BigInteger;

/**
 * Signed fixed type.
 */
public class Fixed extends FixedPointType {

    public Fixed(int mBitSize, int nBitSize, BigInteger value) {
        super("fixed", mBitSize, nBitSize, value);
    }

    public Fixed(BigInteger value) {
        this(DEFAULT_BIT_LENGTH, DEFAULT_BIT_LENGTH, value);
    }

    public Fixed(BigInteger m, BigInteger n) {
        this(convert(m, n));
    }

    public Fixed(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        this(convert(mBitSize, nBitSize, m, n));
    }
}
