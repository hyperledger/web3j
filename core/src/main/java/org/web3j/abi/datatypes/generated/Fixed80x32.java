package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed80x32 extends Fixed {
    public static final Fixed80x32 DEFAULT = new Fixed80x32(BigInteger.ZERO);

    public Fixed80x32(BigInteger value) {
        super(80, 32, value);
    }

    public Fixed80x32(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(80, 32, m, n);
    }
}
