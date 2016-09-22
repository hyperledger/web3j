package org.web3j.abi.datatypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Fixed size array.
 */
public abstract class Array<T extends Type> implements Type<List<T>> {

    private String type;
    private List<T> value;

    public Array(String type, T... values) {
        if (!valid(values, type)) {
            throw new UnsupportedOperationException(
                    "If empty list is provided, use empty array instance");
        }

        this.type = type;
        this.value = Arrays.asList(values);
    }

    Array(String type) {
        this.type = type;
        this.value = Collections.emptyList();
    }

    @Override
    public List<T> getValue() {
        return value;
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    private boolean valid(T[] values, String type) {
        return values != null || values.length != 0 || type != null;
    }
}
