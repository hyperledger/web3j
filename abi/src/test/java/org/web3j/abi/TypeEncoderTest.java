package org.web3j.abi;

import org.junit.Test;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.CompositeDataHolderType;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.StaticStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes1;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.abi.datatypes.generated.Bytes6;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.abi.structs.NestedDynamicStruct2;
import org.web3j.abi.structs.NestedDynamicStruct;
import org.web3j.abi.structs.DynamicStruct7;
import org.web3j.abi.structs.DynamicStructWithUintAndString;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class TypeEncoderTest {

    // TODO: add a test case with a "value" field in a tuple.
    // Perhaps override to prefix with "_"?

    @Test
    public void assertDynamicStructsTest1() {
        final DynamicStructWithString dynamicStruct1 = new DynamicStructWithString("hello");

        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "68656c6c6f000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void assertDynamicStructsTest2() {
        final DynamicStructWithString dynamicStruct1 = new DynamicStructWithString("hello");

        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "68656c6c6f000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void assertDynamicStructsTest3() {
        final DynamicStructWithUintAndString dynamicStruct1 = new DynamicStructWithUintAndString(BigInteger.valueOf(100), "hello");

        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000064" +
                        "0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "68656c6c6f000000000000000000000000000000000000000000000000000000"
                ));
    }

    @Test
    public void assertDynamicStructsTest3a() {
        final DynamicStructWithStringAndUint dynamicStruct1 = new DynamicStructWithStringAndUint("First", BigInteger.valueOf(100));

        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000064" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "4669727374000000000000000000000000000000000000000000000000000000"
                ));
    }


    @Test
    public void assertDynamicStructsTest4() {
        final DynamicStruct4 dynamicStruct1 = new DynamicStruct4("First", "Second");

        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000080" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "4669727374000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000006" +
                        "5365636f6e640000000000000000000000000000000000000000000000000000"
                ));
    }

    @Test
    public void assertDynamicStructsTest5() {
        final StaticStruct5 staticStruct = new StaticStruct5(BigInteger.valueOf(100), BigInteger.valueOf(200));

        assertFalse(TypeEncoder.isDynamic(staticStruct));
        assertThat(TypeEncoder.encode(staticStruct),
                is("0000000000000000000000000000000000000000000000000000000000000064" +
                        "00000000000000000000000000000000000000000000000000000000000000c8"
                ));
    }

    @Test
    public void assertDynamicStructsTest6() {
        final DynamicStruct6 dynamicStruct1 = new DynamicStruct6(BigInteger.ONE, BigInteger.valueOf(2), "three", "four", BigInteger.valueOf(5), true);

        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "00000000000000000000000000000000000000000000000000000000000000c0" +
                        "0000000000000000000000000000000000000000000000000000000000000100" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "7468726565000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000004" +
                        "666f757200000000000000000000000000000000000000000000000000000000"
                ));
    }

    @Test
    public void assertDynamicStructsTest7() {
        final DynamicStruct7 dynamicStruct1 = new DynamicStruct7(BigInteger.ONE, BigInteger.valueOf(2), "third", "fourth", BigInteger.valueOf(5), false, "seventh", BigInteger.valueOf(8));

        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "0000000000000000000000000000000000000000000000000000000000000100" +
                        "0000000000000000000000000000000000000000000000000000000000000140" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "0000000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000180" +
                        "0000000000000000000000000000000000000000000000000000000000000008" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "7468697264000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000006" +
                        "666f757274680000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000007" +
                        "736576656e746800000000000000000000000000000000000000000000000000"
                ));
    }

    //

    @Test
    public void assertDynamicStructsComplexTest1() {
        final NestedDynamicStruct dynamicStruct1 = new NestedDynamicStruct("First", "Second", new NestedDynamicStruct.Foo(BigInteger.valueOf(100), "foobar"));

        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000060" +
                        "00000000000000000000000000000000000000000000000000000000000000a0" +
                        "00000000000000000000000000000000000000000000000000000000000000e0" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "4669727374000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000006" +
                        "5365636f6e640000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000064" +
                        "0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000006" +
                        "666f6f6261720000000000000000000000000000000000000000000000000000"
                ));
    }

    @Test
    public void assertDynamicStructsComplexTest2() {
        // [["Sam", "Na", [2, "Leonard Street", "London"], [37, ["Mobile", [61, "04", "12332112"]], ["Home", [61, "02", "99872000"]]]]]);

        // (BigInteger streetNumber, String streetName, String suburb)
        final NestedDynamicStruct2.HomeAddressStruct homeAddress = new NestedDynamicStruct2.HomeAddressStruct(BigInteger.valueOf(2), "Leonard Street", "London");

        // Mobile
        final NestedDynamicStruct2.AreaCodeAndNumber mobileNumber = new NestedDynamicStruct2.AreaCodeAndNumber(BigInteger.valueOf(61), "04", "12332112");
        final NestedDynamicStruct2.PhoneNumber mobile = new NestedDynamicStruct2.PhoneNumber("Mobile", mobileNumber);

        // Home
        final NestedDynamicStruct2.AreaCodeAndNumber homeNumber = new NestedDynamicStruct2.AreaCodeAndNumber(BigInteger.valueOf(61), "02", "99872000");
        final NestedDynamicStruct2.PhoneNumber home = new NestedDynamicStruct2.PhoneNumber("Home", homeNumber);
        final NestedDynamicStruct2.ContactNumbers contactNumbers = new NestedDynamicStruct2.ContactNumbers(BigInteger.valueOf(37), mobile, home);

        final NestedDynamicStruct2 dynamicStruct1 = new NestedDynamicStruct2("Sam", "Na", homeAddress, contactNumbers);
        assertTrue(TypeEncoder.isDynamic(dynamicStruct1));
        assertThat(TypeEncoder.encode(dynamicStruct1),
                is("0000000000000000000000000000000000000000000000000000000000000020" +
                        "0000000000000000000000000000000000000000000000000000000000000080" +
                        "00000000000000000000000000000000000000000000000000000000000000c0" +
                        "0000000000000000000000000000000000000000000000000000000000000100" +
                        "00000000000000000000000000000000000000000000000000000000000001e0" +
                        "0000000000000000000000000000000000000000000000000000000000000003" +
                        "53616d0000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "4e61000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "0000000000000000000000000000000000000000000000000000000000000060" +
                        "00000000000000000000000000000000000000000000000000000000000000a0" +
                        "000000000000000000000000000000000000000000000000000000000000000e" +
                        "4c656f6e61726420537472656574000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000006" +
                        "4c6f6e646f6e0000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000025" +
                        "0000000000000000000000000000000000000000000000000000000000000060" +
                        "00000000000000000000000000000000000000000000000000000000000001c0" +
                        "0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000080" +
                        "0000000000000000000000000000000000000000000000000000000000000006" +
                        "4d6f62696c650000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000003d" +
                        "0000000000000000000000000000000000000000000000000000000000000060" +
                        "00000000000000000000000000000000000000000000000000000000000000a0" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "3034000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000008" +
                        "3132333332313132000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000080" +
                        "0000000000000000000000000000000000000000000000000000000000000004" +
                        "486f6d6500000000000000000000000000000000000000000000000000000000" +
                        "000000000000000000000000000000000000000000000000000000000000003d" +
                        "0000000000000000000000000000000000000000000000000000000000000060" +
                        "00000000000000000000000000000000000000000000000000000000000000a0" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "3032000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000008" +
                        "3939383732303030000000000000000000000000000000000000000000000000"
                ));
    }

    @Test
    public void assertComplexStorageStructTest() {
//        final ComplexStorage complexStorage = new ComplexStorage();
        final ComplexStorage.TupleClass1 foo = new ComplexStorage.TupleClass1("Sam", "Na");
        final ComplexStorage.TupleClass2 bar = new ComplexStorage.TupleClass2("First", BigInteger.valueOf(100));
//        complexStorage.setFoo(foo);
//        complexStorage.setBar(bar);

        final CompositeDataHolderType holder = new CompositeDataHolderType(foo, bar);

        assertThat(TypeEncoder.encode(holder),
                is("0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000100" +
                        "0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000080" +
                        "0000000000000000000000000000000000000000000000000000000000000003" +
                        "53616d0000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000002" +
                        "4e61000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000040" +
                        "0000000000000000000000000000000000000000000000000000000000000064" +
                        "0000000000000000000000000000000000000000000000000000000000000005" +
                        "4669727374000000000000000000000000000000000000000000000000000000"
                ));
    }

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

    /*
    TODO: Enable once Solidity supports fixed types - see
    https://github.com/ethereum/solidity/issues/409

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
    */

    @Test
    public void testStaticBytes() {
        Bytes staticBytes = new Bytes6(new byte[]{0, 1, 2, 3, 4, 5});
        assertThat(TypeEncoder.encodeBytes(staticBytes),
                is("0001020304050000000000000000000000000000000000000000000000000000"));

        Bytes empty = new Bytes1(new byte[]{0});
        assertThat(TypeEncoder.encodeBytes(empty),
                is("0000000000000000000000000000000000000000000000000000000000000000"));

        Bytes dave = new Bytes4("dave".getBytes());
        assertThat(TypeEncoder.encodeBytes(dave),
                is("6461766500000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    public void testDynamicBytes() {
        DynamicBytes dynamicBytes = new DynamicBytes(new byte[]{0, 1, 2, 3, 4, 5});
        assertThat(TypeEncoder.encodeDynamicBytes(dynamicBytes),
                is("0000000000000000000000000000000000000000000000000000000000000006"
                        + "0001020304050000000000000000000000000000000000000000000000000000"));

        DynamicBytes empty = new DynamicBytes(new byte[]{0});
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
        assertThat(TypeEncoder.encodeAddress(address),
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
        StaticArray<Ufixed> array = new StaticArray2<>(Ufixed.class,
                new Ufixed(BigInteger.valueOf(0x2), BigInteger.valueOf(0x2)),
                new Ufixed(BigInteger.valueOf(0x8), BigInteger.valueOf(0x8))
        );

        assertThat(TypeEncoder.encodeArrayValues(array),
                is("0000000000000000000000000000000220000000000000000000000000000000"
                        + "0000000000000000000000000000000880000000000000000000000000000000"));
    }

    @Test
    public void testDynamicArray() {
        DynamicArray<Uint> array = new DynamicArray<>(Uint.class,
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
        @SuppressWarnings("unchecked")
        DynamicArray<Uint> array = new DynamicArray(Uint.class);
        assertThat(
                TypeEncoder.encodeDynamicArray(array),
                is("0000000000000000000000000000000000000000000000000000000000000000")
        );
    }

    @Test
    public void testArrayOfBytes() {
        DynamicArray<DynamicBytes> array = new DynamicArray<>(
                new DynamicBytes(Numeric.hexStringToByteArray(
                        "0x3c329ee8cd725a7f74f984cac52598eb170d731e7f3"
                                + "80d59a18aa861d2c8d6c43c880b2bfe0f3cde4efcd7"
                                + "11c010c2f1d8af5e796f06716539446f95420df4211c")),
                new DynamicBytes(Numeric.hexStringToByteArray("0xcafe0000cafe0000cafe0000")),
                new DynamicBytes(Numeric.hexStringToByteArray(
                        "0x9215c928b97e0ebeeefd10003a4e3eea23f2eb3acba"
                                + "b477eeb589d7a8874d7c5"))
        );
        DynamicArray emptyArray = DynamicArray.empty("bytes");
        DynamicArray<DynamicBytes> arrayOfEmptyBytes = new DynamicArray<>(
                new DynamicBytes(new byte[0]),
                new DynamicBytes(new byte[0])
        );

        assertThat(
                TypeEncoder.encodeDynamicArray(array),
                //  array length
                is("0000000000000000000000000000000000000000000000000000000000000003"
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
                        + "9215c928b97e0ebeeefd10003a4e3eea23f2eb3acbab477eeb589d7a8874d7c5"
                )
        );
        assertThat(
                TypeEncoder.encodeDynamicArray(emptyArray),
                //  array length
                is("0000000000000000000000000000000000000000000000000000000000000000")
        );
        assertThat(
                TypeEncoder.encodeDynamicArray(arrayOfEmptyBytes),
                //  array length
                is("0000000000000000000000000000000000000000000000000000000000000002"
                        // offset first bytes
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        // offset second bytes
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        // length first bytes
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        // length second bytes
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                )
        );
    }

    @Test
    public void testArrayOfStrings() {
        DynamicArray<Utf8String> array = new DynamicArray<>(
                new Utf8String("This string value is extra long so that it "
                        + "requires more than 32 bytes"),
                new Utf8String("abc"),
                new Utf8String(""),
                new Utf8String("web3j")
        );
        DynamicArray emptyArray = DynamicArray.empty("string");
        DynamicArray<Utf8String> arrayOfEmptyStrings = new DynamicArray<>(
                new Utf8String(""),
                new Utf8String("")
        );

        assertThat(
                TypeEncoder.encodeDynamicArray(array),
                //  array length
                is("0000000000000000000000000000000000000000000000000000000000000004"
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
                        + "776562336a000000000000000000000000000000000000000000000000000000"
                )
        );
        assertThat(
                TypeEncoder.encodeDynamicArray(emptyArray),
                //  array length
                is("0000000000000000000000000000000000000000000000000000000000000000")
        );
        assertThat(
                TypeEncoder.encodeDynamicArray(arrayOfEmptyStrings),
                //  array length
                is("0000000000000000000000000000000000000000000000000000000000000002"
                        // offset first string
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        // offset second string
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        // length first string
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        // length second string
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                )
        );
    }


    private class DynamicStructWithString extends DynamicStructType {

        public DynamicStructWithString(String data) {
            this(new Utf8String(data));
        }

        public DynamicStructWithString(Utf8String data) {
            super(Type.class, Collections.singletonList(data));
        }
    }

    private class DynamicStructWithStringAndUint extends DynamicStructType {

        public DynamicStructWithStringAndUint(String data, BigInteger intval) {
            this(new Utf8String(data), new Uint256(intval));
        }

        public DynamicStructWithStringAndUint(Utf8String data, Uint256 intval) {
            super(Type.class, Arrays.asList(data, intval));
        }
    }

    private class DynamicStruct6 extends DynamicStructType {

        public DynamicStruct6(BigInteger v1, BigInteger v2, String v3, String v4, BigInteger v5, boolean v6) {
            this(new Uint256(v1), new Uint256(v2), new Utf8String(v3), new Utf8String(v4), new Uint256(v5), new Bool(v6));
        }

        public DynamicStruct6(Uint256 v1, Uint256 v2, Utf8String v3, Utf8String v4, Uint256 v5, Bool v6) {
            super(Type.class, Arrays.asList(v1, v2, v3, v4, v5, v6));
        }
    }

    private class DynamicStruct4 extends DynamicStructType {

        public DynamicStruct4(String v1, String v2) {
            this(new Utf8String(v1), new Utf8String(v2));
        }

        public DynamicStruct4(Utf8String v1, Utf8String v2) {
            super(Type.class, Arrays.asList(v1, v2));
        }
    }

    private class StaticStruct5 extends StaticStructType {

        public StaticStruct5(BigInteger v1, BigInteger v2) {
            this(new Uint256(v1), new Uint256(v2));
        }

        public StaticStruct5(Uint256 v1, Uint256 v2) {
            super(Arrays.asList(v1, v2));
        }
    }

    private static class ComplexStorage {

        private TupleClass1 foo;
        private TupleClass2 bar;

        public void setFoo(TupleClass1 foo) {
            this.foo = foo;
        }

        public void setBar(TupleClass2 bar) {
            this.bar = bar;
        }

        public static class TupleClass1 extends DynamicStructType {
            public String id;

            public String name;

            public TupleClass1(String id, String name) {
                super(Type.class, Arrays.asList(new Utf8String(id), new Utf8String(name)));
                this.id = id;
                this.name = name;
            }
        }

        public static class TupleClass2 extends DynamicStructType {
            public String id;

            public BigInteger data;

            public TupleClass2(String id, BigInteger data) {
                super(Type.class, Arrays.asList(new Utf8String(id), new Uint256(data)));
                this.id = id;
                this.data = data;
            }

        }

    }


}

