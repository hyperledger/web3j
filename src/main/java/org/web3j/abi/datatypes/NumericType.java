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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NumericType that = (NumericType) o;

        if (!type.equals(that.type)) {
            return false;
        }

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
