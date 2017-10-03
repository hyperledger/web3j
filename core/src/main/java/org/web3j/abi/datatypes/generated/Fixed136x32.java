package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed136x32 extends Fixed {
    public static final Fixed136x32 DEFAULT = new Fixed136x32(BigInteger.ZERO);

    public Fixed136x32(BigInteger value) {
        super(136, 32, value);
    }

    public Fixed136x32(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(136, 32, m, n);
    }
}
