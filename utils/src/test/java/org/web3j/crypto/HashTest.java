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
package org.web3j.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.web3j.utils.Numeric.asByte;

public class HashTest {

    @Test
    public void testSha3() {
        byte[] input =
                new byte[] {
                    asByte(0x6, 0x8),
                    asByte(0x6, 0x5),
                    asByte(0x6, 0xc),
                    asByte(0x6, 0xc),
                    asByte(0x6, 0xf),
                    asByte(0x2, 0x0),
                    asByte(0x7, 0x7),
                    asByte(0x6, 0xf),
                    asByte(0x7, 0x2),
                    asByte(0x6, 0xc),
                    asByte(0x6, 0x4)
                };

        byte[] expected =
                new byte[] {
                    asByte(0x4, 0x7),
                    asByte(0x1, 0x7),
                    asByte(0x3, 0x2),
                    asByte(0x8, 0x5),
                    asByte(0xa, 0x8),
                    asByte(0xd, 0x7),
                    asByte(0x3, 0x4),
                    asByte(0x1, 0xe),
                    asByte(0x5, 0xe),
                    asByte(0x9, 0x7),
                    asByte(0x2, 0xf),
                    asByte(0xc, 0x6),
                    asByte(0x7, 0x7),
                    asByte(0x2, 0x8),
                    asByte(0x6, 0x3),
                    asByte(0x8, 0x4),
                    asByte(0xf, 0x8),
                    asByte(0x0, 0x2),
                    asByte(0xf, 0x8),
                    asByte(0xe, 0xf),
                    asByte(0x4, 0x2),
                    asByte(0xa, 0x5),
                    asByte(0xe, 0xc),
                    asByte(0x5, 0xf),
                    asByte(0x0, 0x3),
                    asByte(0xb, 0xb),
                    asByte(0xf, 0xa),
                    asByte(0x2, 0x5),
                    asByte(0x4, 0xc),
                    asByte(0xb, 0x0),
                    asByte(0x1, 0xf),
                    asByte(0xa, 0xd)
                };

        byte[] result = Hash.sha3(input);
        assertEquals(result, (expected));
    }

    @Test
    public void testSha3HashHex() {
        assertEquals(
                Hash.sha3(""),
                ("0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470"));

        assertEquals(
                Hash.sha3("68656c6c6f20776f726c64"),
                ("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
    }

    @Test
    public void testSha3String() {
        assertEquals(
                Hash.sha3String(""),
                ("0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470"));

        assertEquals(
                Hash.sha3String("EVWithdraw(address,uint256,bytes32)"),
                ("0x953d0c27f84a9649b0e121099ffa9aeb7ed83e65eaed41d3627f895790c72d41"));
    }

    @Test
    public void testByte() {
        assertEquals(asByte(0x0, 0x0), ((byte) 0x0));
        assertEquals(asByte(0x1, 0x0), ((byte) 0x10));
        assertEquals(asByte(0xf, 0xf), ((byte) 0xff));
        assertEquals(asByte(0xc, 0x5), ((byte) 0xc5));
    }
}
