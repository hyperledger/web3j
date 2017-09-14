package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed112x48 extends Fixed {
    public static final Fixed112x48 DEFAULT = new Fixed112x48(BigInteger.ZERO);

    public Fixed112x48(BigInteger value) {
        super(112, 48, value);
    }

    public Fixed112x48(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(112, 48, m, n);
    }
}
