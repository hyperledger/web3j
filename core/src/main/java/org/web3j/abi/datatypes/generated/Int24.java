package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Int;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Int24 extends Int {
    public static final Int24 DEFAULT = new Int24(BigInteger.ZERO);

    public Int24(BigInteger value) {
        super(24, value);
    }

    public Int24(long value) {
        this(BigInteger.valueOf(value));
    }
}
