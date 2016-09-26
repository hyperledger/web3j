package org.web3j.abi;

import java.math.BigInteger;

import org.junit.Test;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.*;

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
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidUintEncode() {
        new Uint64(BigInteger.valueOf(-1));
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
        DynamicBytesType staticBytes = new DynamicBytesType(new byte[] { 0, 1, 2, 3, 4, 5 });
        assertThat(TypeEncoder.encodeDynamicBytes(staticBytes),
                is("0000000000000000000000000000000000000000000000000000000000000006" +
                        "0001020304050000000000000000000000000000000000000000000000000000"));

        DynamicBytesType empty = new DynamicBytesType(new byte[] { 0 });
        assertThat(TypeEncoder.encodeDynamicBytes(empty),
                is("0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000000"));

        DynamicBytesType dave = new DynamicBytesType("dave".getBytes());
        assertThat(TypeEncoder.encodeDynamicBytes(dave),
                is("0000000000000000000000000000000000000000000000000000000000000004" +
                        "6461766500000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testAddress() {
        Address address = new Address("0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338");
        assertThat(address.getTypeAsString(), is("address160"));
        assertThat(TypeEncoder.encodeNumeric(address), is("000000000000000000000000be5422d15f39373eb0a97ff8c10fbd0e40e29338"));
    }

    @Test
    public void testUtf8String() {
        Utf8String string = new Utf8String("Hello, world!");
        assertThat(TypeEncoder.encodeString(string),
                is("000000000000000000000000000000000000000000000000000000000000000d" +
                        "48656c6c6f2c20776f726c642100000000000000000000000000000000000000"));
    }

    @Test
    public void testFixedArray() {
        StaticArray<Ufixed> array = new StaticArray<>(
                new Ufixed(BigInteger.valueOf(0x2), BigInteger.valueOf(0x2)),
                new Ufixed(BigInteger.valueOf(0x8), BigInteger.valueOf(0x8))
        );

        assertThat(TypeEncoder.encodeArrayValues(array),
                is("0000000000000000000000000000000220000000000000000000000000000000" +
                        "0000000000000000000000000000000880000000000000000000000000000000"));
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
                is("0000000000000000000000000000000000000000000000000000000000000003" +
                        "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "0000000000000000000000000000000000000000000000000000000000000003"
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
