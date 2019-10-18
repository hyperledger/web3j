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

/** Signed fixed type. */
public class Ufixed extends FixedPointType {

    public static final String TYPE_NAME = "ufixed";
    public static final Ufixed DEFAULT = new Ufixed(BigInteger.ZERO);

    protected Ufixed(int mBitSize, int nBitSize, BigInteger value) {
        super(TYPE_NAME, mBitSize, nBitSize, value);
    }

    public Ufixed(BigInteger value) {
        this(DEFAULT_BIT_LENGTH, DEFAULT_BIT_LENGTH, value);
    }

    public Ufixed(BigInteger m, BigInteger n) {
        this(convert(m, n));
    }

    protected Ufixed(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        this(convert(mBitSize, nBitSize, m, n));
    }

    @Override
    boolean valid(int mBitSize, int nBitSize, BigInteger value) {
        return super.valid(mBitSize, nBitSize, value) && value.signum() != -1;
    }
}
