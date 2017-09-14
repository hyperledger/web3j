package org.web3j.abi.datatypes.generated;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Int;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.AbiTypesGenerator} to update.</p>
 */
public class Int48 extends Int {
    public static final Int48 DEFAULT = new Int48(BigInteger.ZERO);

    public Int48(BigInteger value) {
        super(48, value);
    }

    public Int48(long value) {
        this(BigInteger.valueOf(value));
    }
}
