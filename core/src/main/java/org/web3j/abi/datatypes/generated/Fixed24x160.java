package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed24x160 extends Fixed {
    public static final Fixed24x160 DEFAULT = new Fixed24x160(BigInteger.ZERO);

    public Fixed24x160(BigInteger value) {
        super(24, 160, value);
    }

    public Fixed24x160(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(24, 160, m, n);
    }
}
