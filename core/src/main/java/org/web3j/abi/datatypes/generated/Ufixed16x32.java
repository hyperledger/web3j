package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Ufixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Ufixed16x32 extends Ufixed {
    public static final Ufixed16x32 DEFAULT = new Ufixed16x32(BigInteger.ZERO);

    public Ufixed16x32(BigInteger value) {
        super(16, 32, value);
    }

    public Ufixed16x32(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(16, 32, m, n);
    }
}
