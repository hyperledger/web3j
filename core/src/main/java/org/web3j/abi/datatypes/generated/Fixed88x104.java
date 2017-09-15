package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed88x104 extends Fixed {
    public static final Fixed88x104 DEFAULT = new Fixed88x104(BigInteger.ZERO);

    public Fixed88x104(BigInteger value) {
        super(88, 104, value);
    }

    public Fixed88x104(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(88, 104, m, n);
    }
}
