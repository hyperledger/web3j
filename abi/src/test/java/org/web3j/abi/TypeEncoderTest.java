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
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes1;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Bytes6;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.abi.datatypes.primitive.Byte;
import org.web3j.abi.datatypes.primitive.Char;
import org.web3j.abi.datatypes.primitive.Long;
import org.web3j.abi.datatypes.primitive.Short;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.web3j.abi.TypeEncoder.encode;

public class TypeEncoderTest {

    @Test
    public void testBoolEncode() {
        assertEquals(
                TypeEncoder.encodeBool(new Bool(false)),
                ("0000000000000000000000000000000000000000000000000000000000000000"));
        assertEquals(
                TypeEncoder.encodeBool(new Bool(true)),
                ("0000000000000000000000000000000000000000000000000000000000000001"));
    }

    @Test
    public void testUintEncode() {
        Uint zero = new Uint64(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodeNumeric(zero),
                ("0000000000000000000000000000000000000000000000000000000000000000"));

        Uint maxLong = new Uint64(BigInteger.valueOf(java.lang.Long.MAX_VALUE));
        assertEquals(
                TypeEncoder.encodeNumeric(maxLong),
                ("0000000000000000000000000000000000000000000000007fffffffffffffff"));

        Uint maxValue =
                new Uint(
                        new BigInteger(
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                                16));
        assertEquals(
                TypeEncoder.encodeNumeric(maxValue),
                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));

        Uint largeValue =
                new Uint(
                        new BigInteger(
                                "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe",
                                16));
        assertEquals(
                TypeEncoder.encodeNumeric(largeValue),
                ("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe"));
    }

    @Test
    public void testInvalidUintEncode() {
        assertThrows(UnsupportedOperationException.class, () -> new Uint64(BigInteger.valueOf(-1)));
    }

    @Test
    public void testTooLargeUintEncode() {
        // 1 more than "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
        assertThrows(
                UnsupportedOperationException.class,
                () ->
                        new Uint(
                                new BigInteger(
                                        "10000000000000000000000000000000000000000000000000000000000000000",
                                        16)));
    }

    @Test
    public void testIntEncode() {
        Int zero = new Int64(BigInteger.ZERO);
        assertEquals(
                TypeEncoder.encodeNumeric(zero),
                ("0000000000000000000000000000000000000000000000000000000000000000"));

        Int maxLong = new Int64(BigInteger.valueOf(java.lang.Long.MAX_VALUE));
        assertEquals(
                TypeEncoder.encodeNumeric(maxLong),
                ("0000000000000000000000000000000000000000000000007fffffffffffffff"));

        Int minLong = new Int64(BigInteger.valueOf(java.lang.Long.MIN_VALUE));
        assertEquals(
                TypeEncoder.encodeNumeric(minLong),
                ("ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000"));

        Int minusOne = new Int(BigInteger.valueOf(-1));
        assertEquals(
                TypeEncoder.encodeNumeric(minusOne),
                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
    }

    /*
    TODO: Enable once Solidity supports fixed types - see
    https://github.com/ethereum/solidity/issues/409

    @Test
    public void testUfixedEncode() {
        Ufixed zero = new Ufixed24x40(BigInteger.ZERO);
        assertEquals(TypeEncoder.encodeNumeric(zero),
                ("0000000000000000000000000000000000000000000000000000000000000000"));

        Ufixed maxLong = new Ufixed24x40(BigInteger.valueOf(Long.MAX_VALUE));
        assertEquals(TypeEncoder.encodeNumeric(maxLong),
                ("0000000000000000000000000000000000000000000000007fffffffffffffff"));

        Ufixed maxValue = new Ufixed(
                new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                        16));
        assertEquals(TypeEncoder.encodeNumeric(maxValue),
                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
    }

    @Test
    public void testFixedEncode() {
        Fixed zero = new Fixed24x40(BigInteger.ZERO);
        assertEquals(TypeEncoder.encodeNumeric(zero),
                ("0000000000000000000000000000000000000000000000000000000000000000"));

        Fixed maxLong = new Fixed24x40(BigInteger.valueOf(Long.MAX_VALUE));
        assertEquals(TypeEncoder.encodeNumeric(maxLong),
                ("0000000000000000000000000000000000000000000000007fffffffffffffff"));

        Fixed minLong = new Fixed24x40(BigInteger.valueOf(Long.MIN_VALUE));
        assertEquals(TypeEncoder.encodeNumeric(minLong),
                ("ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000"));

        Fixed minusOne = new Fixed24x40(BigInteger.valueOf(-1));
        assertEquals(TypeEncoder.encodeNumeric(minusOne),
                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
    }
    */

    @Test
    public void testStaticBytes() {
        Bytes staticBytes = new Bytes6(new byte[] {0, 1, 2, 3, 4, 5});
        assertEquals(
                TypeEncoder.encodeBytes(staticBytes),
                ("0001020304050000000000000000000000000000000000000000000000000000"));

        Bytes empty = new Bytes1(new byte[] {0});
        assertEquals(
                TypeEncoder.encodeBytes(empty),
                ("0000000000000000000000000000000000000000000000000000000000000000"));

        Bytes ones = new Bytes1(new byte[] {127});
        assertEquals(
                TypeEncoder.encodeBytes(ones),
                ("7f00000000000000000000000000000000000000000000000000000000000000"));

        Bytes dave = new Bytes4("dave".getBytes());
        assertEquals(
                TypeEncoder.encodeBytes(dave),
                ("6461766500000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testDynamicBytes() {
        DynamicBytes dynamicBytes = new DynamicBytes(new byte[] {0, 1, 2, 3, 4, 5});
        assertEquals(
                TypeEncoder.encodeDynamicBytes(dynamicBytes),
                ("0000000000000000000000000000000000000000000000000000000000000006"
                        + "0001020304050000000000000000000000000000000000000000000000000000"));

        DynamicBytes empty = new DynamicBytes(new byte[] {0});
        assertEquals(
                TypeEncoder.encodeDynamicBytes(empty),
                ("0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000000"));

        DynamicBytes dave = new DynamicBytes("dave".getBytes());
        assertEquals(
                TypeEncoder.encodeDynamicBytes(dave),
                ("0000000000000000000000000000000000000000000000000000000000000004"
                        + "6461766500000000000000000000000000000000000000000000000000000000"));

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
                TypeEncoder.encodeDynamicBytes(loremIpsum),
                ("00000000000000000000000000000000000000000000000000000000000001bd"
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
                        + "74206d6f6c6c697420616e696d20696420657374206c61626f72756d2e000000"));
    }

    @Test
    public void testAddress() {
        Address address = new Address("0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338");
        assertEquals(address.getTypeAsString(), ("address"));
        assertEquals(
                TypeEncoder.encodeAddress(address),
                ("000000000000000000000000be5422d15f39373eb0a97ff8c10fbd0e40e29338"));
    }

    @Test
    public void testInvalidAddress() {
        Address address =
                new Address("0xa04462684b510796c186d19abfa6929742f79394583d6efb1243bbb473f21d9f");
        assertEquals(address.getTypeAsString(), ("address"));
        assertThrows(UnsupportedOperationException.class, () -> TypeEncoder.encodeAddress(address));
    }

    @Test
    public void testLongAddress() {
        Address address =
                new Address(
                        256, "0xa04462684b510796c186d19abfa6929742f79394583d6efb1243bbb473f21d9f");
        assertEquals(address.getTypeAsString(), ("address"));
        TypeEncoder.encodeAddress(address);
    }

    @Test
    public void testUtf8String() {
        Utf8String string = new Utf8String("Hello, world!");
        assertEquals(
                TypeEncoder.encodeString(string),
                ("000000000000000000000000000000000000000000000000000000000000000d"
                        + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000"));
    }

    @Test
    public void testFixedArray() {
        StaticArray<Ufixed> array =
                new StaticArray2<>(
                        Ufixed.class,
                        new Ufixed(BigInteger.valueOf(0x2), BigInteger.valueOf(0x2)),
                        new Ufixed(BigInteger.valueOf(0x8), BigInteger.valueOf(0x8)));

        assertEquals(
                TypeEncoder.encodeArrayValues(array),
                ("0000000000000000000000000000000220000000000000000000000000000000"
                        + "0000000000000000000000000000000880000000000000000000000000000000"));
    }

    @Test
    public void testDynamicArray() {
        DynamicArray<Uint> array =
                new DynamicArray<>(
                        Uint.class,
                        new Uint(BigInteger.ONE),
                        new Uint(BigInteger.valueOf(2)),
                        new Uint(BigInteger.valueOf(3)));

        assertEquals(
                TypeEncoder.encodeDynamicArray(array),
                ("0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "0000000000000000000000000000000000000000000000000000000000000003"));
    }

    @Test
    public void testEmptyArray() {
        @SuppressWarnings("unchecked")
        DynamicArray<Uint> array = new DynamicArray(Uint.class);
        assertEquals(
                TypeEncoder.encodeDynamicArray(array),
                ("0000000000000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testArrayOfBytes() {
        DynamicArray<DynamicBytes> array =
                new DynamicArray<>(
                        new DynamicBytes(
                                Numeric.hexStringToByteArray(
                                        "0x3c329ee8cd725a7f74f984cac52598eb170d731e7f3"
                                                + "80d59a18aa861d2c8d6c43c880b2bfe0f3cde4efcd7"
                                                + "11c010c2f1d8af5e796f06716539446f95420df4211c")),
                        new DynamicBytes(
                                Numeric.hexStringToByteArray("0xcafe0000cafe0000cafe0000")),
                        new DynamicBytes(
                                Numeric.hexStringToByteArray(
                                        "0x9215c928b97e0ebeeefd10003a4e3eea23f2eb3acba"
                                                + "b477eeb589d7a8874d7c5")));
        DynamicArray emptyArray = DynamicArray.empty("bytes");
        DynamicArray<DynamicBytes> arrayOfEmptyBytes =
                new DynamicArray<>(new DynamicBytes(new byte[0]), new DynamicBytes(new byte[0]));

        assertEquals(
                TypeEncoder.encodeDynamicArray(array),
                //  array length
                ("0000000000000000000000000000000000000000000000000000000000000003"
                        // offset first bytes
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        // offset second bytes
                        + "00000000000000000000000000000000000000000000000000000000000000e0"
                        // offset third bytes
                        + "0000000000000000000000000000000000000000000000000000000000000120"
                        // length first bytes
                        + "0000000000000000000000000000000000000000000000000000000000000041"
                        // first bytes
                        + "3c329ee8cd725a7f74f984cac52598eb170d731e7f380d59a18aa861d2c8d6c4"
                        // first bytes continued
                        + "3c880b2bfe0f3cde4efcd711c010c2f1d8af5e796f06716539446f95420df421"
                        // first bytes continued
                        + "1c00000000000000000000000000000000000000000000000000000000000000"
                        // length second bytes
                        + "000000000000000000000000000000000000000000000000000000000000000c"
                        // second bytes
                        + "cafe0000cafe0000cafe00000000000000000000000000000000000000000000"
                        // length third bytes
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        // third bytes
                        + "9215c928b97e0ebeeefd10003a4e3eea23f2eb3acbab477eeb589d7a8874d7c5"));
        assertEquals(
                TypeEncoder.encodeDynamicArray(emptyArray),
                //  array length
                ("0000000000000000000000000000000000000000000000000000000000000000"));
        assertEquals(
                TypeEncoder.encodeDynamicArray(arrayOfEmptyBytes),
                //  array length
                ("0000000000000000000000000000000000000000000000000000000000000002"
                        // offset first bytes
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        // offset second bytes
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        // length first bytes
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        // length second bytes
                        + "0000000000000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testArrayOfStrings() {
        DynamicArray<Utf8String> array =
                new DynamicArray<>(
                        new Utf8String(
                                "This string value is extra long so that it "
                                        + "requires more than 32 bytes"),
                        new Utf8String("abc"),
                        new Utf8String(""),
                        new Utf8String("web3j"));
        DynamicArray emptyArray = DynamicArray.empty("string");
        DynamicArray<Utf8String> arrayOfEmptyStrings =
                new DynamicArray<>(new Utf8String(""), new Utf8String(""));

        assertEquals(
                TypeEncoder.encodeDynamicArray(array),
                //  array length
                ("0000000000000000000000000000000000000000000000000000000000000004"
                        // offset first string
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        // offset second string
                        + "0000000000000000000000000000000000000000000000000000000000000100"
                        // offset third string
                        + "0000000000000000000000000000000000000000000000000000000000000140"
                        // offset fourth string
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        // length first string
                        + "0000000000000000000000000000000000000000000000000000000000000046"
                        // first string
                        + "5468697320737472696e672076616c7565206973206578747261206c6f6e6720"
                        // first string continued
                        + "736f2074686174206974207265717569726573206d6f7265207468616e203332"
                        // first string continued
                        + "2062797465730000000000000000000000000000000000000000000000000000"
                        // length second string
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        // second string
                        + "6162630000000000000000000000000000000000000000000000000000000000"
                        // length third string
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        // length fourth string
                        + "0000000000000000000000000000000000000000000000000000000000000005"
                        // fourth string
                        + "776562336a000000000000000000000000000000000000000000000000000000"));
        assertEquals(
                TypeEncoder.encodeDynamicArray(emptyArray),
                //  array length
                ("0000000000000000000000000000000000000000000000000000000000000000"));
        assertEquals(
                TypeEncoder.encodeDynamicArray(arrayOfEmptyStrings),
                //  array length
                ("0000000000000000000000000000000000000000000000000000000000000002"
                        // offset first string
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        // offset second string
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        // length first string
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        // length second string
                        + "0000000000000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testPrimitiveByte() {
        assertEquals(
                encode(new Byte((byte) 0)),
                ("0000000000000000000000000000000000000000000000000000000000000000"));
        assertEquals(
                encode(new Byte((byte) 127)),
                ("7f00000000000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testPrimitiveChar() {
        assertEquals(
                encode(new Char('a')),
                ("0000000000000000000000000000000000000000000000000000000000000001"
                        + "6100000000000000000000000000000000000000000000000000000000000000"));
        assertEquals(
                encode(new Char(' ')),
                ("0000000000000000000000000000000000000000000000000000000000000001"
                        + "2000000000000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testPrimitiveInt() {
        assertEquals(
                encode(new org.web3j.abi.datatypes.primitive.Int(0)),
                ("0000000000000000000000000000000000000000000000000000000000000000"));

        assertEquals(
                encode(new org.web3j.abi.datatypes.primitive.Int(Integer.MIN_VALUE)),
                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffff80000000"));

        assertEquals(
                encode(new org.web3j.abi.datatypes.primitive.Int(Integer.MAX_VALUE)),
                ("000000000000000000000000000000000000000000000000000000007fffffff"));
    }

    @Test
    public void testPrimitiveShort() {
        assertEquals(
                encode(new Short((short) 0)),
                ("0000000000000000000000000000000000000000000000000000000000000000"));

        assertEquals(
                encode(new Short(java.lang.Short.MIN_VALUE)),
                ("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8000"));

        assertEquals(
                encode(new Short(java.lang.Short.MAX_VALUE)),
                ("0000000000000000000000000000000000000000000000000000000000007fff"));
    }

    @Test
    public void testPrimitiveLong() {
        assertEquals(
                encode(new Long(0)),
                ("0000000000000000000000000000000000000000000000000000000000000000"));

        assertEquals(
                encode(new Long(java.lang.Long.MIN_VALUE)),
                ("ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000"));

        assertEquals(
                encode(new Long(java.lang.Long.MAX_VALUE)),
                ("0000000000000000000000000000000000000000000000007fffffffffffffff"));
    }

    @Test
    public void testPrimitiveFloat() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> encode(new org.web3j.abi.datatypes.primitive.Float(0)));
    }

    @Test
    public void testPrimitiveDouble() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> encode(new org.web3j.abi.datatypes.primitive.Double(0)));
    }
}
