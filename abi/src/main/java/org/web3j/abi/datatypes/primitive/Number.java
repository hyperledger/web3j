package org.web3j.abi.datatypes.primitive;

import org.web3j.abi.datatypes.NumericType;

public abstract class Number<T extends java.lang.Number> extends PrimitiveType<T> {
    Number(T value) {
        super(value);
    }

    @Override
    public abstract NumericType toSolidityType();
}
