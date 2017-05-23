package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed16x88 extends Fixed {
    public static final Fixed16x88 DEFAULT = new Fixed16x88(BigInteger.ZERO);

    public Fixed16x88(BigInteger value) {
        super(16, 88, value);
    }

    public Fixed16x88(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(16, 88, m, n);
    }
}
