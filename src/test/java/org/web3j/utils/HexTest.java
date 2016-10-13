package org.web3j.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.web3j.utils.Hex.b;

public class HexTest {

    private final byte[] hexRangeArray = new byte[] {
            b(0x0, 0x1),
            b(0x2, 0x3),
            b(0x4, 0x5),
            b(0x6, 0x7),
            b(0x8, 0x9),
            b(0xa, 0xb),
            b(0xc, 0xd),
            b(0xe, 0xf)
    };

    private String hexRangeString = "0x0123456789abcdef";

    @Test
    public void testHexStringToByteArray() {
        assertThat(Hex.hexStringToByteArray(""), is(new byte[] { }));
        assertThat(Hex.hexStringToByteArray("0"), is(new byte[] { 0 }));
        assertThat(Hex.hexStringToByteArray("1"), is(new byte[] { 0x1 }));
        assertThat(Hex.hexStringToByteArray(hexRangeString),
                is(hexRangeArray));

        assertThat(Hex.hexStringToByteArray("0x123"),
                is(new byte[] { 0x1, 0x23 }));
    }

    @Test
    public void testToHexString() {
        assertThat(Hex.toHexString(new byte[] {}), is("0x"));
        assertThat(Hex.toHexString(new byte[] { 0x1 }), is("0x01"));
        assertThat(Hex.toHexString(hexRangeArray), is(hexRangeString));
    }
}
