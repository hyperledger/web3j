package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed32x224 extends Fixed {
    public static final Fixed32x224 DEFAULT = new Fixed32x224(BigInteger.ZERO);

    public Fixed32x224(BigInteger value) {
        super(32, 224, value);
    }

    public Fixed32x224(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(32, 224, m, n);
    }
}
