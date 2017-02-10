package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Fixed;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Fixed40x208 extends Fixed {
  public static final Fixed40x208 DEFAULT = new Fixed40x208(BigInteger.ZERO);

  public Fixed40x208(BigInteger value) {
    super(40, 208, value);
  }

  public Fixed40x208(int mBitSize, int nBitSize, BigInteger m, BigInteger n) {
    super(40, 208, m, n);
  }
}
