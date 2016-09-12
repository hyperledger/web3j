package org.web3j.protocol;

import java.math.BigInteger;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class UtilsTest {

    @Test
    public void testQuantityDecode() {
        assertThat(Utils.decodeQuantity("0x0"), equalTo(BigInteger.valueOf(0L)));
        assertThat(Utils.decodeQuantity("0x400"), equalTo(BigInteger.valueOf((1024L))));
        assertThat(Utils.decodeQuantity("0x0"), equalTo(BigInteger.valueOf((0L))));
        assertThat(Utils.decodeQuantity("0x7fffffffffffffff"), equalTo(BigInteger.valueOf((Long.MAX_VALUE))));
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeMissingPrefix() {
        Utils.decodeQuantity("ff");
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeLeadingZero() {
        Utils.decodeQuantity("0x0400");
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeMissingValue() {
        Utils.decodeQuantity("0x");
    }

    @Test
    public void testQuantityEncode() {
        assertThat(Utils.encodeQuantity(BigInteger.valueOf(0)), is("0x0"));
        assertThat(Utils.encodeQuantity(BigInteger.valueOf(1)), is("0x1"));
        assertThat(Utils.encodeQuantity(BigInteger.valueOf(1024)), is("0x400"));
        assertThat(Utils.encodeQuantity(BigInteger.valueOf(Long.MAX_VALUE)), is("0x7fffffffffffffff"));
    }

    @Test(expected = MessageEncodingException.class)
    public void testQuantityEncodeNegative() {
        Utils.encodeQuantity(BigInteger.valueOf(-1));
    }
}
