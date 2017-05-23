package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed80x96 extends Fixed {
    public static final Fixed80x96 DEFAULT = new Fixed80x96(BigInteger.ZERO);

    public Fixed80x96(BigInteger value) {
        super(80, 96, value);
    }

    public Fixed80x96(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(80, 96, m, n);
    }
}
