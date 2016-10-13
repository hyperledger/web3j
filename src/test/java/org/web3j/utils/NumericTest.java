package org.web3j.utils;

import java.math.BigInteger;

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
        assertThat(Numeric.decodeQuantity(
                "0x7fffffffffffffff"), equalTo(BigInteger.valueOf((Long.MAX_VALUE))));
        assertThat(Numeric.decodeQuantity("0x99dc848b94efc27edfad28def049810f"),
                equalTo(new BigInteger("204516877000845695339750056077105398031")));
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
        assertThat(Numeric.encodeQuantity(
                BigInteger.valueOf(Long.MAX_VALUE)), is("0x7fffffffffffffff"));
        assertThat(Numeric.encodeQuantity(
                new BigInteger("204516877000845695339750056077105398031")),
                is("0x99dc848b94efc27edfad28def049810f"));
    }

    @Test(expected = MessageEncodingException.class)
    public void testQuantityEncodeNegative() {
        Numeric.encodeQuantity(BigInteger.valueOf(-1));
    }

    @Test
    public void testCleanHexPrefix() {
        assertThat(Numeric.cleanHexPrefix(""), is(""));
        assertThat(Numeric.cleanHexPrefix("0123456789abcdef"), is("0123456789abcdef"));
        assertThat(Numeric.cleanHexPrefix("0x"), is(""));
        assertThat(Numeric.cleanHexPrefix("0x0123456789abcdef"), is("0123456789abcdef"));
    }

    @Test
    public void testToHexStringWithPrefix() {
        assertThat(Numeric.toHexStringWithPrefix(BigInteger.TEN), is("0xa"));
    }

    @Test
    public void testToHexStringNoPrefix() {
        assertThat(Numeric.toHexStringNoPrefix(BigInteger.TEN), is("a"));
    }

    @Test
    public void testToBytesPadded() {
        assertThat(Numeric.toBytesPadded(BigInteger.TEN, 1),
                is(new byte[] { 0xa }));

        assertThat(Numeric.toBytesPadded(BigInteger.TEN, 8),
                is(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0xa }));

        assertThat(Numeric.toBytesPadded(BigInteger.valueOf(Integer.MAX_VALUE), 4),
                is(new byte[] { 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff }));
    }

    @Test(expected = RuntimeException.class)
    public void testToBytesPaddedInvalid() {
        Numeric.toBytesPadded(BigInteger.valueOf(Long.MAX_VALUE), 7);
    }
}
