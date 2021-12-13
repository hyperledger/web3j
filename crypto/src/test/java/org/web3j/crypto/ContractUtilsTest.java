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
import static org.web3j.crypto.ContractUtils.generateCreate2ContractAddress;
import static org.web3j.utils.Numeric.hexStringToByteArray;

public class ContractUtilsTest {

    @Test
    public void testCreateContractAddress() {
        String address = "0x19e03255f667bdfd50a32722df860b1eeaf4d635";

        assertEquals(
                generateContractAddress(address, BigInteger.valueOf(209)),
                ("0xe41e694d8fa4337b7bffc7483d3609ae1ea068d5"));

        assertEquals(
                generateContractAddress(address, BigInteger.valueOf(257)),
                ("0x59c21d36fbe415218e834683cb6616f2bc971ca9"));
    }

    @Test
    public void testEIP1014Create2ContractAddress() {
        // https://eips.ethereum.org/EIPS/eip-1014
        // example 0
        assertEquals(
                Keys.toChecksumAddress(
                        generateCreate2ContractAddress(
                                "0x0000000000000000000000000000000000000000",
                                hexStringToByteArray(
                                        "0x0000000000000000000000000000000000000000000000000000000000000000"),
                                hexStringToByteArray("0x00"))),
                ("0x4D1A2e2bB4F88F0250f26Ffff098B0b30B26BF38"));

        // example 1
        assertEquals(
                Keys.toChecksumAddress(
                        generateCreate2ContractAddress(
                                "0xdeadbeef00000000000000000000000000000000",
                                hexStringToByteArray(
                                        "0x0000000000000000000000000000000000000000000000000000000000000000"),
                                hexStringToByteArray("0x00"))),
                ("0xB928f69Bb1D91Cd65274e3c79d8986362984fDA3"));

        // example 2
        assertEquals(
                Keys.toChecksumAddress(
                        generateCreate2ContractAddress(
                                "0xdeadbeef00000000000000000000000000000000",
                                hexStringToByteArray(
                                        "0x000000000000000000000000feed000000000000000000000000000000000000"),
                                hexStringToByteArray("0x00"))),
                ("0xD04116cDd17beBE565EB2422F2497E06cC1C9833"));

        // example 3
        assertEquals(
                Keys.toChecksumAddress(
                        generateCreate2ContractAddress(
                                "0x0000000000000000000000000000000000000000",
                                hexStringToByteArray(
                                        "0x0000000000000000000000000000000000000000000000000000000000000000"),
                                hexStringToByteArray("0xdeadbeef"))),
                ("0x70f2b2914A2a4b783FaEFb75f459A580616Fcb5e"));

        // example 4
        assertEquals(
                Keys.toChecksumAddress(
                        generateCreate2ContractAddress(
                                "0x00000000000000000000000000000000deadbeef",
                                hexStringToByteArray(
                                        "0x00000000000000000000000000000000000000000000000000000000cafebabe"),
                                hexStringToByteArray("0xdeadbeef"))),
                ("0x60f3f640a8508fC6a86d45DF051962668E1e8AC7"));

        // example 5
        assertEquals(
                Keys.toChecksumAddress(
                        generateCreate2ContractAddress(
                                "0x00000000000000000000000000000000deadbeef",
                                hexStringToByteArray(
                                        "0x00000000000000000000000000000000000000000000000000000000cafebabe"),
                                hexStringToByteArray(
                                        "0xdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeef"))),
                ("0x1d8bfDC5D46DC4f61D6b6115972536eBE6A8854C"));

        // example 6
        assertEquals(
                Keys.toChecksumAddress(
                        generateCreate2ContractAddress(
                                "0x0000000000000000000000000000000000000000",
                                hexStringToByteArray(
                                        "0x0000000000000000000000000000000000000000000000000000000000000000"),
                                hexStringToByteArray("0x"))),
                ("0xE33C0C7F7df4809055C3ebA6c09CFe4BaF1BD9e0"));
    }
}
