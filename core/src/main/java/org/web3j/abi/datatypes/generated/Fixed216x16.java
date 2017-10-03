package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed216x16 extends Fixed {
    public static final Fixed216x16 DEFAULT = new Fixed216x16(BigInteger.ZERO);

    public Fixed216x16(BigInteger value) {
        super(216, 16, value);
    }

    public Fixed216x16(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(216, 16, m, n);
    }
}
