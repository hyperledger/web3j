package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed8x48 extends Ufixed {
    public static final Ufixed8x48 DEFAULT = new Ufixed8x48(BigInteger.ZERO);

    public Ufixed8x48(BigInteger value) {
        super(8, 48, value);
    }

    public Ufixed8x48(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
        super(8, 48, m, n);
    }
}
