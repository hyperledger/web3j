package org.web3j.abi.datatypes;


/**
 * Static array type.
 */
public class StaticArray<T extends Type> extends Array<T> {

    public StaticArray(T... values) {
        super(values[0].getTypeAsString() + "[" + values.length + "]", values);
    }
}
