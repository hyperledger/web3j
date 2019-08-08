package org.web3j.tuples.generated;

import org.web3j.tuples.Tuple;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.TupleGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public final class Tuple3<T1, T2, T3> implements Tuple {
    private static final int SIZE = 3;

    private final T1 value1;

    private final T2 value2;

    private final T3 value3;

    public Tuple3(T1 value1, T2 value2, T3 value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
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

    /**
     * @deprecated use 'component2' method instead */
    @Deprecated
    public T2 getValue2() {
        return value2;
    }

    public T2 component2() {
        return value2;
    }

    /**
     * @deprecated use 'component3' method instead */
    @Deprecated
    public T3 getValue3() {
        return value3;
    }

    public T3 component3() {
        return value3;
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
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        if (value1 != null ? !value1.equals(tuple3.value1) : tuple3.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(tuple3.value2) : tuple3.value2 != null) {
            return false;
        }
        return value3 != null ? value3.equals(tuple3.value3) : tuple3.value3 == null;
    }

    @Override
    public int hashCode() {
        int result = value1.hashCode();
        result = 31 * result + (value2 != null ? value2.hashCode() : 0);
        result = 31 * result + (value3 != null ? value3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple3{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", value3=" + value3 +
                "}";
    }
}
