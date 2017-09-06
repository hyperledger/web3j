package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed48x200 extends Ufixed {
    public static final Ufixed48x200 DEFAULT = new Ufixed48x200(BigInteger.ZERO);

    public Ufixed48x200(BigInteger value) {
        super(48, 200, value);
    }

    public Ufixed48x200(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(48, 200, m, n);
    }
}
