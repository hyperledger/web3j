package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed64x72 extends Fixed {
    public static final Fixed64x72 DEFAULT = new Fixed64x72(BigInteger.ZERO);

    public Fixed64x72(BigInteger value) {
        super(64, 72, value);
    }

    public Fixed64x72(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(64, 72, m, n);
    }
}
