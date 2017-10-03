package org.web3j.abi;

import java.math.BigInteger;

import org.junit.Test;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Fixed;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes1;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Bytes6;
import org.web3j.abi.datatypes.generated.Fixed24x40;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.Ufixed24x40;
import org.web3j.abi.datatypes.generated.Uint64;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class TypeEncoderTest {

    @Test
    public void testBoolEncode() {
        assertThat(TypeEncoder.encodeBool(new Bool(false)),
                is("0000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(TypeEncoder.encodeBool(new Bool(true)),
                is("0000000000000000000000000000000000000000000000000000000000000001"));
    }

    @Test
    public void testUintEncode() {
        Uint zero = new Uint64(BigInteger.ZERO);
        assertThat(TypeEncoder.encodeNumeric(zero),
                is("0000000000000000000000000000000000000000000000000000000000000000"));

        Uint maxLong = new Uint64(BigInteger.valueOf(Long.MAX_VALUE));
        assertThat(TypeEncoder.encodeNumeric(maxLong),
                is("0000000000000000000000000000000000000000000000007fffffffffffffff"));

        Uint maxValue = new Uint(
                new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                16));
        assertThat(TypeEncoder.encodeNumeric(maxValue),
                is("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));

        Uint largeValue = new Uint(
                new BigInteger("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe",
                16));
        assertThat(TypeEncoder.encodeNumeric(largeValue),
                is("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidUintEncode() {
        new Uint64(BigInteger.valueOf(-1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTooLargeUintEncode() {
        // 1 more than "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
        new Uint(new BigInteger("10000000000000000000000000000000000000000000000000000000000000000",
                16));

    }

    @Test
    public void testIntEncode() {
        Int zero = new Int64(BigInteger.ZERO);
        assertThat(TypeEncoder.encodeNumeric(zero),
                is("0000000000000000000000000000000000000000000000000000000000000000"));

        Int maxLong = new Int64(BigInteger.valueOf(Long.MAX_VALUE));
        assertThat(TypeEncoder.encodeNumeric(maxLong),
                is("0000000000000000000000000000000000000000000000007fffffffffffffff"));

        Int minLong = new Int64(BigInteger.valueOf(Long.MIN_VALUE));
        assertThat(TypeEncoder.encodeNumeric(minLong),
                is("ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000"));

        Int minusOne = new Int(BigInteger.valueOf(-1));
        assertThat(TypeEncoder.encodeNumeric(minusOne),
                is("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
    }

    @Test
    public void testUfixedEncode() {
        Ufixed zero = new Ufixed24x40(BigInteger.ZERO);
        assertThat(TypeEncoder.encodeNumeric(zero),
                is("0000000000000000000000000000000000000000000000000000000000000000"));

        Ufixed maxLong = new Ufixed24x40(BigInteger.valueOf(Long.MAX_VALUE));
        assertThat(TypeEncoder.encodeNumeric(maxLong),
                is("0000000000000000000000000000000000000000000000007fffffffffffffff"));

        Ufixed maxValue = new Ufixed(
                new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                        16));
        assertThat(TypeEncoder.encodeNumeric(maxValue),
                is("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
    }

    @Test
    public void testFixedEncode() {
        Fixed zero = new Fixed24x40(BigInteger.ZERO);
        assertThat(TypeEncoder.encodeNumeric(zero),
                is("0000000000000000000000000000000000000000000000000000000000000000"));

        Fixed maxLong = new Fixed24x40(BigInteger.valueOf(Long.MAX_VALUE));
        assertThat(TypeEncoder.encodeNumeric(maxLong),
                is("0000000000000000000000000000000000000000000000007fffffffffffffff"));

        Fixed minLong = new Fixed24x40(BigInteger.valueOf(Long.MIN_VALUE));
        assertThat(TypeEncoder.encodeNumeric(minLong),
                is("ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000"));

        Fixed minusOne = new Fixed24x40(BigInteger.valueOf(-1));
        assertThat(TypeEncoder.encodeNumeric(minusOne),
                is("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
    }

    @Test
    public void testStaticBytes() {
        Bytes staticBytes = new Bytes6(new byte[] { 0, 1, 2, 3, 4, 5 });
        assertThat(TypeEncoder.encodeBytes(staticBytes),
                is("0001020304050000000000000000000000000000000000000000000000000000"));

        Bytes empty = new Bytes1(new byte[] { 0 });
        assertThat(TypeEncoder.encodeBytes(empty),
                is("0000000000000000000000000000000000000000000000000000000000000000"));

        Bytes dave = new Bytes4("dave".getBytes());
        assertThat(TypeEncoder.encodeBytes(dave),
                is("6461766500000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testDynamicBytes() {
        DynamicBytes dynamicBytes = new DynamicBytes(new byte[] { 0, 1, 2, 3, 4, 5 });
        assertThat(TypeEncoder.encodeDynamicBytes(dynamicBytes),
                is("0000000000000000000000000000000000000000000000000000000000000006"
                        + "0001020304050000000000000000000000000000000000000000000000000000"));

        DynamicBytes empty = new DynamicBytes(new byte[] { 0 });
        assertThat(TypeEncoder.encodeDynamicBytes(empty),
                is("0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000000"));

        DynamicBytes dave = new DynamicBytes("dave".getBytes());
        assertThat(TypeEncoder.encodeDynamicBytes(dave),
                is("0000000000000000000000000000000000000000000000000000000000000004"
                        + "6461766500000000000000000000000000000000000000000000000000000000"));

        DynamicBytes loremIpsum = new DynamicBytes(
                ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod "
                        + "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim "
                        + "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex "
                        + "ea commodo consequat. Duis aute irure dolor in reprehenderit in "
                        + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
                        + "sint occaecat cupidatat non proident, sunt in culpa qui officia "
                        + "deserunt mollit anim id est laborum.").getBytes());
        assertThat(TypeEncoder.encodeDynamicBytes(loremIpsum),
                is("00000000000000000000000000000000000000000000000000000000000001bd"
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
        assertThat(address.getTypeAsString(), is("address"));
        assertThat(TypeEncoder.encodeNumeric(address),
                is("000000000000000000000000be5422d15f39373eb0a97ff8c10fbd0e40e29338"));
    }

    @Test
    public void testUtf8String() {
        Utf8String string = new Utf8String("Hello, world!");
        assertThat(TypeEncoder.encodeString(string),
                is("000000000000000000000000000000000000000000000000000000000000000d"
                        + "48656c6c6f2c20776f726c642100000000000000000000000000000000000000"));
    }

    @Test
    public void testFixedArray() {
        StaticArray<Ufixed> array = new StaticArray<>(
                new Ufixed(BigInteger.valueOf(0x2), BigInteger.valueOf(0x2)),
                new Ufixed(BigInteger.valueOf(0x8), BigInteger.valueOf(0x8))
        );

        assertThat(TypeEncoder.encodeArrayValues(array),
                is("0000000000000000000000000000000220000000000000000000000000000000"
                        + "0000000000000000000000000000000880000000000000000000000000000000"));
    }

    @Test
    public void testDynamicArray() {
        DynamicArray<Uint> array = new DynamicArray<>(
                new Uint(BigInteger.ONE),
                new Uint(BigInteger.valueOf(2)),
                new Uint(BigInteger.valueOf(3))
        );

        assertThat(
                TypeEncoder.encodeDynamicArray(array),
                is("0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                ));
    }

    @Test
    public void testEmptyArray() {
        DynamicArray<Uint> array = DynamicArray.empty("uint");
        assertThat(
                TypeEncoder.encodeDynamicArray(array),
                is("0000000000000000000000000000000000000000000000000000000000000000")
        );
    }
}
