package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed56x200 extends Fixed {
    public static final Fixed56x200 DEFAULT = new Fixed56x200(BigInteger.ZERO);

    public Fixed56x200(BigInteger value) {
        super(56, 200, value);
    }

    public Fixed56x200(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(56, 200, m, n);
    }
}
