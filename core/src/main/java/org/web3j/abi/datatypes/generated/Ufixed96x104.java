package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Ufixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Ufixed96x104 extends Ufixed {
    public static final Ufixed96x104 DEFAULT = new Ufixed96x104(BigInteger.ZERO);

    public Ufixed96x104(BigInteger value) {
        super(96, 104, value);
    }

    public Ufixed96x104(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(96, 104, m, n);
    }
}
