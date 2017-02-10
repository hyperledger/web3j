package org.web3j.abi.datatypes;

import java.math.BigInteger;

/**
 * Signed fixed type.
 */
public class Fixed extends FixedPointType {

    public static final String TYPE_NAME = "fixed";
    public static final Fixed DEFAULT = new Fixed(BigInteger.ZERO);

    protected Fixed(int mBitSize, int nBitSize, BigInteger value) {
        super(TYPE_NAME , mBitSize, nBitSize, value);
    }

    public Fixed(BigInteger value) {
        this(DEFAULT_BIT_LENGTH, DEFAULT_BIT_LENGTH, value);
    }

    public Fixed(BigInteger m, BigInteger n) {
        this(convert(m, n));
    }

    protected Fixed(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        this(convert(mBitSize, nBitSize, m, n));
    }
}
