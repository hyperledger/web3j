package org.web3j.abi.datatypes;

import static java.util.Objects.requireNonNull;

/**
 * UTF-8 encoded string type.
 */
public class Utf8String implements Type<String> {

    public static final String TYPE_NAME = "string";

    private String value;

    public Utf8String(String value) {
        requireNonNull(value, "type must not be null");
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utf8String that = (Utf8String) o;

        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString(){
        return value;
    }
}
