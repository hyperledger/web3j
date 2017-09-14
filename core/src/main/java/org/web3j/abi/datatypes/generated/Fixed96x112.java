package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed96x112 extends Fixed {
    public static final Fixed96x112 DEFAULT = new Fixed96x112(BigInteger.ZERO);

    public Fixed96x112(BigInteger value) {
        super(96, 112, value);
    }

    public Fixed96x112(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(96, 112, m, n);
    }
}
