package org.web3j.abi.datatypes;

import java.util.Arrays;
import java.util.List;

import org.web3j.abi.datatypes.generated.AbiTypes;

/**
 * Static array type.
 */
public class StaticArray<T extends Type> extends Array<T> {

    /**
     * Warning: increasing this constant will cause more generated StaticArrayN types, see:
     * org.web3j.codegen.AbiTypesGenerator#generateStaticArrayTypes
     */
    public static int MAX_SIZE_OF_STATIC_ARRAY = 32;

    private final Integer expectedSize;

    @Deprecated
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public StaticArray(T... values) {
        super((Class<T>) AbiTypes.getType(values[0].getTypeAsString()), values);
        this.expectedSize = null;
        isValid();
    }

    @Deprecated
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public StaticArray(int expectedSize, T... values) {
        super((Class<T>) AbiTypes.getType(values[0].getTypeAsString()), values);
        this.expectedSize = expectedSize;
        isValid();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public StaticArray(List<T> values) {
        super((Class<T>) AbiTypes.getType(values.get(0).getTypeAsString()), values);
        this.expectedSize = null;
        isValid();
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public StaticArray(int expectedSize, List<T> values) {
        super((Class<T>) AbiTypes.getType(values.get(0).getTypeAsString()), values);
        this.expectedSize = expectedSize;
        isValid();
    }

    @SafeVarargs
    public StaticArray(Class<T> type, T... values) {
       this(type, Arrays.asList(values));
    }

    @SafeVarargs
    public StaticArray(Class<T> type, int expectedSize, T... values) {
        this(type, expectedSize, Arrays.asList(values));
    }

    public StaticArray(Class<T> type, List<T> values) {
        super(type, values);
        this.expectedSize = null;
        isValid();
    }

    public StaticArray(Class<T> type, int expectedSize, List<T> values) {
        super(type, values);
        this.expectedSize = expectedSize;
        isValid();
    }

    @Override
    public String getTypeAsString() {
        return AbiTypes.getTypeAString(getComponentType()) + "[" + value.size() + "]";
    }

    private void isValid() {
        MAX_SIZE_OF_STATIC_ARRAY = 32;
        if (expectedSize == null && value.size() > MAX_SIZE_OF_STATIC_ARRAY) {
            throw new UnsupportedOperationException(
                    "Static arrays with a length greater than 32 are not supported.");
        } else if (expectedSize != null && value.size() != expectedSize) {
            throw new UnsupportedOperationException(
                    "Expected array of type [" + getClass().getSimpleName() + "] to have ["
                            + expectedSize + "] elements.");
        }
    }
}
