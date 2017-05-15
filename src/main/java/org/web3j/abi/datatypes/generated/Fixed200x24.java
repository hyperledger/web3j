package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed200x24 extends Fixed {
    public static final Fixed200x24 DEFAULT = new Fixed200x24(BigInteger.ZERO);

    public Fixed200x24(BigInteger value) {
        super(200, 24, value);
    }

    public Fixed200x24(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(200, 24, m, n);
    }
}
