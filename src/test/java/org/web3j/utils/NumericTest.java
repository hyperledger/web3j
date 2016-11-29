package org.web3j.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import org.web3j.crypto.Keys;
import org.web3j.protocol.exceptions.MessageDecodingException;
import org.web3j.protocol.exceptions.MessageEncodingException;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.web3j.utils.Numeric.b;


public class NumericTest {

    private final byte[] HEX_RANGE_ARRAY = new byte[] {
            b(0x0, 0x1),
            b(0x2, 0x3),
            b(0x4, 0x5),
            b(0x6, 0x7),
            b(0x8, 0x9),
            b(0xa, 0xb),
            b(0xc, 0xd),
            b(0xe, 0xf)
    };

    private String HEX_RANGE_STRING = "0x0123456789abcdef";


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

    @Test
    public void testQuantityDecodeLeadingZero() {
        assertThat(Numeric.decodeQuantity("0x0400"), equalTo(BigInteger.valueOf(1024L)));
        assertThat(Numeric.decodeQuantity("0x001"), equalTo(BigInteger.valueOf(1L)));
    }

    @Test(expected = MessageDecodingException.class)
    public void testQuantityDecodeMissingPrefix() {
        Numeric.decodeQuantity("ff");
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
    public void testPrependHexPrefix() {
        assertThat(Numeric.prependHexPrefix(""), is("0x"));
        assertThat(Numeric.prependHexPrefix("0x0123456789abcdef"), is("0x0123456789abcdef"));
        assertThat(Numeric.prependHexPrefix("0x"), is("0x"));
        assertThat(Numeric.prependHexPrefix("0123456789abcdef"), is("0x0123456789abcdef"));
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

    @Test
    public void testHexStringToByteArray() {
        assertThat(Numeric.hexStringToByteArray(""), is(new byte[] { }));
        assertThat(Numeric.hexStringToByteArray("0"), is(new byte[] { 0 }));
        assertThat(Numeric.hexStringToByteArray("1"), is(new byte[] { 0x1 }));
        assertThat(Numeric.hexStringToByteArray(HEX_RANGE_STRING),
                is(HEX_RANGE_ARRAY));

        assertThat(Numeric.hexStringToByteArray("0x123"),
                is(new byte[] { 0x1, 0x23 }));
    }

    @Test
    public void testToHexString() {
        assertThat(Numeric.toHexString(new byte[] {}), is("0x"));
        assertThat(Numeric.toHexString(new byte[] { 0x1 }), is("0x01"));
        assertThat(Numeric.toHexString(HEX_RANGE_ARRAY), is(HEX_RANGE_STRING));
    }

    @Test
    public void testToHexStringNoPrefixZeroPadded() {
        assertThat(
                Numeric.toHexStringNoPrefixZeroPadded(
                        BigInteger.ZERO,
                        5),
                is("00000"));

        assertThat(
                Numeric.toHexStringNoPrefixZeroPadded(
                        new BigInteger("11c52b08330e05d731e38c856c1043288f7d9744", 16),
                        Keys.ADDRESS_LENGTH_IN_HEX),
                is("11c52b08330e05d731e38c856c1043288f7d9744"));

        assertThat(
                Numeric.toHexStringNoPrefixZeroPadded(
                        new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16),
                        Keys.ADDRESS_LENGTH_IN_HEX),
                is("01c52b08330e05d731e38c856c1043288f7d9744"));
    }

    @Test
    public void testToHexStringWithPrefixZeroPadded() {
        assertThat(
                Numeric.toHexStringWithPrefixZeroPadded(
                        BigInteger.ZERO,
                        5),
                is("0x00000"));

        assertThat(
                Numeric.toHexStringWithPrefixZeroPadded(
                        new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16),
                        Keys.ADDRESS_LENGTH_IN_HEX),
                is("0x01c52b08330e05d731e38c856c1043288f7d9744"));

        assertThat(
                Numeric.toHexStringWithPrefixZeroPadded(
                        new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16),
                        Keys.ADDRESS_LENGTH_IN_HEX),
                is("0x01c52b08330e05d731e38c856c1043288f7d9744"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToHexStringZeroPaddedNegative() {
        Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(-1), 20);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testToHexStringZeroPaddedTooLargs() {
        Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(-1), 5);
    }

    @Test
    public void testIsIntegerValue() {
        assertTrue(Numeric.isIntegerValue(BigDecimal.ZERO));
        assertTrue(Numeric.isIntegerValue(BigDecimal.ZERO));
        assertTrue(Numeric.isIntegerValue(BigDecimal.valueOf(Long.MAX_VALUE)));
        assertTrue(Numeric.isIntegerValue(BigDecimal.valueOf(Long.MIN_VALUE)));
        assertTrue(Numeric.isIntegerValue(new BigDecimal(
                "9999999999999999999999999999999999999999999999999999999999999999.0")));
        assertTrue(Numeric.isIntegerValue(new BigDecimal(
                "-9999999999999999999999999999999999999999999999999999999999999999.0")));

        assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(0.1)));
        assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(-0.1)));
        assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(1.1)));
        assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(-1.1)));
    }
}
