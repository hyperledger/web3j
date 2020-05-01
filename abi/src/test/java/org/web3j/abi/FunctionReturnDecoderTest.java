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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.web3j.abi.TypeReference.StaticArrayTypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes16;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.StaticArray3;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionReturnDecoderTest {

    @Test
    public void testSimpleFunctionDecode() {
        final Function function =
                new Function(
                        "test",
                        Collections.<Type<?>>emptyList(),
                        Collections.singletonList(new TypeReference<Uint>() {}));

        assertEquals(
                FunctionReturnDecoder.decode(
                        "0x0000000000000000000000000000000000000000000000000000000000000037",
                        function.getOutputParameters()),
                (Collections.singletonList(new Uint(BigInteger.valueOf(55)))));
    }

    @Test
    public void testSimpleFunctionStringResultDecode() {
        final Function function =
                new Function(
                        "simple",
                        Arrays.asList(),
                        Collections.singletonList(new TypeReference<Utf8String>() {}));

        final List<Type<?>> utf8Strings =
                FunctionReturnDecoder.decode(
                        "0x0000000000000000000000000000000000000000000000000000000000000020"
                                + "000000000000000000000000000000000000000000000000000000000000000d"
                                + "6f6e65206d6f72652074696d6500000000000000000000000000000000000000",
                        function.getOutputParameters());

        assertEquals(utf8Strings.get(0).getValue(), ("one more time"));
    }

    @Test
    public void testFunctionEmptyStringResultDecode() {
        final Function function =
                new Function(
                        "test",
                        Collections.emptyList(),
                        Collections.singletonList(new TypeReference<Utf8String>() {}));

        final List<Type<?>> utf8Strings =
                FunctionReturnDecoder.decode(
                        "0x0000000000000000000000000000000000000000000000000000000000000020"
                                + "0000000000000000000000000000000000000000000000000000000000000000",
                        function.getOutputParameters());

        assertEquals(utf8Strings.get(0).getValue(), (""));
    }

    @Test
    public void testMultipleResultFunctionDecode() {
        final Function function =
                new Function(
                        "test",
                        Collections.<Type<?>>emptyList(),
                        Arrays.asList(new TypeReference<Uint>() {}, new TypeReference<Uint>() {}));

        assertEquals(
                FunctionReturnDecoder.decode(
                        "0x0000000000000000000000000000000000000000000000000000000000000037"
                                + "0000000000000000000000000000000000000000000000000000000000000007",
                        function.getOutputParameters()),
                (Arrays.asList(new Uint(BigInteger.valueOf(55)), new Uint(BigInteger.valueOf(7)))));
    }

    @Test
    public void testDecodeMultipleStringValues() {
        final Function function =
                new Function(
                        "function",
                        Collections.<Type<?>>emptyList(),
                        Arrays.asList(
                                new TypeReference<Utf8String>() {},
                                new TypeReference<Utf8String>() {},
                                new TypeReference<Utf8String>() {},
                                new TypeReference<Utf8String>() {}));

        assertEquals(
                FunctionReturnDecoder.decode(
                        "0x0000000000000000000000000000000000000000000000000000000000000080"
                                + "00000000000000000000000000000000000000000000000000000000000000c0"
                                + "0000000000000000000000000000000000000000000000000000000000000100"
                                + "0000000000000000000000000000000000000000000000000000000000000140"
                                + "0000000000000000000000000000000000000000000000000000000000000004"
                                + "6465663100000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000000000000000000004"
                                + "6768693100000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000000000000000000004"
                                + "6a6b6c3100000000000000000000000000000000000000000000000000000000"
                                + "0000000000000000000000000000000000000000000000000000000000000004"
                                + "6d6e6f3200000000000000000000000000000000000000000000000000000000",
                        function.getOutputParameters()),
                (Arrays.asList(
                        new Utf8String("def1"), new Utf8String("ghi1"),
                        new Utf8String("jkl1"), new Utf8String("mno2"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDecodeStaticArrayValue() {
        final List<TypeReference<Type<?>>> outputParameters = new ArrayList<>(1);
        outputParameters.add(
                (TypeReference) new StaticArrayTypeReference<StaticArray<Uint256>>(2) {});
        outputParameters.add((TypeReference) new TypeReference<Uint256>() {});

        final List<Type<?>> decoded =
                FunctionReturnDecoder.decode(
                        "0x0000000000000000000000000000000000000000000000000000000000000037"
                                + "0000000000000000000000000000000000000000000000000000000000000001"
                                + "000000000000000000000000000000000000000000000000000000000000000a",
                        outputParameters);

        final StaticArray2<Uint256> uint256StaticArray2 =
                new StaticArray2<>(
                        Uint256.class,
                        new Uint256(BigInteger.valueOf(55)),
                        new Uint256(BigInteger.ONE));

        final List<Type<?>> expected =
                Arrays.asList(uint256StaticArray2, new Uint256(BigInteger.TEN));
        assertEquals(decoded, expected);
    }

    @Test
    public void testVoidResultFunctionDecode() {
        final Function function =
                new Function("test", Collections.emptyList(), Collections.emptyList());

        assertEquals(
                FunctionReturnDecoder.decode("0x", function.getOutputParameters()),
                (Collections.emptyList()));
    }

    @Test
    public void testEmptyResultFunctionDecode() {
        final Function function =
                new Function(
                        "test",
                        Collections.emptyList(),
                        Collections.singletonList(new TypeReference<Uint>() {}));

        assertEquals(
                FunctionReturnDecoder.decode("0x", function.getOutputParameters()),
                (Collections.emptyList()));
    }

    @Test
    public void testDecodeIndexedUint256Value() {
        final Uint256 value = new Uint256(BigInteger.TEN);
        final String encoded = TypeEncoder.encodeNumeric(value);

        assertEquals(
                FunctionReturnDecoder.decodeIndexedValue(encoded, new TypeReference<Uint256>() {}),
                (value));
    }

    @Test
    public void testDecodeIndexedStringValue() {
        final Utf8String string = new Utf8String("some text");
        final String encoded = TypeEncoder.encodeString(string);
        final String hash = Hash.sha3(encoded);

        assertEquals(
                FunctionReturnDecoder.decodeIndexedValue(hash, new TypeReference<Utf8String>() {}),
                (new Bytes32(Numeric.hexStringToByteArray(hash))));
    }

    @Test
    public void testDecodeIndexedBytes32Value() {
        final String rawInput =
                "0x1234567890123456789012345678901234567890123456789012345678901234";
        final byte[] rawInputBytes = Numeric.hexStringToByteArray(rawInput);

        assertEquals(
                FunctionReturnDecoder.decodeIndexedValue(rawInput, new TypeReference<Bytes32>() {}),
                (new Bytes32(rawInputBytes)));
    }

    @Test
    public void testDecodeIndexedBytes16Value() {
        final String rawInput =
                "0x1234567890123456789012345678901200000000000000000000000000000000";
        final byte[] rawInputBytes = Numeric.hexStringToByteArray(rawInput.substring(0, 34));

        assertEquals(
                FunctionReturnDecoder.decodeIndexedValue(rawInput, new TypeReference<Bytes16>() {}),
                (new Bytes16(rawInputBytes)));
    }

    @Test
    public void testDecodeIndexedDynamicBytesValue() {
        final DynamicBytes bytes = new DynamicBytes(new byte[] {1, 2, 3, 4, 5});
        final String encoded = TypeEncoder.encodeDynamicBytes(bytes);
        final String hash = Hash.sha3(encoded);

        assertEquals(
                FunctionReturnDecoder.decodeIndexedValue(
                        hash, new TypeReference<DynamicBytes>() {}),
                (new Bytes32(Numeric.hexStringToByteArray(hash))));
    }

    @Test
    public void testDecodeIndexedDynamicArrayValue() {
        final DynamicArray<Uint256> array =
                new DynamicArray<>(Uint256.class, new Uint256(BigInteger.TEN));

        final String encoded = TypeEncoder.encodeDynamicArray(array);
        final String hash = Hash.sha3(encoded);

        assertEquals(
                FunctionReturnDecoder.decodeIndexedValue(
                        hash, new TypeReference<DynamicArray<Type<?>>>() {}),
                (new Bytes32(Numeric.hexStringToByteArray(hash))));
    }

    @Test
    public void testDecodeStaticStruct() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000064";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getBarFunction.getOutputParameters()),
                Collections.singletonList(
                        new AbiV2TestFixture.Bar(BigInteger.ONE, BigInteger.valueOf(100))));
    }

    @Test
    public void testDecodeDynamicStruct() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getFooFunction.getOutputParameters()),
                Collections.singletonList(new AbiV2TestFixture.Foo("id", "name")));
    }

    @Test
    public void testDecodeDynamicStruct2() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getBozFunction.getOutputParameters()),
                Collections.singletonList(new AbiV2TestFixture.Boz(BigInteger.ONE, "id")));
    }

    @Test
    public void testDecodeStaticStructNested() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000001"
                        + "000000000000000000000000000000000000000000000000000000000000000a"
                        + "0000000000000000000000000000000000000000000000000000000000000001";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getFuzzFunction.getOutputParameters()),
                Collections.singletonList(
                        new AbiV2TestFixture.Fuzz(
                                new AbiV2TestFixture.Bar(BigInteger.ONE, BigInteger.TEN),
                                BigInteger.ONE)));
    }

    @Test
    public void testDecodeMultipleStaticStructNested() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000001"
                        + "000000000000000000000000000000000000000000000000000000000000000a"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "000000000000000000000000000000000000000000000000000000000000000a"
                        + "0000000000000000000000000000000000000000000000000000000000000001";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getFuzzFuzzFunction.getOutputParameters()),
                Arrays.asList(
                        new AbiV2TestFixture.Fuzz(
                                new AbiV2TestFixture.Bar(BigInteger.ONE, BigInteger.TEN),
                                BigInteger.ONE),
                        new AbiV2TestFixture.Fuzz(
                                new AbiV2TestFixture.Bar(BigInteger.ONE, BigInteger.TEN),
                                BigInteger.ONE)));
    }

    @Test
    public void testDynamicStructNestedEncode() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getNuuFunction.getOutputParameters()),
                Collections.singletonList(
                        new AbiV2TestFixture.Nuu(new AbiV2TestFixture.Foo("id", "name"))));
    }

    @Test
    public void testDecodeTupleDynamicStructNested() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "000000000000000000000000000000000000000000000000000000000000000a"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getFooBarFunction.getOutputParameters()),
                Arrays.asList(
                        new AbiV2TestFixture.Foo("id", "name"),
                        new AbiV2TestFixture.Bar(BigInteger.ONE, BigInteger.TEN)));
    }

    @Test
    public void testDecodeMultipleDynamicStruct() {
        String rawInput =
                "0x00000000000000000000000000000000000000000000000000000000000000a0"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "000000000000000000000000000000000000000000000000000000000000000a"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "000000000000000000000000000000000000000000000000000000000000000b"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getFooBarBarFunction.getOutputParameters()),
                Arrays.asList(
                        new AbiV2TestFixture.Foo("id", "name"),
                        new AbiV2TestFixture.Bar(BigInteger.ONE, BigInteger.TEN),
                        new AbiV2TestFixture.Bar(BigInteger.valueOf(2), BigInteger.valueOf(11))));
    }

    @Test
    public void testDecodeMultipleDynamicStruct2() {
        String rawInput =
                "0x00000000000000000000000000000000000000000000000000000000000000c0"
                        + "0000000000000000000000000000000000000000000000000000000000000180"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "000000000000000000000000000000000000000000000000000000000000000a"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "000000000000000000000000000000000000000000000000000000000000000b"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getFooFooBarBarFunction.getOutputParameters()),
                Arrays.asList(
                        new AbiV2TestFixture.Foo("id", "name"),
                        new AbiV2TestFixture.Foo("id", "name"),
                        new AbiV2TestFixture.Bar(BigInteger.ONE, BigInteger.TEN),
                        new AbiV2TestFixture.Bar(BigInteger.valueOf(2), BigInteger.valueOf(11))));
    }

    @Test
    public void testDecodeDynamicNested3() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getNarFunction.getOutputParameters()),
                Arrays.asList(
                        new AbiV2TestFixture.Nar(
                                new AbiV2TestFixture.Nuu(
                                        new AbiV2TestFixture.Foo("4", "nestedFoo")))));
    }

    @Test
    public void testDecodeMultipleDynamicStaticNestedStructs() {
        String rawInput =
                "0000000000000000000000000000000000000000000000000000000000000240"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "00000000000000000000000000000000000000000000000000000000000004d1"
                        + "0000000000000000000000000000000000000000000000000000000000000079"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "0000000000000000000000000000000000000000000000000000000000000340"
                        + "0000000000000000000000000000000000000000000000000000000000000400"
                        + "00000000000000000000000000000000000000000000000000000000000004d1"
                        + "0000000000000000000000000000000000000000000000000000000000000079"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "0000000000000000000000000000000000000000000000000000000000000500"
                        + "00000000000000000000000000000000000000000000000000000000000005a0"
                        + "00000000000000000000000000000000000000000000000000000000000004d1"
                        + "0000000000000000000000000000000000000000000000000000000000000079"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6861686100000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "686f686f00000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6861686100000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "686f686f00000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput,
                        AbiV2TestFixture.getNarBarBarFuzzFooNarFuzzNuuFooFuzzFunction
                                .getOutputParameters()),
                Arrays.asList(
                        new AbiV2TestFixture.Nar(
                                new AbiV2TestFixture.Nuu(
                                        new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                        new AbiV2TestFixture.Bar(BigInteger.valueOf(123), BigInteger.valueOf(123)),
                        new AbiV2TestFixture.Bar(BigInteger.valueOf(123), BigInteger.valueOf(123)),
                        new AbiV2TestFixture.Fuzz(
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(1233), BigInteger.valueOf(121)),
                                BigInteger.valueOf(2)),
                        new AbiV2TestFixture.Foo("haha", "hoho"),
                        new AbiV2TestFixture.Nar(
                                new AbiV2TestFixture.Nuu(
                                        new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                        new AbiV2TestFixture.Fuzz(
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(1233), BigInteger.valueOf(121)),
                                BigInteger.valueOf(2)),
                        new AbiV2TestFixture.Nuu(new AbiV2TestFixture.Foo("", "")),
                        new AbiV2TestFixture.Foo("haha", "hoho"),
                        new AbiV2TestFixture.Fuzz(
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(1233), BigInteger.valueOf(121)),
                                BigInteger.valueOf(2))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDecodeDynamicStructDynamicArray() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput,
                        AbiV2TestFixture.getFooDynamicArrayFunction.getOutputParameters()),
                Arrays.asList(
                        new DynamicArray(
                                AbiV2TestFixture.Foo.class,
                                new AbiV2TestFixture.Foo("id", "name"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDecodeStaticStructStaticArray() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getBarStaticArrayFunction.getOutputParameters()),
                Arrays.asList(
                        new StaticArray3(
                                AbiV2TestFixture.Bar.class,
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(0), BigInteger.valueOf(0)),
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(0), BigInteger.valueOf(0)))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDecodeDynamicStructStaticArray() {
        String rawInput =
                "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000220"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput, AbiV2TestFixture.getNarStaticArrayFunction.getOutputParameters()),
                Arrays.asList(
                        new StaticArray3(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(new AbiV2TestFixture.Foo("", ""))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDecodeDynamicStructDynamicArray2() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000260"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput,
                        AbiV2TestFixture.getNarDynamicArrayFunction.getOutputParameters()),
                Arrays.asList(
                        new DynamicArray(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("", ""))))));
    }

    @Test
    public void testDecodeMultipleDynamicStructStaticDynamicArrays() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000140"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000460"
                        + "0000000000000000000000000000000000000000000000000000000000000560"
                        + "00000000000000000000000000000000000000000000000000000000000008a0"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000220"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000260"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000120"
                        + "00000000000000000000000000000000000000000000000000000000000001e0"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput,
                        AbiV2TestFixture.getNarBarFooNarFooDynamicArrayFunction
                                .getOutputParameters()),
                Arrays.asList(
                        new StaticArray3<>(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(new AbiV2TestFixture.Foo("", ""))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo")))),
                        new StaticArray3<>(
                                AbiV2TestFixture.Bar.class,
                                new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO),
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO)),
                        new DynamicArray<>(
                                AbiV2TestFixture.Foo.class, new AbiV2TestFixture.Foo("id", "name")),
                        new DynamicArray<>(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("", "")))),
                        new StaticArray3<>(
                                AbiV2TestFixture.Foo.class,
                                new AbiV2TestFixture.Foo("id", "name"),
                                new AbiV2TestFixture.Foo("id", "name"),
                                new AbiV2TestFixture.Foo("id", "name"))));
    }

    @Test
    public void testDecodeStructMultipleDynamicStaticArray() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000140"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000480"
                        + "0000000000000000000000000000000000000000000000000000000000000580"
                        + "00000000000000000000000000000000000000000000000000000000000008c0"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000260"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000260"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput,
                        AbiV2TestFixture.idNarBarFooNarFooArraysFunction.getOutputParameters()),
                Arrays.asList(
                        new DynamicArray<>(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("", "")))),
                        new StaticArray3<>(
                                AbiV2TestFixture.Bar.class,
                                new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO),
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO)),
                        new DynamicArray<>(
                                AbiV2TestFixture.Foo.class, new AbiV2TestFixture.Foo("id", "name")),
                        new DynamicArray<>(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("", "")))),
                        new DynamicArray<>(
                                AbiV2TestFixture.Foo.class,
                                new AbiV2TestFixture.Foo("id", "name"))));
    }

    @Test
    public void testDecodeStructMultipleDynamicStaticArray2() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000140"
                        + "0000000000000000000000000000000000000000000000000000000000000460"
                        + "0000000000000000000000000000000000000000000000000000000000000560"
                        + "00000000000000000000000000000000000000000000000000000000000008a0"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000220"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000260"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000120"
                        + "00000000000000000000000000000000000000000000000000000000000001e0"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput,
                        AbiV2TestFixture.idBarNarFooNarFooArraysFunction.getOutputParameters()),
                Arrays.asList(
                        new StaticArray3<>(
                                AbiV2TestFixture.Bar.class,
                                new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO),
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO)),
                        new StaticArray3<>(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(new AbiV2TestFixture.Foo("", ""))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo")))),
                        new DynamicArray<>(
                                AbiV2TestFixture.Foo.class, new AbiV2TestFixture.Foo("id", "name")),
                        new DynamicArray<>(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("", "")))),
                        new StaticArray3<>(
                                AbiV2TestFixture.Foo.class,
                                new AbiV2TestFixture.Foo("id", "name"),
                                new AbiV2TestFixture.Foo("id", "name"),
                                new AbiV2TestFixture.Foo("id", "name"))));
    }

    @Test
    public void testDecodeStructMultipleDynamicStaticArray3() {
        String rawInput =
                "0x00000000000000000000000000000000000000000000000000000000000000a0"
                        + "00000000000000000000000000000000000000000000000000000000000003c0"
                        + "00000000000000000000000000000000000000000000000000000000000004a0"
                        + "00000000000000000000000000000000000000000000000000000000000005a0"
                        + "00000000000000000000000000000000000000000000000000000000000008e0"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000220"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000000c"
                        + "0000000000000000000000000000000000000000000000000000000000000021"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000003"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000160"
                        + "0000000000000000000000000000000000000000000000000000000000000260"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000001"
                        + "3400000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000009"
                        + "6e6573746564466f6f0000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000060"
                        + "0000000000000000000000000000000000000000000000000000000000000120"
                        + "00000000000000000000000000000000000000000000000000000000000001e0"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000040"
                        + "0000000000000000000000000000000000000000000000000000000000000080"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "6964000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000004"
                        + "6e616d6500000000000000000000000000000000000000000000000000000000";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput,
                        AbiV2TestFixture.idNarBarFooNarFooArraysFunction2.getOutputParameters()),
                Arrays.asList(
                        new StaticArray3<>(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(new AbiV2TestFixture.Foo("", ""))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo")))),
                        new DynamicArray<>(
                                AbiV2TestFixture.Bar.class,
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(12), BigInteger.valueOf(33)),
                                new AbiV2TestFixture.Bar(BigInteger.ZERO, BigInteger.ZERO)),
                        new DynamicArray<>(
                                AbiV2TestFixture.Foo.class, new AbiV2TestFixture.Foo("id", "name")),
                        new DynamicArray<>(
                                AbiV2TestFixture.Nar.class,
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("4", "nestedFoo"))),
                                new AbiV2TestFixture.Nar(
                                        new AbiV2TestFixture.Nuu(
                                                new AbiV2TestFixture.Foo("", "")))),
                        new StaticArray3<>(
                                AbiV2TestFixture.Foo.class,
                                new AbiV2TestFixture.Foo("id", "name"),
                                new AbiV2TestFixture.Foo("id", "name"),
                                new AbiV2TestFixture.Foo("id", "name"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDecodeStaticStructDynamicArray() {
        String rawInput =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                        + "0000000000000000000000000000000000000000000000000000000000000002"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b"
                        + "000000000000000000000000000000000000000000000000000000000000007b";

        assertEquals(
                FunctionReturnDecoder.decode(
                        rawInput,
                        AbiV2TestFixture.getBarDynamicArrayFunction.getOutputParameters()),
                Arrays.asList(
                        new DynamicArray(
                                AbiV2TestFixture.Bar.class,
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(123), BigInteger.valueOf(123)),
                                new AbiV2TestFixture.Bar(
                                        BigInteger.valueOf(123), BigInteger.valueOf(123)))));
    }
}
