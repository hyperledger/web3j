package org.web3j.abi.datatypes;

/**
 * Boolean type.
 */
public class Bool implements Type<Boolean> {

    private boolean value;

    public Bool(boolean value) {
        this.value = value;
    }

    @Override
    public String getTypeAsString() {
        return "bool";
    }

    @Override
    public Boolean getValue() {
        return value;
    }
}
