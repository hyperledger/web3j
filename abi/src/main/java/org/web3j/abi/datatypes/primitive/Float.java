package org.web3j.abi.datatypes.primitive;

import org.web3j.abi.datatypes.NumericType;

public class Float extends Number<java.lang.Float> {

    public Float(float value) {
        super(value);
    }

    @Override
    public NumericType toSolidityType() {
        throw new UnsupportedOperationException("Fixed types are not supported");
    }
}
