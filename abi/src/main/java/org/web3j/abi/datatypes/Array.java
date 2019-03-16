package org.web3j.abi.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.web3j.abi.datatypes.generated.AbiTypes;

/**
 * Fixed size array.
 */
public abstract class Array<T extends Type> implements Type<List<T>> {

    private final Class<T> type;
    protected final List<T> value;

    @Deprecated
    @SafeVarargs
    Array(String type, T... values) {
        this(type, Arrays.asList(values));
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    Array(String type, List<T> values) {
        this((Class<T>) AbiTypes.getType(type), values);
    }

    @Deprecated
    Array(String type) {
        this(type, new ArrayList<>());
    }

    @SafeVarargs
    Array(Class<T> type, T... values) {
        this(type, Arrays.asList(values));
    }

    Array(Class<T> type, List<T> values) {
        checkValid(type, values);

        this.type = type;
        this.value = values;
    }

    @Override
    public List<T> getValue() {
        return value;
    }

    public Class<T> getComponentType() {
        return type;
    }

    @Override
    public abstract String getTypeAsString();

    private void checkValid(Class<T> type, List<T> values) {
        if (type == null) {
            throw new NullPointerException("No type provided");
        }

        if (values == null) {
            throw new NullPointerException("No list value provided");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Array<?> array = (Array<?>) o;

        if (!type.equals(array.type)) {
            return false;
        }
        return value != null ? value.equals(array.value) : array.value == null;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
