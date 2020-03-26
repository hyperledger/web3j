package org.web3j.tuples.generated;

import org.web3j.tuples.Tuple;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.TupleGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public final class Tuple6<T1, T2, T3, T4, T5, T6> implements Tuple {
    private static final int SIZE = 6;

    private final T1 value1;

    private final T2 value2;

    private final T3 value3;

    private final T4 value4;

    private final T5 value5;

    private final T6 value6;

    public Tuple6(final T1 value1, final T2 value2, final T3 value3, final T4 value4, final T5 value5, final T6 value6) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
    }

    /**
     * @deprecated use 'component1' method instead
     * @return the return value
     * */
    @Deprecated
    public T1 getValue1() {
        return value1;
    }

    public T1 component1() {
        return value1;
    }

    /**
     * @deprecated use 'component2' method instead
     * @return the return value
     * */
    @Deprecated
    public T2 getValue2() {
        return value2;
    }

    public T2 component2() {
        return value2;
    }

    /**
     * @deprecated use 'component3' method instead
     * @return the return value
     * */
    @Deprecated
    public T3 getValue3() {
        return value3;
    }

    public T3 component3() {
        return value3;
    }

    /**
     * @deprecated use 'component4' method instead
     * @return the return value
     * */
    @Deprecated
    public T4 getValue4() {
        return value4;
    }

    public T4 component4() {
        return value4;
    }

    /**
     * @deprecated use 'component5' method instead
     * @return the return value
     * */
    @Deprecated
    public T5 getValue5() {
        return value5;
    }

    public T5 component5() {
        return value5;
    }

    /**
     * @deprecated use 'component6' method instead
     * @return the return value
     * */
    @Deprecated
    public T6 getValue6() {
        return value6;
    }

    public T6 component6() {
        return value6;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Tuple6<?, ?, ?, ?, ?, ?> tuple6 = (Tuple6<?, ?, ?, ?, ?, ?>) o;
        if (value1 != null ? !value1.equals(tuple6.value1) : tuple6.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(tuple6.value2) : tuple6.value2 != null) {
            return false;
        }
        if (value3 != null ? !value3.equals(tuple6.value3) : tuple6.value3 != null) {
            return false;
        }
        if (value4 != null ? !value4.equals(tuple6.value4) : tuple6.value4 != null) {
            return false;
        }
        if (value5 != null ? !value5.equals(tuple6.value5) : tuple6.value5 != null) {
            return false;
        }
        return value6 != null ? value6.equals(tuple6.value6) : tuple6.value6 == null;
    }

    @Override
    public int hashCode() {
        int result = value1.hashCode();
        result = 31 * result + (value2 != null ? value2.hashCode() : 0);
        result = 31 * result + (value3 != null ? value3.hashCode() : 0);
        result = 31 * result + (value4 != null ? value4.hashCode() : 0);
        result = 31 * result + (value5 != null ? value5.hashCode() : 0);
        result = 31 * result + (value6 != null ? value6.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple6{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", value3=" + value3 +
                ", value4=" + value4 +
                ", value5=" + value5 +
                ", value6=" + value6 +
                "}";
    }
}
