package org.web3j.utils;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import org.web3j.protocol.exceptions.MessageDecodingException;
import org.web3j.protocol.exceptions.MessageEncodingException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class NumericTest {

    @Test
    public void testQuantityDecode() {
        assertThat(Numeric.decodeQuantity("0x0"), equalTo(BigInteger.valueOf(0L)));
        assertThat(Numeric.decodeQuantity("0x400"), equalTo(BigInteger.valueOf((1024L))));
        assertThat(Numeric.decodeQuantity("0x0"), equalTo(BigInteger.valueOf((0L))));
        assertThat(Numeric.decodeQuantity("0x7fffffffffffffff"), equalTo(BigInteger.valueOf((Long.MAX_VALUE))));
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeMissingPrefix() {
        Numeric.decodeQuantity("ff");
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeLeadingZero() {
        Numeric.decodeQuantity("0x0400");
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeMissingValue() {
        Numeric.decodeQuantity("0x");
    }

    @Test
    public void testQuantityEncode() {
        assertThat(Numeric.encodeQuantity(BigInteger.valueOf(0)), is("0x0"));
        assertThat(Numeric.encodeQuantity(BigInteger.valueOf(1)), is("0x1"));
        assertThat(Numeric.encodeQuantity(BigInteger.valueOf(1024)), is("0x400"));
        assertThat(Numeric.encodeQuantity(BigInteger.valueOf(Long.MAX_VALUE)), is("0x7fffffffffffffff"));
    }

    @Test(expected = MessageEncodingException.class)
    public void testQuantityEncodeNegative() {
        Numeric.encodeQuantity(BigInteger.valueOf(-1));
    }

    @Test
    public void testCleanHexPrefix() {
        Assert.assertThat(Numeric.cleanHexPrefix(""), is(""));
        Assert.assertThat(Numeric.cleanHexPrefix("0123456789abcdef"), is("0123456789abcdef"));
        Assert.assertThat(Numeric.cleanHexPrefix("0x"), is(""));
        Assert.assertThat(Numeric.cleanHexPrefix("0x0123456789abcdef"), is("0123456789abcdef"));
    }
}
