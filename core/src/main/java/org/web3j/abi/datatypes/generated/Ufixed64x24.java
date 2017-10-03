package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Ufixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Ufixed64x24 extends Ufixed {
    public static final Ufixed64x24 DEFAULT = new Ufixed64x24(BigInteger.ZERO);

    public Ufixed64x24(BigInteger value) {
        super(64, 24, value);
    }

    public Ufixed64x24(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(64, 24, m, n);
    }
}
