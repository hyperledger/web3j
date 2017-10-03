package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed56x192 extends Fixed {
    public static final Fixed56x192 DEFAULT = new Fixed56x192(BigInteger.ZERO);

    public Fixed56x192(BigInteger value) {
        super(56, 192, value);
    }

    public Fixed56x192(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(56, 192, m, n);
    }
}
