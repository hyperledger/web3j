package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed88x88 extends Fixed {
  public static final Fixed88x88 DEFAULT = new Fixed88x88(BigInteger.ZERO);

  public Fixed88x88(BigInteger value) {
    super(88, 88, value);
  }

  public Fixed88x88(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(88, 88, m, n);
  }
}
