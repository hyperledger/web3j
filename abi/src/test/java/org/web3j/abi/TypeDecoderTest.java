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

import org.junit.jupiter.api.Test;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes1;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Bytes6;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.StaticArray3;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TypeDecoderTest {

    @Test
    public void testBoolDecode() throws Exception {
        assertEquals(
                TypeDecoder.decodeBool(
                        "0000000000000000000000000000000000000000000000000000000000000000", 0),
                (new Bool(false)));

        assertEquals(
                TypeDecoder.decodeBool(
                        "0000000000000000000000000000000000000000000000000000000000000001", 0),
                (new Bool(true)));

        assertEquals(TypeDecoder.instantiateType("bool", true), (new Bool(true)));

        assertEquals(TypeDecoder.instantiateType("bool", 1), (new Bool(true)));

        assertEquals(TypeDecoder.instantiateType("bool", false), (new Bool(false)));

        assertEquals(TypeDecoder.instantiateType("bool", 0), (new Bool(false)));
    }

    @Test
    public void testBoolDecodeGivenOffset() {
        // Decode second parameter as Bool
        assertEquals(
                TypeDecoder.decode(
                        "0000000000000000000000000000000000000000000000007fffffffffffffff"
                                + "0000000000000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000007fffffffffffffff",
                        64,
                        Bool.class),
                (new Bool(false)));

        assertEquals(
                TypeDecoder.decode(
                        "0000000000000000000000000000000000000000000000007fffffffffffffff"
                                + "0000000000000000000000000000000000000000000000000000000000000001"
                                + "0000000000000000000000000000000000000000000000007fffffffffffffff",
                        64,
                        Bool.class),
                (new Bool(true)));
    }

    @Test
    public void testUintDecode() throws Exception {
        assertEquals(
                TypeDecoder.decodeNumeric(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        Uint64.class),
                (new Uint64(BigInteger.ZERO)));

        assertEquals(
                TypeDecoder.decodeNumeric(
                        "0000000000000000000000000000000000000000000000007fffffffffffffff",
                        Uint64.class),
                (new Uint64(BigInteger.valueOf(Long.MAX_VALUE))));

        assertEquals(
                TypeDecoder.decodeNumeric(
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                        Uint64.class),
                (new Uint64(new BigInteger("0ffffffffffffffff", 16))));
        assertEquals(TypeDecoder.instantiateType("uint", 123), (new Uint(BigInteger.valueOf(123))));
    }

    @Test
    public void testIntDecode() throws Exception {
        assertEquals(
                TypeDecoder.decodeNumeric(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        Int64.class),
                (new Int64(BigInteger.ZERO)));

        assertEquals(
                TypeDecoder.decodeNumeric(
                        "0000000000000000000000000000000000000000000000007fffffffffffffff",
                        Int64.class),
                (new Int64(BigInteger.valueOf(Long.MAX_VALUE))));

        assertEquals(
                TypeDecoder.decodeNumeric(
                        "fffffffffffffffffffffffffffffffffffffffffffffff88000000000000000",
                        Int64.class),
                (new Int64(BigInteger.valueOf(Long.MIN_VALUE))));

        assertEquals(
                TypeDecoder.decodeNumeric(
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                        Int64.class),
                (new Int64(BigInteger.valueOf(-1))));

        assertEquals(
                TypeDecoder.decodeNumeric(
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                        Int256.class),
                (new Int256(BigInteger.valueOf(-1))));

        assertEquals(TypeDecoder.instantiateType("int", 123), (new Int(BigInteger.valueOf(123))));

        assertEquals(TypeDecoder.instantiateType("int", -123), (new Int(BigInteger.valueOf(-123))));
    }

    /*
    TODO: Enable once Solidity supports fixed types - see
    https://github.com/ethereum/solidity/issues/409

    @Test
    public void testUfixedDecode() {
        assertEquals(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000000000000000000000",
                Ufixed24x40.class),
                (new Ufixed24x40(BigInteger.ZERO)));

        assertEquals(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000007fffffffffffffff",
                Ufixed24x40.class),
                (new Ufixed24x40(BigInteger.valueOf(Long.MAX_VALUE))));

        assertEquals(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                Ufixed.class),
                (new Ufixed(
                        new BigInteger(
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                                16
                        ))));
    }

    @Test
    public void testFixedDecode() {
        assertEquals(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000000000000000000000",
                Fixed24x40.class),
                (new Fixed24x40(BigInteger.ZERO)));

        assertEquals(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000007fffffffffffffff",
                Fixed24x40.class),
                (new Fixed24x40(BigInteger.valueOf(Long.MAX_VALUE))));

        assertEquals(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000",
                Fixed24x40.class),
                (new Fixed24x40(BigInteger.valueOf(Long.MIN_VALUE))));

        assertEquals(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                Fixed.class),
                (new Fixed(BigInteger.valueOf(-1))));
    }
    */

    @Test
    public void testStaticBytes() throws Exception {
        byte[] testbytes = new byte[] {0, 1, 2, 3, 4, 5};
        Bytes6 staticBytes = new Bytes6(testbytes);
        assertEquals(
                TypeDecoder.decodeBytes(
                        "0001020304050000000000000000000000000000000000000000000000000000",
                        Bytes6.class),
                (staticBytes));

        Bytes empty = new Bytes1(new byte[] {0});
        assertEquals(
                TypeDecoder.decodeBytes(
                        "0000000000000000000000000000000000000000000000000000000000000000",
                        Bytes1.class),
                (empty));

        Bytes dave = new Bytes4("dave".getBytes());
        assertEquals(
                TypeDecoder.decodeBytes(
                        "6461766500000000000000000000000000000000000000000000000000000000",
                        Bytes4.class),
                (dave));

        assertEquals(TypeDecoder.instantiateType("bytes6", testbytes), (new Bytes6(testbytes)));
    }

    @Test
    public void testDynamicBytes() throws Exception {
        byte[] testbytes = new byte[] {0, 1, 2, 3, 4, 5};
        DynamicBytes dynamicBytes = new DynamicBytes(testbytes);
        assertEquals(
                TypeDecoder.decodeDynamicBytes(
                        "0000000000000000000000000000000000000000000000000000000000000006" // length
                                + "0001020304050000000000000000000000000000000000000000000000000000",
                        0),
                (dynamicBytes));

        DynamicBytes empty = new DynamicBytes(new byte[] {0});
        assertEquals(
                TypeDecoder.decodeDynamicBytes(
                        "0000000000000000000000000000000000000000000000000000000000000001"
                                + "0000000000000000000000000000000000000000000000000000000000000000",
                        0),
                (empty));

        DynamicBytes dave = new DynamicBytes("dave".getBytes());
        assertEquals(
                TypeDecoder.decodeDynamicBytes(
                        "0000000000000000000000000000000000000000000000000000000000000004"
                                + "6461766500000000000000000000000000000000000000000000000000000000",
                        0),
                (dave));

        DynamicBytes loremIpsum =
                new DynamicBytes(
                        ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
                                        + "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim "
                                        + "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex "
                                        + "ea commodo consequat. Duis aute irure dolor in reprehenderit in "
                                        + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
                                        + "sint occaecat cupidatat non proident, sunt in culpa qui officia "
                                        + "deserunt mollit anim id est laborum.")
                                .getBytes());

        assertEquals(
                TypeDecoder.decodeDynamicBytes(
                        "00000000000000000000000000000000000000000000000000000000000001bd"
                                + "4c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73"
                                + "656374657475722061646970697363696e6720656c69742c2073656420646f20"
                                + "656975736d6f642074656d706f7220696e6369646964756e74207574206c6162"
                                + "6f726520657420646f6c6f7265206d61676e6120616c697175612e2055742065"
                                + "6e696d206164206d696e696d2076656e69616d2c2071756973206e6f73747275"
                                + "6420657865726369746174696f6e20756c6c616d636f206c61626f726973206e"
                                + "69736920757420616c697175697020657820656120636f6d6d6f646f20636f6e"
                                + "7365717561742e2044756973206175746520697275726520646f6c6f7220696e"
                                + "20726570726568656e646572697420696e20766f6c7570746174652076656c69"
                                + "7420657373652063696c6c756d20646f6c6f726520657520667567696174206e"
                                + "756c6c612070617269617475722e204578636570746575722073696e74206f63"
                                + "63616563617420637570696461746174206e6f6e2070726f6964656e742c2073"
                                + "756e7420696e2063756c706120717569206f666669636961206465736572756e"
                                + "74206d6f6c6c697420616e696d20696420657374206c61626f72756d2e000000",
                        0),
                (loremIpsum));

        assertEquals(
                TypeDecoder.instantiateType("bytes", testbytes), (new DynamicBytes(testbytes)));
    }

    @Test
    public void testAddress() throws Exception {
        assertEquals(
                TypeDecoder.decodeAddress(
                        "000000000000000000000000be5422d15f39373eb0a97ff8c10fbd0e40e29338"),
                (new Address("0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338")));

        assertEquals(
                TypeDecoder.decodeAddress(
                        "000000000000000000000000be5422d15f39373eb0a97ff8c10fbd0e40e29338"),
                (new Address("0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338")));

        assertEquals(
                TypeDecoder.instantiateType(
                        "address", "0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338"),
                (new Address("0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338")));

        assertEquals(
                TypeDecoder.instantiateType("address", BigInteger.ONE),
                (new Address("0x0000000000000000000000000000000000000001")));
    }

    @Test
    public void testUtf8String() throws Exception {
        assertEquals(
                TypeDecoder.decodeUtf8String(
                        "000000000000000000000000000000000000000000000000000000000000000d" // length
                                + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000",
                        0),
                (new Utf8String("Hello, world!")));

        assertEquals(
                TypeDecoder.instantiateType("string", "Hello, world!"),
                (new Utf8String("Hello, world!")));
    }

    @Test
    public void testStaticArray() throws Exception {
        assertEquals(
                TypeDecoder.decodeStaticArray(
                        "000000000000000000000000000000000000000000000000000000000000000a"
                                + "0000000000000000000000000000000000000000000000007fffffffffffffff",
                        0,
                        new TypeReference.StaticArrayTypeReference<StaticArray<Uint256>>(2) {},
                        2),
                (new StaticArray2<>(
                        Uint256.class,
                        new Uint256(BigInteger.TEN),
                        new Uint256(BigInteger.valueOf(Long.MAX_VALUE)))));

        assertEquals(
                TypeDecoder.decodeStaticArray(
                        "000000000000000000000000000000000000000000000000000000000000000d"
                                + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000"
                                + "000000000000000000000000000000000000000000000000000000000000000d"
                                + "776f726c64212048656c6c6f2c00000000000000000000000000000000000000",
                        0,
                        new TypeReference.StaticArrayTypeReference<StaticArray<Utf8String>>(2) {},
                        2),
                (new StaticArray2<>(
                        Utf8String.class,
                        new Utf8String("Hello, world!"),
                        new Utf8String("world! Hello,"))));

        StaticArray2 arr =
                (StaticArray2)
                        TypeDecoder.instantiateType("uint256[2]", new long[] {10, Long.MAX_VALUE});
        assert (arr instanceof StaticArray2);

        assertEquals(arr.getValue().get(0), (new Uint256(BigInteger.TEN)));

        assertEquals(arr.getValue().get(1), (new Uint256(BigInteger.valueOf(Long.MAX_VALUE))));
    }

    @Test
    public void testEmptyStaticArray() {
        assertThrows(
                UnsupportedOperationException.class,
                () ->
                        TypeDecoder.decodeStaticArray(
                                "0000000000000000000000000000000000000000000000000000000000000000",
                                0,
                                new TypeReference.StaticArrayTypeReference<StaticArray<Uint256>>(
                                        0) {},
                                0));
    }

    @Test
    public void testEmptyStaticArrayInstantiateType() throws Exception {
        assertThrows(
                ClassNotFoundException.class,
                () -> TypeDecoder.instantiateType("uint256[0]", new long[] {}));
    }

    @Test
    public void testDynamicArray() throws Exception {
        assertEquals(
                TypeDecoder.decodeDynamicArray(
                        "0000000000000000000000000000000000000000000000000000000000000000", // length
                        0,
                        new TypeReference<DynamicArray<Uint256>>() {}),
                (new DynamicArray<>(Uint256.class)));

        assertEquals(
                TypeDecoder.decodeDynamicArray(
                        "0000000000000000000000000000000000000000000000000000000000000002" // length
                                + "000000000000000000000000000000000000000000000000000000000000000a"
                                + "0000000000000000000000000000000000000000000000007fffffffffffffff",
                        0,
                        new TypeReference<DynamicArray<Uint256>>() {}),
                (new DynamicArray<>(
                        Uint256.class,
                        new Uint256(BigInteger.TEN),
                        new Uint256(BigInteger.valueOf(Long.MAX_VALUE)))));

        assertEquals(
                TypeDecoder.decodeDynamicArray(
                        "0000000000000000000000000000000000000000000000000000000000000002" // length
                                + "000000000000000000000000000000000000000000000000000000000000000d"
                                + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000"
                                + "000000000000000000000000000000000000000000000000000000000000000d"
                                + "776f726c64212048656c6c6f2c00000000000000000000000000000000000000",
                        0,
                        new TypeReference<DynamicArray<Utf8String>>() {}),
                (new DynamicArray<>(
                        Utf8String.class,
                        new Utf8String("Hello, world!"),
                        new Utf8String("world! Hello,"))));

        DynamicArray arr =
                (DynamicArray)
                        TypeDecoder.instantiateType(
                                "string[]", new String[] {"Hello, world!", "world! Hello,"});
        assert (arr instanceof DynamicArray);

        assertEquals(arr.getValue().get(0), (new Utf8String("Hello, world!")));

        assertEquals(arr.getValue().get(1), (new Utf8String("world! Hello,")));
    }

    @Test
    public void multiDimArrays() throws Exception {
        byte[] bytes1d = new byte[] {1, 2, 3};
        byte[][] bytes2d = new byte[][] {bytes1d, bytes1d, bytes1d};
        final byte[][][] bytes3d = new byte[][][] {bytes2d, bytes2d, bytes2d};

        assertEquals(TypeDecoder.instantiateType("bytes", bytes1d), (new DynamicBytes(bytes1d)));

        StaticArray3<DynamicArray<Uint256>> twoDim =
                (StaticArray3<DynamicArray<Uint256>>)
                        TypeDecoder.instantiateType("uint256[][3]", bytes2d);
        assert (twoDim instanceof StaticArray3);
        DynamicArray<Uint256> row1 = twoDim.getValue().get(1);
        assert (row1 instanceof DynamicArray);
        assertEquals(row1.getValue().get(2), (new Uint256(3)));

        StaticArray3<StaticArray3<DynamicArray<Uint256>>> threeDim =
                (StaticArray3<StaticArray3<DynamicArray<Uint256>>>)
                        TypeDecoder.instantiateType("uint256[][3][3]", bytes3d);
        assert (threeDim instanceof StaticArray3);
        row1 = threeDim.getValue().get(1).getValue().get(1);
        assert (row1 instanceof DynamicArray);
        assertEquals(row1.getValue().get(1), (new Uint256(2)));
    }
}
