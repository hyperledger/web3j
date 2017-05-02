package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed200x16 extends Ufixed {
  public static final Ufixed200x16 DEFAULT = new Ufixed200x16(BigInteger.ZERO);

  public Ufixed200x16(BigInteger value) {
    super(200, 16, value);
  }

  public Ufixed200x16(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(200, 16, m, n);
  }
}
