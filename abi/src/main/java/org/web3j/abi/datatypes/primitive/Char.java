package org.web3j.abi.datatypes.primitive;

import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

public final class Char extends PrimitiveType<Character> {

    public Char(char value) {
        super(value);
    }

    @Override
    public Type toSolidityType() {
        return new Utf8String(String.valueOf(getValue()));
    }
}
