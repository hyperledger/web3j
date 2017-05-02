package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Ufixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Ufixed192x32 extends Ufixed {
  public static final Ufixed192x32 DEFAULT = new Ufixed192x32(BigInteger.ZERO);

  public Ufixed192x32(BigInteger value) {
    super(192, 32, value);
  }

  public Ufixed192x32(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(192, 32, m, n);
  }
}
