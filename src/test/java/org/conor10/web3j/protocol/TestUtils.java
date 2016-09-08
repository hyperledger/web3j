package org.conor10.web3j.protocol;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class TestUtils {

    @Test
    public void testQuantityDecode() {
        assertThat(Utils.decodeQuantity("0x0"), is(0));
        assertThat(Utils.decodeQuantity("0x400"), is(1024));
        assertThat(Utils.decodeQuantity("0x0"), is(0));
        assertThat(Utils.decodeQuantity("0xfffffc00"), is(-1024));
        assertThat(Utils.decodeQuantity("0x7fffffff"), is(Integer.MAX_VALUE));
        assertThat(Utils.decodeQuantity("0x80000000"), is(Integer.MIN_VALUE));
        assertThat(Utils.decodeQuantity("0xffffffff"), is(-1));
    }

    @Test(expected = MessageEncodingException.class)
    public void testQuantityDecodeMissingPrefix() {
        Utils.decodeQuantity("ff");
    }

    @Test(expected = MessageEncodingException.class)
    public void testQuantityDecodeLeadingZero() {
        Utils.decodeQuantity("0x0400");
    }

    @Test(expected = MessageEncodingException.class)
    public void testQuantityDecodeMissingValue() {
        Utils.decodeQuantity("0x");
    }

    @Test
    public void testQuantityEncode() {
        assertThat(Utils.encodeQuantity(0), is("0x0"));
        assertThat(Utils.encodeQuantity(1), is("0x1"));
        assertThat(Utils.encodeQuantity(-1), is("0xffffffff"));
        assertThat(Utils.encodeQuantity(1024), is("0x400"));
        assertThat(Utils.encodeQuantity(-1024), is("0xfffffc00"));
        assertThat(Utils.encodeQuantity(Integer.MAX_VALUE), is("0x7fffffff"));
        assertThat(Utils.encodeQuantity(Integer.MIN_VALUE), is("0x80000000"));
    }
}
