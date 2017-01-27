package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed32x144 extends Fixed {
  public static final Fixed32x144 DEFAULT = new Fixed32x144(BigInteger.ZERO);

  public Fixed32x144(BigInteger value) {
    super(32, 144, value);
  }

  public Fixed32x144(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(32, 144, m, n);
  }
}
