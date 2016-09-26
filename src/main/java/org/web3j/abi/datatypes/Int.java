package org.web3j.abi.datatypes;

import java.math.BigInteger;

/**
 * Integer type.
 */
public class Int extends IntType {

    public static final String TYPE_NAME = "int";

    public Int(BigInteger value) {
        // "int" values should be declared as int256 in computing function selectors
        this(MAX_BIT_LENGTH, value);
    }

    protected Int(int bitSize, BigInteger value) {
        super(TYPE_NAME, bitSize, value);
    }
}
