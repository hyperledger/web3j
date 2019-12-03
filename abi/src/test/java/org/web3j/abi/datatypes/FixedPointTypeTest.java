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
package org.web3j.abi.datatypes;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixedPointTypeTest {

    @Test
    public void testConvert() {
        assertEquals(
                FixedPointType.convert(BigInteger.valueOf(0x2), BigInteger.valueOf(0x2)),
                (new BigInteger("220000000000000000000000000000000", 16)));

        assertEquals(
                FixedPointType.convert(BigInteger.valueOf(0x8), BigInteger.valueOf(0x8)),
                (new BigInteger("880000000000000000000000000000000", 16)));

        assertEquals(
                FixedPointType.convert(BigInteger.valueOf(0xAAFF), BigInteger.valueOf(0x1111)),
                (new BigInteger("AAFF11110000000000000000000000000000", 16)));
    }
}
