package org.web3j.abi.datatypes;

import java.math.BigInteger;

/**
 * Integer type.
 */
public class Int extends IntType {

    public Int(BigInteger value) {
        // "int" values should be declared as int256 in computing function selectors
        this(MAX_BIT_LENGTH, value);
    }

    public Int(int bitSize, BigInteger value) {
        super("int", bitSize, value);
    }
}
