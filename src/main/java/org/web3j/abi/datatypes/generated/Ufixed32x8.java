package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed32x8 extends Ufixed {
  public static final Ufixed32x8 DEFAULT = new Ufixed32x8(BigInteger.ZERO);

  public Ufixed32x8(BigInteger value) {
    super(32, 8, value);
  }

  public Ufixed32x8(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(32, 8, m, n);
  }
}
