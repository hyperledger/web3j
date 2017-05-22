package org.web3j.abi.datatypes;

/**
 * Boolean type.
 */
public class Bool implements Type<Boolean> {

    public static final String TYPE_NAME = "bool";
    public static final Bool DEFAULT = new Bool(false);

    private boolean value;

    public Bool(boolean value) {
        this.value = value;
    }

    @Override
    public String getTypeAsString() {
        return TYPE_NAME;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Bool bool = (Bool) o;

        return value == bool.value;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }
}
