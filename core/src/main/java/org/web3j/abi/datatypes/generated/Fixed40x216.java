package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed40x216 extends Fixed {
    public static final Fixed40x216 DEFAULT = new Fixed40x216(BigInteger.ZERO);

    public Fixed40x216(BigInteger value) {
        super(40, 216, value);
    }

    public Fixed40x216(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(40, 216, m, n);
    }
}
