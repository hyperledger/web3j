package org.web3j.abi;


import java.math.BigInteger;

import org.junit.Test;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TypeDecoderTest {

    @Test
    public void testBoolDecode() {
        assertThat(TypeDecoder.decodeBool(
                "0000000000000000000000000000000000000000000000000000000000000000"),
                is(new Bool(false)));

        assertThat(TypeDecoder.decodeBool(
                "0000000000000000000000000000000000000000000000000000000000000001"),
                is(new Bool(true)));
    }

    @Test
    public void testUintDecode() {

        assertThat(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000000000000000000000",
                Uint64.class
                ),
                is(new Uint64(BigInteger.ZERO)));

        assertThat(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000007fffffffffffffff",
                Uint64.class
                ),
                is(new Uint64(BigInteger.valueOf(Long.MAX_VALUE))));

        assertThat(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                Uint64.class
                ),
                is(new Uint64(new BigInteger(
                        "0ffffffffffffffff", 16))));
    }

    @Test
    public void testIntDecode() {
        assertThat(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000000000000000000000",
                Int64.class
                ),
                is(new Int64(BigInteger.ZERO)));

        assertThat(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000007fffffffffffffff",
                Int64.class
                ),
                is(new Int64(BigInteger.valueOf(Long.MAX_VALUE))));

        assertThat(TypeDecoder.decodeNumeric(
                "fffffffffffffffffffffffffffffffffffffffffffffff88000000000000000",
                Int64.class
                ),
                is(new Int64(BigInteger.valueOf(Long.MIN_VALUE))));

        assertThat(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                Int64.class
                ),
                is(new Int64(BigInteger.valueOf(-1))));

        assertThat(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                Int256.class
                ),
                is(new Int256(BigInteger.valueOf(-1))));
    }

    @Test
    public void testUfixedDecode() {
        assertThat(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000000000000000000000",
                Ufixed24x40.class),
                is(new Ufixed24x40(BigInteger.ZERO)));

        assertThat(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000007fffffffffffffff",
                Ufixed24x40.class),
                is(new Ufixed24x40(BigInteger.valueOf(Long.MAX_VALUE))));

        assertThat(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                Ufixed.class),
                is(new Ufixed(
                        new BigInteger(
                                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                                16
                        ))));
    }

    @Test
    public void testFixedDecode() {
        assertThat(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000000000000000000000",
                Fixed24x40.class),
                is(new Fixed24x40(BigInteger.ZERO)));

        assertThat(TypeDecoder.decodeNumeric(
                "0000000000000000000000000000000000000000000000007fffffffffffffff",
                Fixed24x40.class),
                is(new Fixed24x40(BigInteger.valueOf(Long.MAX_VALUE))));

        assertThat(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000",
                Fixed24x40.class),
                is(new Fixed24x40(BigInteger.valueOf(Long.MIN_VALUE))));

        assertThat(TypeDecoder.decodeNumeric(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",
                Fixed.class),
                is(new Fixed(BigInteger.valueOf(-1))));
    }

    @Test
    public void testStaticBytes() {
        Bytes6 staticBytes = new Bytes6(new byte[] { 0, 1, 2, 3, 4, 5 });
        assertThat(TypeDecoder.decodeBytes(
                "0001020304050000000000000000000000000000000000000000000000000000", Bytes6.class),
                is(staticBytes));

        Bytes empty = new Bytes1(new byte[] { 0 });
        assertThat(TypeDecoder.decodeBytes(
                "0000000000000000000000000000000000000000000000000000000000000000", Bytes1.class),
                is(empty));

        Bytes dave = new Bytes4("dave".getBytes());
        assertThat(TypeDecoder.decodeBytes(
                "6461766500000000000000000000000000000000000000000000000000000000", Bytes4.class),
                is(dave));
    }

    @Test
    public void testDynamicBytes() {
        DynamicBytes dynamicBytes = new DynamicBytes(new byte[] { 0, 1, 2, 3, 4, 5 });
        assertThat(TypeDecoder.decodeDynamicBytes(
                "0000000000000000000000000000000000000000000000000000000000000001" +
                "0000000000000000000000000000000000000000000000000000000000000006" +
                        "0001020304050000000000000000000000000000000000000000000000000000", 0),
                is(dynamicBytes));

        DynamicBytes empty = new DynamicBytes(new byte[] { 0 });
        assertThat(TypeDecoder.decodeDynamicBytes(
                "0000000000000000000000000000000000000000000000000000000000000001" +
                "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000000", 0),
                is(empty));

        DynamicBytes dave = new DynamicBytes("dave".getBytes());

        assertThat(TypeDecoder.decodeDynamicBytes(
                "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000004" +
                        "6461766500000000000000000000000000000000000000000000000000000000", 0),
                is(dave));
    }

    @Test
    public void testAddress() {
        assertThat(TypeDecoder.decodeNumeric(
                "000000000000000000000000be5422d15f39373eb0a97ff8c10fbd0e40e29338", Address.class),
                is(new Address("0xbe5422d15f39373eb0a97ff8c10fbd0e40e29338")));
    }

    @Test
    public void testUtf8String() {
        assertThat(TypeDecoder.decodeUtf8String(
                "0000000000000000000000000000000000000000000000000000000000000001" +
                "000000000000000000000000000000000000000000000000000000000000000d" +
                "48656c6c6f2c20776f726c642100000000000000000000000000000000000000", 0),
                is(new Utf8String("Hello, world!")));
    }
}
