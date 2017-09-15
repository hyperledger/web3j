package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed168x8 extends Fixed {
    public static final Fixed168x8 DEFAULT = new Fixed168x8(BigInteger.ZERO);

    public Fixed168x8(BigInteger value) {
        super(168, 8, value);
    }

    public Fixed168x8(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(168, 8, m, n);
    }
}
