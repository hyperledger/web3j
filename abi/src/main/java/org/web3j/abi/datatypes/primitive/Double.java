package org.web3j.abi.datatypes.primitive;

import org.web3j.abi.datatypes.NumericType;

public final class Double extends Number<java.lang.Double> {

    public Double(double value) {
        super(value);
    }

    @Override
    public NumericType toSolidityType() {
        throw new UnsupportedOperationException("Fixed types are not supported");
    }
}
