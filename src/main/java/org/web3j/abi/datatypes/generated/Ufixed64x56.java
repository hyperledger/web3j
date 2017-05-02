package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed64x56 extends Ufixed {
  public static final Ufixed64x56 DEFAULT = new Ufixed64x56(BigInteger.ZERO);

  public Ufixed64x56(BigInteger value) {
    super(64, 56, value);
  }

  public Ufixed64x56(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(64, 56, m, n);
  }
}
