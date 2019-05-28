package org.web3j.abi.datatypes.primitive;

import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.generated.Int64;

public final class Long extends Number<java.lang.Long> {

    public Long(long value) {
        super(value);
    }

    @Override
    public NumericType toSolidityType() {
        return new Int64(getValue());
    }
}
