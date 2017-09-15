package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Ufixed;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class Ufixed48x72 extends Ufixed {
    public static final Ufixed48x72 DEFAULT = new Ufixed48x72(BigInteger.ZERO);

    public Ufixed48x72(BigInteger value) {
        super(48, 72, value);
    }

    public Ufixed48x72(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(48, 72, m, n);
    }
}
