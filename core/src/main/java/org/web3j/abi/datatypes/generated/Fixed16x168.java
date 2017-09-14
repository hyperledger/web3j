package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed16x168 extends Fixed {
    public static final Fixed16x168 DEFAULT = new Fixed16x168(BigInteger.ZERO);

    public Fixed16x168(BigInteger value) {
        super(16, 168, value);
    }

    public Fixed16x168(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(16, 168, m, n);
    }
}
