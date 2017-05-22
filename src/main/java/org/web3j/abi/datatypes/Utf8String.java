package org.web3j.abi.datatypes;

/**
 * UTF-8 encoded string type.
 */
public class Utf8String implements Type<String> {

    public static final String TYPE_NAME = "string";
    public static final Utf8String DEFAULT = new Utf8String("");

    private String value;

    public Utf8String(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return TYPE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Utf8String that = (Utf8String) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value;
    }
}
