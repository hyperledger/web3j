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

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.web3j.crypto.ContractUtils.generateContractAddress;

public class ContractUtilsTest {

    @Test
    public void testCreateContractAddress() {
        final String address = "0x19e03255f667bdfd50a32722df860b1eeaf4d635";

        assertEquals(
                generateContractAddress(address, BigInteger.valueOf(209)),
                ("0xe41e694d8fa4337b7bffc7483d3609ae1ea068d5"));

        assertEquals(
                generateContractAddress(address, BigInteger.valueOf(257)),
                ("0x59c21d36fbe415218e834683cb6616f2bc971ca9"));
    }
}
