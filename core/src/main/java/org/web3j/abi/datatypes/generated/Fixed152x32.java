package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed152x32 extends Fixed {
    public static final Fixed152x32 DEFAULT = new Fixed152x32(BigInteger.ZERO);

    public Fixed152x32(BigInteger value) {
        super(152, 32, value);
    }

    public Fixed152x32(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(152, 32, m, n);
    }
}
