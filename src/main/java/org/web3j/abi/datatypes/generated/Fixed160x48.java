package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed160x48 extends Fixed {
  public static final Fixed160x48 DEFAULT = new Fixed160x48(BigInteger.ZERO);

  public Fixed160x48(BigInteger value) {
    super(160, 48, value);
  }

  public Fixed160x48(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(160, 48, m, n);
  }
}
