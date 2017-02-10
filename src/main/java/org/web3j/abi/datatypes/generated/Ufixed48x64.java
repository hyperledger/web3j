package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed48x64 extends Ufixed {
  public static final Ufixed48x64 DEFAULT = new Ufixed48x64(BigInteger.ZERO);

  public Ufixed48x64(BigInteger value) {
    super(48, 64, value);
  }

  public Ufixed48x64(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(48, 64, m, n);
  }
}
