package org.web3j.abi.datatypes;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class Utf8StringTest {

    @Test (expected = NullPointerException.class)
    public void testConstructorNullParameter() {
        new Utf8String(null);
    }

    @Test
    public void testToString() {
        String expected = "web3j";
        Utf8String utf8String = new Utf8String(expected);
        assertThat(utf8String.toString(), is(expected));
    }

}