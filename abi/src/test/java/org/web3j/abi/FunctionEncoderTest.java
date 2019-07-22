/*
 * Copyright 2019 Web3 Labs LTD.
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

import org.junit.Test;

import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes10;
import org.web3j.abi.datatypes.generated.Uint32;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FunctionEncoderTest {

    @Test
    public void testBuildMethodId() {
        assertThat(FunctionEncoder.buildMethodId("baz(uint32,bool)"), is("0xcdcd77c0"));
    }

    @Test
    public void testBuildMessageSignature() {
        assertThat(
                FunctionEncoder.buildMethodSignature(
                        "baz", Arrays.asList(new Uint32(BigInteger.valueOf(69)), new Bool(true))),
                is("baz(uint32,bool)"));
    }

    @Test
    public void testBuildEmptyMethodSignature() {
        assertThat(
                FunctionEncoder.buildMethodSignature("empty", Collections.emptyList()),
                is("empty()"));
    }

    @Test
    public void testEncodeConstructorEmpty() {
        assertThat(FunctionEncoder.encodeConstructor(Collections.emptyList()), is(""));
    }

    @Test
    public void testEncodeConstructorString() {
        assertThat(
                FunctionEncoder.encodeConstructor(
                        Collections.singletonList(new Utf8String("Greetings!"))),
                is(
                        "0000000000000000000000000000000000000000000000000000000000000020"
                                + "000000000000000000000000000000000000000000000000000000000000000a"
                                + "4772656574696e67732100000000000000000000000000000000000000000000"));
    }

    @Test
    public void testEncodeConstructorUint() {
        assertThat(
                FunctionEncoder.encodeConstructor(
                        Arrays.asList(
                                new Uint(BigInteger.ONE), new Uint(BigInteger.valueOf(0x20)))),
                is(
                        "0000000000000000000000000000000000000000000000000000000000000001"
                                + "0000000000000000000000000000000000000000000000000000000000000020"));
    }

    @Test
    public void testFunctionSimpleEncode() {
        Function function =
                new Function(
                        "baz",
                        Arrays.asList(new Uint32(BigInteger.valueOf(69)), new Bool(true)),
                        Collections.<TypeReference<?>>emptyList());

        assertThat(
                FunctionEncoder.encode(function),
                is(
                        "0xcdcd77c0"
                                + "0000000000000000000000000000000000000000000000000000000000000045"
                                + "0000000000000000000000000000000000000000000000000000000000000001"));
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
                        Collections.<TypeReference<?>>emptyList());

        assertThat(
                FunctionEncoder.encode(function),
                is(
                        "0xa5643bf2"
                                + "0000000000000000000000000000000000000000000000000000000000000060"
                                + "0000000000000000000000000000000000000000000000000000000000000001"
                                + "00000000000000000000000000000000000000000000000000000000000000a0"
                                + "0000000000000000000000000000000000000000000000000000000000000004"
                                + "6461766500000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000000000000000000003"
                                + "0000000000000000000000000000000000000000000000000000000000000001"
                                + "0000000000000000000000000000000000000000000000000000000000000002"
                                + "0000000000000000000000000000000000000000000000000000000000000003"));
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
                        Collections.<TypeReference<?>>emptyList());

        assertThat(
                FunctionEncoder.encode(function),
                is(
                        "0x8be65246"
                                + "0000000000000000000000000000000000000000000000000000000000000123"
                                + "0000000000000000000000000000000000000000000000000000000000000080"
                                + "3132333435363738393000000000000000000000000000000000000000000000"
                                + "00000000000000000000000000000000000000000000000000000000000000e0"
                                + "0000000000000000000000000000000000000000000000000000000000000002"
                                + "0000000000000000000000000000000000000000000000000000000000000456"
                                + "0000000000000000000000000000000000000000000000000000000000000789"
                                + "000000000000000000000000000000000000000000000000000000000000000d"
                                + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000"));
    }
}
