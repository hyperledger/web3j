package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed56x24 extends Ufixed {
  public static final Ufixed56x24 DEFAULT = new Ufixed56x24(BigInteger.ZERO);

  public Ufixed56x24(BigInteger value) {
    super(56, 24, value);
  }

  public Ufixed56x24(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(56, 24, m, n);
  }
}
