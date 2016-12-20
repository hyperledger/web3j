package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed32x80 extends Fixed {
  public Fixed32x80(BigInteger value) {
    super(32, 80, value);
  }

  public Fixed32x80(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(32, 80, m, n);
  }
}
