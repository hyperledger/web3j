package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed64x128 extends Fixed {
  public static final Fixed64x128 DEFAULT = new Fixed64x128(BigInteger.ZERO);

  public Fixed64x128(BigInteger value) {
    super(64, 128, value);
  }

  public Fixed64x128(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(64, 128, m, n);
  }
}
