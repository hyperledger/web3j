package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed8x32 extends Fixed {
  public static final Fixed8x32 DEFAULT = new Fixed8x32(BigInteger.ZERO);

  public Fixed8x32(BigInteger value) {
    super(8, 32, value);
  }

  public Fixed8x32(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(8, 32, m, n);
  }
}
