package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed120x80 extends Fixed {
    public static final Fixed120x80 DEFAULT = new Fixed120x80(BigInteger.ZERO);

    public Fixed120x80(BigInteger value) {
        super(120, 80, value);
    }

    public Fixed120x80(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(120, 80, m, n);
    }
}
