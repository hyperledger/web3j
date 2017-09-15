package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed8x248 extends Fixed {
    public static final Fixed8x248 DEFAULT = new Fixed8x248(BigInteger.ZERO);

    public Fixed8x248(BigInteger value) {
        super(8, 248, value);
    }

    public Fixed8x248(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(8, 248, m, n);
    }
}
