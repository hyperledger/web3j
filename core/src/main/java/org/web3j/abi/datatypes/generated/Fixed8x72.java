package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed8x72 extends Fixed {
    public static final Fixed8x72 DEFAULT = new Fixed8x72(BigInteger.ZERO);

    public Fixed8x72(BigInteger value) {
        super(8, 72, value);
    }

    public Fixed8x72(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(8, 72, m, n);
    }
}
