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
package org.web3j.rlp;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class RlpEncoderTest {

    /**
     * Examples taken from https://github.com/ethereum/wiki/wiki/RLP#examples.
     *
     * <p>For further examples see https://github.com/ethereum/tests/tree/develop/RLPTests.
     */
    @Test
    public void testEncode() {
        assertArrayEquals(
                RlpEncoder.encode(RlpString.create("dog")),
                (new byte[] {(byte) 0x83, 'd', 'o', 'g'}));

        assertArrayEquals(
                RlpEncoder.encode(new RlpList(RlpString.create("cat"), RlpString.create("dog"))),
                (new byte[] {(byte) 0xc8, (byte) 0x83, 'c', 'a', 't', (byte) 0x83, 'd', 'o', 'g'}));

        assertArrayEquals(RlpEncoder.encode(RlpString.create("")), (new byte[] {(byte) 0x80}));

        assertArrayEquals(
                RlpEncoder.encode(RlpString.create(new byte[] {})), (new byte[] {(byte) 0x80}));

        assertArrayEquals(RlpEncoder.encode(new RlpList()), (new byte[] {(byte) 0xc0}));

        assertArrayEquals(
                RlpEncoder.encode(RlpString.create(BigInteger.valueOf(0x0f))),
                (new byte[] {(byte) 0x0f}));

        assertArrayEquals(
                RlpEncoder.encode(RlpString.create(BigInteger.valueOf(0x0400))),
                (new byte[] {(byte) 0x82, (byte) 0x04, (byte) 0x00}));

        assertArrayEquals(
                RlpEncoder.encode(
                        new RlpList(
                                new RlpList(),
                                new RlpList(new RlpList()),
                                new RlpList(new RlpList(), new RlpList(new RlpList())))),
                (new byte[] {
                    (byte) 0xc7,
                    (byte) 0xc0,
                    (byte) 0xc1,
                    (byte) 0xc0,
                    (byte) 0xc3,
                    (byte) 0xc0,
                    (byte) 0xc1,
                    (byte) 0xc0
                }));

        assertArrayEquals(
                RlpEncoder.encode(
                        RlpString.create(
                                "Lorem ipsum dolor sit amet, consectetur adipisicing elit")),
                (new byte[] {
                    (byte) 0xb8,
                    (byte) 0x38,
                    'L',
                    'o',
                    'r',
                    'e',
                    'm',
                    ' ',
                    'i',
                    'p',
                    's',
                    'u',
                    'm',
                    ' ',
                    'd',
                    'o',
                    'l',
                    'o',
                    'r',
                    ' ',
                    's',
                    'i',
                    't',
                    ' ',
                    'a',
                    'm',
                    'e',
                    't',
                    ',',
                    ' ',
                    'c',
                    'o',
                    'n',
                    's',
                    'e',
                    'c',
                    't',
                    'e',
                    't',
                    'u',
                    'r',
                    ' ',
                    'a',
                    'd',
                    'i',
                    'p',
                    'i',
                    's',
                    'i',
                    'c',
                    'i',
                    'n',
                    'g',
                    ' ',
                    'e',
                    'l',
                    'i',
                    't'
                }));

        assertArrayEquals(
                RlpEncoder.encode(RlpString.create(BigInteger.ZERO)), (new byte[] {(byte) 0x80}));

        // https://github.com/paritytech/parity-common/blob/master/rlp/tests/tests.rs#L237
        assertArrayEquals(
                RlpEncoder.encode(RlpString.create(new byte[] {0})), (new byte[] {(byte) 0x00}));

        assertArrayEquals(
                RlpEncoder.encode(
                        new RlpList(
                                RlpString.create("zw"),
                                new RlpList(RlpString.create(4)),
                                RlpString.create(1))),
                (new byte[] {
                    (byte) 0xc6,
                    (byte) 0x82,
                    (byte) 0x7a,
                    (byte) 0x77,
                    (byte) 0xc1,
                    (byte) 0x04,
                    (byte) 0x01
                }));

        // 55 bytes. See https://github.com/web3j/web3j/issues/519
        byte[] encodeMe = new byte[55];
        Arrays.fill(encodeMe, (byte) 0);
        byte[] expectedEncoding = new byte[56];
        expectedEncoding[0] = (byte) 0xb7;
        System.arraycopy(encodeMe, 0, expectedEncoding, 1, encodeMe.length);
        assertArrayEquals(RlpEncoder.encode(RlpString.create(encodeMe)), (expectedEncoding));
    }
}
