package org.web3j.abi.datatypes.primitive;

import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;

public final class Boolean extends PrimitiveType<java.lang.Boolean> {

    public Boolean(boolean value) {
        super(value);
    }

    @Override
    public Type toSolidityType() {
        return new Bool(getValue());
    }
}
