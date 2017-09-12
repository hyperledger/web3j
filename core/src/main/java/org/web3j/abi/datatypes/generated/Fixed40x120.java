package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed40x120 extends Fixed {
    public static final Fixed40x120 DEFAULT = new Fixed40x120(BigInteger.ZERO);

    public Fixed40x120(BigInteger value) {
        super(40, 120, value);
    }

    public Fixed40x120(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(40, 120, m, n);
    }
}
