package org.web3j.abi.datatypes;

import java.math.BigInteger;

/**
 * Common numeric type.
 */
public abstract class NumericType implements Type<BigInteger> {

    private String type;
    BigInteger value;

    public NumericType(String type, BigInteger value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public BigInteger getValue() {
        return value;
    }
}
