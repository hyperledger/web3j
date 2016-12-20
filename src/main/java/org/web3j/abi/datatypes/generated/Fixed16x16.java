package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed16x16 extends Fixed {
  public Fixed16x16(BigInteger value) {
    super(16, 16, value);
  }

  public Fixed16x16(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(16, 16, m, n);
  }
}
