package org.web3j.utils.tuples.generated;

import org.web3j.utils.tuples.Tuple;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.TupleGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public final class Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> implements Tuple {
    private static final int SIZE = 15;

    private final T1 value1;

    private final T2 value2;

    private final T3 value3;

    private final T4 value4;

    private final T5 value5;

    private final T6 value6;

    private final T7 value7;

    private final T8 value8;

    private final T9 value9;

    private final T10 value10;

    private final T11 value11;

    private final T12 value12;

    private final T13 value13;

    private final T14 value14;

    private final T15 value15;

    public Tuple15(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, T6 value6, T7 value7, T8 value8, T9 value9, T10 value10, T11 value11, T12 value12, T13 value13, T14 value14, T15 value15) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.value9 = value9;
        this.value10 = value10;
        this.value11 = value11;
        this.value12 = value12;
        this.value13 = value13;
        this.value14 = value14;
        this.value15 = value15;
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

    public T6 getValue6() {
        return value6;
    }

    public T7 getValue7() {
        return value7;
    }

    public T8 getValue8() {
        return value8;
    }

    public T9 getValue9() {
        return value9;
    }

    public T10 getValue10() {
        return value10;
    }

    public T11 getValue11() {
        return value11;
    }

    public T12 getValue12() {
        return value12;
    }

    public T13 getValue13() {
        return value13;
    }

    public T14 getValue14() {
        return value14;
    }

    public T15 getValue15() {
        return value15;
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
        Tuple15<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple15 = (Tuple15<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
        if (value1 != null ? !value1.equals(tuple15.value1) : tuple15.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(tuple15.value2) : tuple15.value2 != null) {
            return false;
        }
        if (value3 != null ? !value3.equals(tuple15.value3) : tuple15.value3 != null) {
            return false;
        }
        if (value4 != null ? !value4.equals(tuple15.value4) : tuple15.value4 != null) {
            return false;
        }
        if (value5 != null ? !value5.equals(tuple15.value5) : tuple15.value5 != null) {
            return false;
        }
        if (value6 != null ? !value6.equals(tuple15.value6) : tuple15.value6 != null) {
            return false;
        }
        if (value7 != null ? !value7.equals(tuple15.value7) : tuple15.value7 != null) {
            return false;
        }
        if (value8 != null ? !value8.equals(tuple15.value8) : tuple15.value8 != null) {
            return false;
        }
        if (value9 != null ? !value9.equals(tuple15.value9) : tuple15.value9 != null) {
            return false;
        }
        if (value10 != null ? !value10.equals(tuple15.value10) : tuple15.value10 != null) {
            return false;
        }
        if (value11 != null ? !value11.equals(tuple15.value11) : tuple15.value11 != null) {
            return false;
        }
        if (value12 != null ? !value12.equals(tuple15.value12) : tuple15.value12 != null) {
            return false;
        }
        if (value13 != null ? !value13.equals(tuple15.value13) : tuple15.value13 != null) {
            return false;
        }
        if (value14 != null ? !value14.equals(tuple15.value14) : tuple15.value14 != null) {
            return false;
        }
        return value15 != null ? value15.equals(tuple15.value15) : tuple15.value15 == null;
    }

    @Override
    public int hashCode() {
        int result = value1.hashCode();
        result = 31 * result + (value2 != null ? value2.hashCode() : 0);
        result = 31 * result + (value3 != null ? value3.hashCode() : 0);
        result = 31 * result + (value4 != null ? value4.hashCode() : 0);
        result = 31 * result + (value5 != null ? value5.hashCode() : 0);
        result = 31 * result + (value6 != null ? value6.hashCode() : 0);
        result = 31 * result + (value7 != null ? value7.hashCode() : 0);
        result = 31 * result + (value8 != null ? value8.hashCode() : 0);
        result = 31 * result + (value9 != null ? value9.hashCode() : 0);
        result = 31 * result + (value10 != null ? value10.hashCode() : 0);
        result = 31 * result + (value11 != null ? value11.hashCode() : 0);
        result = 31 * result + (value12 != null ? value12.hashCode() : 0);
        result = 31 * result + (value13 != null ? value13.hashCode() : 0);
        result = 31 * result + (value14 != null ? value14.hashCode() : 0);
        result = 31 * result + (value15 != null ? value15.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple15{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", value3=" + value3 +
                ", value4=" + value4 +
                ", value5=" + value5 +
                ", value6=" + value6 +
                ", value7=" + value7 +
                ", value8=" + value8 +
                ", value9=" + value9 +
                ", value10=" + value10 +
                ", value11=" + value11 +
                ", value12=" + value12 +
                ", value13=" + value13 +
                ", value14=" + value14 +
                ", value15=" + value15 +
                "}";
    }
}
