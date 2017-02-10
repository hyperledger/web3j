package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed40x24 extends Ufixed {
  public static final Ufixed40x24 DEFAULT = new Ufixed40x24(BigInteger.ZERO);

  public Ufixed40x24(BigInteger value) {
    super(40, 24, value);
  }

  public Ufixed40x24(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(40, 24, m, n);
  }
}
