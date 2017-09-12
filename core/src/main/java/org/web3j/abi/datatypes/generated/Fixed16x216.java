package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed16x216 extends Fixed {
    public static final Fixed16x216 DEFAULT = new Fixed16x216(BigInteger.ZERO);

    public Fixed16x216(BigInteger value) {
        super(16, 216, value);
    }

    public Fixed16x216(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(16, 216, m, n);
    }
}
