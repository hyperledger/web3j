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
package org.web3j.abi;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes10;
import org.web3j.abi.datatypes.generated.Uint32;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultFunctionEncoderTest {

    @Test
    public void testBuildMethodId() {
        assertEquals("0xcdcd77c0", DefaultFunctionEncoder.buildMethodId("baz(uint32,bool)"));
    }

    @Test
    public void testBuildMessageSignature() {
        assertEquals(
                "baz(uint32,bool)",
                DefaultFunctionEncoder.buildMethodSignature(
                        "baz", Arrays.asList(new Uint32(BigInteger.valueOf(69)), new Bool(true))));
    }

    @Test
    public void testBuildEmptyMethodSignature() {
        assertEquals(
                "empty()",
                DefaultFunctionEncoder.buildMethodSignature("empty", Collections.emptyList()));
    }

    @Test
    public void testEncodeConstructorEmpty() {
        assertEquals("", FunctionEncoder.encodeConstructor(Collections.emptyList()));
    }

    @Test
    public void testEncodeConstructorString() {
        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000000020"
                        + "000000000000000000000000000000000000000000000000000000000000000a"
                        + "4772656574696e67732100000000000000000000000000000000000000000000",
                FunctionEncoder.encodeConstructor(
                        Collections.singletonList(new Utf8String("Greetings!"))));
    }

    @Test
    public void testEncodeConstructorUint() {
        assertEquals(
                "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000020",
                FunctionEncoder.encodeConstructor(
                        Arrays.asList(
                                new Uint(BigInteger.ONE), new Uint(BigInteger.valueOf(0x20)))));
    }

    @Test
    public void testFunctionSimpleEncode() {
        Function function =
                new Function(
                        "baz",
                        Arrays.asList(new Uint32(BigInteger.valueOf(69)), new Bool(true)),
                        Collections.emptyList());

        assertEquals(
                "0xcdcd77c0"
                        + "0000000000000000000000000000000000000000000000000000000000000045"
                        + "0000000000000000000000000000000000000000000000000000000000000001",
                FunctionEncoder.encode(function));
    }

    @Test
    public void testFunctionMDynamicArrayEncode1() {
        Function function =
                new Function(
                        "sam",
                        Arrays.asList(
                                new DynamicBytes("dave".getBytes()),
                                new Bool(true),
                                new DynamicArray<>(
                                        new Uint(BigInteger.ONE),
                                        new Uint(BigInteger.valueOf(2)),
                                        new Uint(BigInteger.valueOf(3)))),
                        Collections.emptyList());

        assertEquals(
                "0xa5643bf2"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "00000000000000000000000000000000000000000000000000000000000000a0"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6461766500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "0000000000000000000000000000000000000000000000000000000000000003",
                FunctionEncoder.encode(function));
    }

    @Test
    public void testFunctionMDynamicArrayEncode2() {
        Function function =
                new Function(
                        "f",
                        Arrays.asList(
                                new Uint(BigInteger.valueOf(0x123)),
                                new DynamicArray<>(
                                        new Uint32(BigInteger.valueOf(0x456)),
                                        new Uint32(BigInteger.valueOf(0x789))),
                                new Bytes10("1234567890".getBytes()),
                                new DynamicBytes("Hello, world!".getBytes())),
                        Collections.emptyList());

        assertEquals(
                "0x8be65246"
                        + "0000000000000000000000000000000000000000000000000000000000000123"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "3132333435363738393000000000000000000000000000000000000000000000"
                        + "00000000000000000000000000000000000000000000000000000000000000e0"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "0000000000000000000000000000000000000000000000000000000000000456"
                        + "0000000000000000000000000000000000000000000000000000000000000789"
                        + "000000000000000000000000000000000000000000000000000000000000000d"
                        + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000",
                FunctionEncoder.encode(function));
    }
}
