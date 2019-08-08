package org.web3j.tuples.generated;

import org.web3j.tuples.Tuple;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.TupleGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public final class Tuple1<T1> implements Tuple {
    private static final int SIZE = 1;

    private final T1 value1;

    public Tuple1(T1 value1) {
        this.value1 = value1;
    }

    /**
     * @deprecated use 'component1' method instead */
    @Deprecated
    public T1 getValue1() {
        return value1;
    }

    public T1 component1() {
        return value1;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tuple1<?> tuple1 = (Tuple1<?>) o;
        return value1 != null ? value1.equals(tuple1.value1) : tuple1.value1 == null;
    }

    @Override
    public int hashCode() {
        int result = value1.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Tuple1{" +
                "value1=" + value1 +
                "}";
    }
}
