package org.web3j.abi.datatypes;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NumericTypeTest {

    @Test (expected = NullPointerException.class)
    public void testConstructorNullParameter1(){
        new NumericType(null, BigInteger.valueOf(41)) {};
    }

    @Test (expected = NullPointerException.class)
    public void testConstructorNullParameter2(){
        new NumericType("test", null) {};
    }

    @Test
    public void testToString() throws Exception {
        BigInteger expected = BigInteger.valueOf(41);
        NumericType numericType = new NumericType("test", expected) {};

        assertThat(numericType.toString(), is(expected.toString()));
    }

}