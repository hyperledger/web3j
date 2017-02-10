package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed24x232 extends Fixed {
  public static final Fixed24x232 DEFAULT = new Fixed24x232(BigInteger.ZERO);

  public Fixed24x232(BigInteger value) {
    super(24, 232, value);
  }

  public Fixed24x232(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(24, 232, m, n);
  }
}
