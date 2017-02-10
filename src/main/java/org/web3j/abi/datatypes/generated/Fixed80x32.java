package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;
import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed80x32 extends Fixed {
  public static final Fixed80x32 DEFAULT = new Fixed80x32(BigInteger.ZERO);

  public Fixed80x32(BigInteger value) {
    super(80, 32, value);
  }

  public Fixed80x32(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(80, 32, m, n);
  }
}
