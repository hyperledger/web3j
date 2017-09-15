package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed40x200 extends Fixed {
    public static final Fixed40x200 DEFAULT = new Fixed40x200(BigInteger.ZERO);

    public Fixed40x200(BigInteger value) {
        super(40, 200, value);
    }

    public Fixed40x200(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(40, 200, m, n);
    }
}
