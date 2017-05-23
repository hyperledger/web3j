package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed88x128 extends Fixed {
    public static final Fixed88x128 DEFAULT = new Fixed88x128(BigInteger.ZERO);

    public Fixed88x128(BigInteger value) {
        super(88, 128, value);
    }

    public Fixed88x128(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(88, 128, m, n);
    }
}
