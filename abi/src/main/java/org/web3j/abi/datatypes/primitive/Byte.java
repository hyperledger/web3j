package org.web3j.abi.datatypes.primitive;

import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes1;

public final class Byte extends PrimitiveType<java.lang.Byte> {

    public Byte(byte value) {
        super(value);
    }

    @Override
    public Type toSolidityType() {
        return new Bytes1(new byte[]{getValue()});
    }
}
