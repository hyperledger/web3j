package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Fixed72x40 extends Fixed {
    public static final Fixed72x40 DEFAULT = new Fixed72x40(BigInteger.ZERO);

    public Fixed72x40(BigInteger value) {
        super(72, 40, value);
    }

    public Fixed72x40(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(72, 40, m, n);
    }
}
