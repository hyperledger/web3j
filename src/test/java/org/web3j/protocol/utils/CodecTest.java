package org.web3j.protocol.utils;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import org.web3j.crypto.HexUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class CodecTest {

    @Test
    public void testQuantityDecode() {
        assertThat(Codec.decodeQuantity("0x0"), equalTo(BigInteger.valueOf(0L)));
        assertThat(Codec.decodeQuantity("0x400"), equalTo(BigInteger.valueOf((1024L))));
        assertThat(Codec.decodeQuantity("0x0"), equalTo(BigInteger.valueOf((0L))));
        assertThat(Codec.decodeQuantity("0x7fffffffffffffff"), equalTo(BigInteger.valueOf((Long.MAX_VALUE))));
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeMissingPrefix() {
        Codec.decodeQuantity("ff");
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeLeadingZero() {
        Codec.decodeQuantity("0x0400");
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeMissingValue() {
        Codec.decodeQuantity("0x");
    }

    @Test
    public void testQuantityEncode() {
        assertThat(Codec.encodeQuantity(BigInteger.valueOf(0)), is("0x0"));
        assertThat(Codec.encodeQuantity(BigInteger.valueOf(1)), is("0x1"));
        assertThat(Codec.encodeQuantity(BigInteger.valueOf(1024)), is("0x400"));
        assertThat(Codec.encodeQuantity(BigInteger.valueOf(Long.MAX_VALUE)), is("0x7fffffffffffffff"));
    }

    @Test(expected = MessageEncodingException.class)
    public void testQuantityEncodeNegative() {
        Codec.encodeQuantity(BigInteger.valueOf(-1));
    }

    @Test
    public void testCleanHexPrefix() {
        Assert.assertThat(Codec.cleanHexPrefix(""), is(""));
        Assert.assertThat(Codec.cleanHexPrefix("0123456789abcdef"), is("0123456789abcdef"));
        Assert.assertThat(Codec.cleanHexPrefix("0x"), is(""));
        Assert.assertThat(Codec.cleanHexPrefix("0x0123456789abcdef"), is("0123456789abcdef"));
    }
}
