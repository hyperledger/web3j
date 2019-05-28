package org.web3j.abi.datatypes.primitive;

import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.generated.Int32;

public final class Int extends Number<java.lang.Integer> {

    public Int(int value) {
        super(value);
    }

    @Override
    public NumericType toSolidityType() {
        return new Int32(getValue());
    }
}