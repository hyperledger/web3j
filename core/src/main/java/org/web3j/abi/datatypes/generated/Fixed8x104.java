package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed8x104 extends Fixed {
    public static final Fixed8x104 DEFAULT = new Fixed8x104(BigInteger.ZERO);

    public Fixed8x104(BigInteger value) {
        super(8, 104, value);
    }

    public Fixed8x104(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(8, 104, m, n);
    }
}
