/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.web3j.exceptions.MessageDecodingException;
import org.web3j.exceptions.MessageEncodingException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.web3j.utils.Numeric.asByte;

public class NumericTest {

    private static final byte[] HEX_RANGE_ARRAY =
            new byte[] {
                asByte(0x0, 0x1),
                asByte(0x2, 0x3),
                asByte(0x4, 0x5),
                asByte(0x6, 0x7),
                asByte(0x8, 0x9),
                asByte(0xa, 0xb),
                asByte(0xc, 0xd),
                asByte(0xe, 0xf)
            };

    private static final String HEX_RANGE_STRING = "0x0123456789abcdef";

    @Test
    public void testQuantityEncodeLeadingZero() {
        assertEquals(Numeric.toHexStringWithPrefixSafe(BigInteger.valueOf(0L)), ("0x00"));
        assertEquals(Numeric.toHexStringWithPrefixSafe(BigInteger.valueOf(1024L)), ("0x400"));
        assertEquals(
                Numeric.toHexStringWithPrefixSafe(BigInteger.valueOf(Long.MAX_VALUE)),
                ("0x7fffffffffffffff"));
        assertEquals(
                Numeric.toHexStringWithPrefixSafe(
                        new BigInteger("204516877000845695339750056077105398031")),
                ("0x99dc848b94efc27edfad28def049810f"));
    }

    @Test
    public void testQuantityDecode() {
        assertEquals(Numeric.decodeQuantity("0x0"), (BigInteger.valueOf(0L)));
        assertEquals(Numeric.decodeQuantity("0x400"), (BigInteger.valueOf((1024L))));
        assertEquals(Numeric.decodeQuantity("0x0"), (BigInteger.valueOf((0L))));
        assertEquals(
                Numeric.decodeQuantity("0x7fffffffffffffff"),
                (BigInteger.valueOf((Long.MAX_VALUE))));
        assertEquals(
                Numeric.decodeQuantity("0x99dc848b94efc27edfad28def049810f"),
                (new BigInteger("204516877000845695339750056077105398031")));
    }

    @Test
    public void testQuantityDecodeLeadingZero() {
        assertEquals(Numeric.decodeQuantity("0x0400"), (BigInteger.valueOf(1024L)));
        assertEquals(Numeric.decodeQuantity("0x001"), (BigInteger.valueOf(1L)));
    }

    // If TestRpc resolves the following issue, we can reinstate this code
    // https://github.com/ethereumjs/testrpc/issues/220
    @Disabled
    @Test
    public void testQuantityDecodeLeadingZeroException() {

        assertThrows(MessageDecodingException.class, () -> Numeric.decodeQuantity("0x0400"));
    }

    @Test
    public void testQuantityDecodeMissingPrefix() {

        assertThrows(MessageDecodingException.class, () -> Numeric.decodeQuantity("ff"));
    }

    @Test
    public void testQuantityDecodeMissingValue() {

        assertThrows(MessageDecodingException.class, () -> Numeric.decodeQuantity("0x"));
    }

    @Test
    public void testQuantityEncode() {
        assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(0)), ("0x0"));
        assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(1)), ("0x1"));
        assertEquals(Numeric.encodeQuantity(BigInteger.valueOf(1024)), ("0x400"));
        assertEquals(
                Numeric.encodeQuantity(BigInteger.valueOf(Long.MAX_VALUE)), ("0x7fffffffffffffff"));
        assertEquals(
                Numeric.encodeQuantity(new BigInteger("204516877000845695339750056077105398031")),
                ("0x99dc848b94efc27edfad28def049810f"));
    }

    @Test
    public void testQuantityEncodeNegative() {

        assertThrows(
                MessageEncodingException.class,
                () -> Numeric.encodeQuantity(BigInteger.valueOf(-1)));
    }

    @Test
    public void testCleanHexPrefix() {
        assertEquals(Numeric.cleanHexPrefix(""), (""));
        assertEquals(Numeric.cleanHexPrefix("0123456789abcdef"), ("0123456789abcdef"));
        assertEquals(Numeric.cleanHexPrefix("0x"), (""));
        assertEquals(Numeric.cleanHexPrefix("0x0123456789abcdef"), ("0123456789abcdef"));
    }

    @Test
    public void testPrependHexPrefix() {
        assertEquals(Numeric.prependHexPrefix(""), ("0x"));
        assertEquals(Numeric.prependHexPrefix("0x0123456789abcdef"), ("0x0123456789abcdef"));
        assertEquals(Numeric.prependHexPrefix("0x"), ("0x"));
        assertEquals(Numeric.prependHexPrefix("0123456789abcdef"), ("0x0123456789abcdef"));
    }

    @Test
    public void testToHexStringWithPrefix() {
        assertEquals(Numeric.toHexStringWithPrefix(BigInteger.TEN), ("0xa"));
    }

    @Test
    public void testToHexStringNoPrefix() {
        assertEquals(Numeric.toHexStringNoPrefix(BigInteger.TEN), ("a"));
    }

    @Test
    public void testToBytesPadded() {
        assertArrayEquals(Numeric.toBytesPadded(BigInteger.TEN, 1), (new byte[] {0xa}));

        assertArrayEquals(
                Numeric.toBytesPadded(BigInteger.TEN, 8), (new byte[] {0, 0, 0, 0, 0, 0, 0, 0xa}));

        assertArrayEquals(
                Numeric.toBytesPadded(BigInteger.valueOf(Integer.MAX_VALUE), 4),
                (new byte[] {0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff}));
    }

    @Test
    public void testToBytesPaddedInvalid() {

        assertThrows(
                RuntimeException.class,
                () -> Numeric.toBytesPadded(BigInteger.valueOf(Long.MAX_VALUE), 7));
    }

    @Test
    public void testHexStringToByteArray() {
        assertArrayEquals(Numeric.hexStringToByteArray(""), (new byte[] {}));
        assertArrayEquals(Numeric.hexStringToByteArray("0"), (new byte[] {0}));
        assertArrayEquals(Numeric.hexStringToByteArray("1"), (new byte[] {0x1}));
        assertArrayEquals(Numeric.hexStringToByteArray(HEX_RANGE_STRING), (HEX_RANGE_ARRAY));

        assertArrayEquals(Numeric.hexStringToByteArray("0x123"), (new byte[] {0x1, 0x23}));
    }

    @Test
    public void testToHexString() {
        assertEquals(Numeric.toHexString(new byte[] {}), ("0x"));
        assertEquals(Numeric.toHexString(new byte[] {0x1}), ("0x01"));
        assertEquals(Numeric.toHexString(HEX_RANGE_ARRAY), (HEX_RANGE_STRING));
    }

    @Test
    public void testToHexStringNoPrefixZeroPadded() {
        assertEquals(Numeric.toHexStringNoPrefixZeroPadded(BigInteger.ZERO, 5), ("00000"));

        assertEquals(
                Numeric.toHexStringNoPrefixZeroPadded(
                        new BigInteger("11c52b08330e05d731e38c856c1043288f7d9744", 16), 40),
                ("11c52b08330e05d731e38c856c1043288f7d9744"));

        assertEquals(
                Numeric.toHexStringNoPrefixZeroPadded(
                        new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40),
                ("01c52b08330e05d731e38c856c1043288f7d9744"));
    }

    @Test
    public void testToHexStringWithPrefixZeroPadded() {
        assertEquals(Numeric.toHexStringWithPrefixZeroPadded(BigInteger.ZERO, 5), ("0x00000"));

        assertEquals(
                Numeric.toHexStringWithPrefixZeroPadded(
                        new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40),
                ("0x01c52b08330e05d731e38c856c1043288f7d9744"));

        assertEquals(
                Numeric.toHexStringWithPrefixZeroPadded(
                        new BigInteger("01c52b08330e05d731e38c856c1043288f7d9744", 16), 40),
                ("0x01c52b08330e05d731e38c856c1043288f7d9744"));
    }

    @Test
    public void testToHexStringZeroPaddedNegative() {

        assertThrows(
                UnsupportedOperationException.class,
                () -> Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(-1), 20));
    }

    @Test
    public void testToHexStringZeroPaddedTooLargs() {

        assertThrows(
                UnsupportedOperationException.class,
                () -> Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(-1), 5));
    }

    @Test
    public void testIsIntegerValue() {
        assertTrue(Numeric.isIntegerValue(BigDecimal.ZERO));
        assertTrue(Numeric.isIntegerValue(BigDecimal.ZERO));
        assertTrue(Numeric.isIntegerValue(BigDecimal.valueOf(Long.MAX_VALUE)));
        assertTrue(Numeric.isIntegerValue(BigDecimal.valueOf(Long.MIN_VALUE)));
        assertTrue(
                Numeric.isIntegerValue(
                        new BigDecimal(
                                "9999999999999999999999999999999999999999999999999999999999999999.0")));
        assertTrue(
                Numeric.isIntegerValue(
                        new BigDecimal(
                                "-9999999999999999999999999999999999999999999999999999999999999999.0")));

        assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(0.1)));
        assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(-0.1)));
        assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(1.1)));
        assertFalse(Numeric.isIntegerValue(BigDecimal.valueOf(-1.1)));
    }

    @Test
    public void testHandleNPE() {
        assertFalse(Numeric.containsHexPrefix(null));
        assertFalse(Numeric.containsHexPrefix(""));
    }
}
