package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed16x48 extends Fixed {
    public static final Fixed16x48 DEFAULT = new Fixed16x48(BigInteger.ZERO);

    public Fixed16x48(BigInteger value) {
        super(16, 48, value);
    }

    public Fixed16x48(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(16, 48, m, n);
    }
}
