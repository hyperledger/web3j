package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed144x96 extends Fixed {
    public static final Fixed144x96 DEFAULT = new Fixed144x96(BigInteger.ZERO);

    public Fixed144x96(BigInteger value) {
        super(144, 96, value);
    }

    public Fixed144x96(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(144, 96, m, n);
    }
}
