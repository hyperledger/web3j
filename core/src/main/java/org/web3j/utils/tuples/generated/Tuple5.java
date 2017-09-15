package org.web3j.utils.tuples.generated;

import org.web3j.codegen.TupleGenerator;
import org.web3j.utils.tuples.Tuple;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use {@link TupleGenerator} to update.
 */
public final class Tuple5<T1, T2, T3, T4, T5> implements Tuple {
    private static final int SIZE = 5;

    private final T1 value1;

    private final T2 value2;

    private final T3 value3;

    private final T4 value4;

    private final T5 value5;

    public Tuple5(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
    }

    public T1 getValue1() {
        return value1;
    }

    public T2 getValue2() {
        return value2;
    }

    public T3 getValue3() {
        return value3;
    }

    public T4 getValue4() {
        return value4;
    }

    public T5 getValue5() {
        return value5;
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
        Tuple5<?, ?, ?, ?, ?> tuple5 = (Tuple5<?, ?, ?, ?, ?>) o;
        if (value1 != null ? !value1.equals(tuple5.value1) : tuple5.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(tuple5.value2) : tuple5.value2 != null) {
            return false;
        }
        if (value3 != null ? !value3.equals(tuple5.value3) : tuple5.value3 != null) {
            return false;
        }
        if (value4 != null ? !value4.equals(tuple5.value4) : tuple5.value4 != null) {
            return false;
        }
        return value5 != null ? value5.equals(tuple5.value5) : tuple5.value5 == null;
    }

    @Override
    public int hashCode() {
        int result = value1.hashCode();
        result = 31 * result + (value2 != null ? value2.hashCode() : 0);
        result = 31 * result + (value3 != null ? value3.hashCode() : 0);
        result = 31 * result + (value4 != null ? value4.hashCode() : 0);
        result = 31 * result + (value5 != null ? value5.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple5{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", value3=" + value3 +
                ", value4=" + value4 +
                ", value5=" + value5 +
                "}";
    }
}
