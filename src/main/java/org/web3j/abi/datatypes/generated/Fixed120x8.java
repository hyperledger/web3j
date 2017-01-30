package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed120x8 extends Fixed {
  public static final Fixed120x8 DEFAULT = new Fixed120x8(BigInteger.ZERO);

  public Fixed120x8(BigInteger value) {
    super(120, 8, value);
  }

  public Fixed120x8(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(120, 8, m, n);
  }
}
