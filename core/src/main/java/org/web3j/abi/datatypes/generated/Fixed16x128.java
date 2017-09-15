package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed16x128 extends Fixed {
    public static final Fixed16x128 DEFAULT = new Fixed16x128(BigInteger.ZERO);

    public Fixed16x128(BigInteger value) {
        super(16, 128, value);
    }

    public Fixed16x128(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(16, 128, m, n);
    }
}
