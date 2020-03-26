package org.web3j.tuples.generated;

import org.web3j.tuples.Tuple;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.web3j.codegen.TupleGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public final class Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> implements Tuple {
    private static final int SIZE = 10;

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

    public Tuple10(final T1 value1, final T2 value2, final T3 value3, final T4 value4, final T5 value5, final T6 value6, final T7 value7, final T8 value8, final T9 value9, final T10 value10) {
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
    }

    /**
     * @deprecated use 'component1' method instead
     * @return returns a value
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
     * @return returns a value
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
     * @return returns a value
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
     * @return returns a value
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
     * @return returns a value
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
     * @return returns a value
     * */
    @Deprecated
    public T6 getValue6() {
        return value6;
    }

    public T6 component6() {
        return value6;
    }

    /**
     * @deprecated use 'component7' method instead
     * @return returns a value
     * */
    @Deprecated
    public T7 getValue7() {
        return value7;
    }

    public T7 component7() {
        return value7;
    }

    /**
     * @deprecated use 'component8' method instead
     * @return  returns a value
     * */
    @Deprecated
    public T8 getValue8() {
        return value8;
    }

    public T8 component8() {
        return value8;
    }

    /**
     * @deprecated use 'component9' method instead
	 * @return a value */
    @Deprecated
    public T9 getValue9() {
        return value9;
    }

    public T9 component9() {
        return value9;
    }

    /**
     * @deprecated use 'component10' method instead
     * @return returns a value
     * */
    @Deprecated
    public T10 getValue10() {
        return value10;
    }

    public T10 component10() {
        return value10;
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
        final Tuple10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple10 = (Tuple10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
        if (value1 != null ? !value1.equals(tuple10.value1) : tuple10.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(tuple10.value2) : tuple10.value2 != null) {
            return false;
        }
        if (value3 != null ? !value3.equals(tuple10.value3) : tuple10.value3 != null) {
            return false;
        }
        if (value4 != null ? !value4.equals(tuple10.value4) : tuple10.value4 != null) {
            return false;
        }
        if (value5 != null ? !value5.equals(tuple10.value5) : tuple10.value5 != null) {
            return false;
        }
        if (value6 != null ? !value6.equals(tuple10.value6) : tuple10.value6 != null) {
            return false;
        }
        if (value7 != null ? !value7.equals(tuple10.value7) : tuple10.value7 != null) {
            return false;
        }
        if (value8 != null ? !value8.equals(tuple10.value8) : tuple10.value8 != null) {
            return false;
        }
        if (value9 != null ? !value9.equals(tuple10.value9) : tuple10.value9 != null) {
            return false;
        }
        return value10 != null ? value10.equals(tuple10.value10) : tuple10.value10 == null;
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
        return result;
    }

    @Override
    public String toString() {
        return "Tuple10{" +
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
                "}";
    }
}
