package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed96x120 extends Ufixed {
    public static final Ufixed96x120 DEFAULT = new Ufixed96x120(BigInteger.ZERO);

    public Ufixed96x120(BigInteger value) {
        super(96, 120, value);
    }

    public Ufixed96x120(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(96, 120, m, n);
    }
}
