package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed8x88 extends Fixed {
    public static final Fixed8x88 DEFAULT = new Fixed8x88(BigInteger.ZERO);

    public Fixed8x88(BigInteger value) {
        super(8, 88, value);
    }

    public Fixed8x88(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(8, 88, m, n);
    }
}
