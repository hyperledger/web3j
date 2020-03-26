/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.abi.datatypes;

import java.math.BigInteger;

/** Common fixed-point type properties. */
public abstract class FixedPointType extends NumericType {

    static final int DEFAULT_BIT_LENGTH = MAX_BIT_LENGTH >> 1;

    private final int bitSize;

    public FixedPointType(
            final String typePrefix,
            final int mBitSize,
            final int nBitSize,
            final BigInteger value) {
        super(typePrefix + mBitSize + "x" + nBitSize, value);
        this.bitSize = mBitSize + nBitSize;
        if (!valid(mBitSize, nBitSize, value)) {
            throw new UnsupportedOperationException(
                    "Bitsize must be 8 bit aligned, and in range 0 < bitSize <= 256");
        }
    }

    @Override
    public int getBitSize() {
        return bitSize;
    }

    boolean valid(final int mBitSize, final int nBitSize, final BigInteger value) {
        return isValidBitSize(mBitSize, nBitSize) && isValidBitCount(mBitSize, nBitSize, value);
    }

    private boolean isValidBitSize(final int mBitSize, final int nBitSize) {
        return mBitSize % 8 == 0 && nBitSize % 8 == 0 && bitSize > 0 && bitSize <= MAX_BIT_LENGTH;
    }

    private static boolean isValidBitCount(
            final int mBitSize, final int nBitSize, final BigInteger value) {
        return value.bitCount() <= mBitSize + nBitSize;
    }

    static BigInteger convert(final BigInteger m, final BigInteger n) {
        return convert(DEFAULT_BIT_LENGTH, DEFAULT_BIT_LENGTH, m, n);
    }

    static BigInteger convert(
            final int mBitSize, final int nBitSize, final BigInteger m, final BigInteger n) {
        final BigInteger mPadded = m.shiftLeft(nBitSize);
        final int nBitLength = n.bitLength();

        // find next multiple of 4
        final int shift = (nBitLength + 3) & ~0x03;
        return mPadded.or(n.shiftLeft(nBitSize - shift));
    }
}
