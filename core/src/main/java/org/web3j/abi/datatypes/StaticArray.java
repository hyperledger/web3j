package org.web3j.abi.datatypes;

import java.util.List;

/**
 * Static array type.
 */
public class StaticArray<T extends Type> extends Array<T> {

    public StaticArray(T... values) {
        super(values[0].getTypeAsString() + "[" + values.length + "]", values);
    }

    public StaticArray(List<T> values) {
        super(values.get(0).getTypeAsString() + "[" + values.size() + "]", values);
    }
}
