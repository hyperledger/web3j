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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.primitive.PrimitiveType;
import org.web3j.utils.Numeric;

import static org.web3j.abi.datatypes.Type.MAX_BIT_LENGTH;
import static org.web3j.abi.datatypes.Type.MAX_BYTE_LENGTH;

/**
 * Ethereum Contract Application Binary Interface (ABI) encoding for types. Further details are
 * available <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 */
public class TypeEncoder {

    private TypeEncoder() {}

    static boolean isDynamic(Type parameter) {
        return parameter instanceof DynamicBytes
                || parameter instanceof Utf8String
                || parameter instanceof DynamicArray;
    }

    @SuppressWarnings("unchecked")
    public static String encode(Type parameter) {
        if (parameter instanceof NumericType) {
            return encodeNumeric(((NumericType) parameter));
        } else if (parameter instanceof Address) {
            return encodeAddress((Address) parameter);
        } else if (parameter instanceof Bool) {
            return encodeBool((Bool) parameter);
        } else if (parameter instanceof Bytes) {
            return encodeBytes((Bytes) parameter);
        } else if (parameter instanceof DynamicBytes) {
            return encodeDynamicBytes((DynamicBytes) parameter);
        } else if (parameter instanceof Utf8String) {
            return encodeString((Utf8String) parameter);
        } else if (parameter instanceof StaticArray) {
            return encodeArrayValues((StaticArray) parameter);
        } else if (parameter instanceof DynamicStruct) {
            return encodeDynamicStruct((DynamicStruct) parameter);
        } else if (parameter instanceof DynamicArray) {
            return encodeDynamicArray((DynamicArray) parameter);
        } else if (parameter instanceof PrimitiveType) {
            return encode(((PrimitiveType) parameter).toSolidityType());
        } else {
            throw new UnsupportedOperationException(
                    "Type cannot be encoded: " + parameter.getClass());
        }
    }

    static String encodeAddress(Address address) {
        return encodeNumeric(address.toUint());
    }

    static String encodeNumeric(NumericType numericType) {
        byte[] rawValue = toByteArray(numericType);
        byte paddingValue = getPaddingValue(numericType);
        byte[] paddedRawValue = new byte[MAX_BYTE_LENGTH];
        if (paddingValue != 0) {
            for (int i = 0; i < paddedRawValue.length; i++) {
                paddedRawValue[i] = paddingValue;
            }
        }

        System.arraycopy(
                rawValue, 0, paddedRawValue, MAX_BYTE_LENGTH - rawValue.length, rawValue.length);
        return Numeric.toHexStringNoPrefix(paddedRawValue);
    }

    private static byte getPaddingValue(NumericType numericType) {
        if (numericType.getValue().signum() == -1) {
            return (byte) 0xff;
        } else {
            return 0;
        }
    }

    private static byte[] toByteArray(NumericType numericType) {
        BigInteger value = numericType.getValue();
        if (numericType instanceof Ufixed || numericType instanceof Uint) {
            if (value.bitLength() == MAX_BIT_LENGTH) {
                // As BigInteger is signed, if we have a 256 bit value, the resultant byte array
                // will contain a sign byte in it's MSB, which we should ignore for this unsigned
                // integer type.
                byte[] byteArray = new byte[MAX_BYTE_LENGTH];
                System.arraycopy(value.toByteArray(), 1, byteArray, 0, MAX_BYTE_LENGTH);
                return byteArray;
            }
        }
        return value.toByteArray();
    }

    static String encodeBool(Bool value) {
        byte[] rawValue = new byte[MAX_BYTE_LENGTH];
        if (value.getValue()) {
            rawValue[rawValue.length - 1] = 1;
        }
        return Numeric.toHexStringNoPrefix(rawValue);
    }

    static String encodeBytes(BytesType bytesType) {
        byte[] value = bytesType.getValue();
        int length = value.length;
        int mod = length % MAX_BYTE_LENGTH;

        byte[] dest;
        if (mod != 0) {
            int padding = MAX_BYTE_LENGTH - mod;
            dest = new byte[length + padding];
            System.arraycopy(value, 0, dest, 0, length);
        } else {
            dest = value;
        }
        return Numeric.toHexStringNoPrefix(dest);
    }

    static String encodeDynamicBytes(DynamicBytes dynamicBytes) {
        int size = dynamicBytes.getValue().length;
        String encodedLength = encode(new Uint(BigInteger.valueOf(size)));
        String encodedValue = encodeBytes(dynamicBytes);

        StringBuilder result = new StringBuilder();
        result.append(encodedLength);
        result.append(encodedValue);
        return result.toString();
    }

    static String encodeString(Utf8String string) {
        byte[] utfEncoded = string.getValue().getBytes(StandardCharsets.UTF_8);
        return encodeDynamicBytes(new DynamicBytes(utfEncoded));
    }

    static <T extends Type> String encodeArrayValues(Array<T> value) {
        StringBuilder result = new StringBuilder();
        for (Type type : value.getValue()) {
            result.append(encode(type));
        }
        return result.toString();
    }

    static String encodeDynamicStruct(final DynamicStruct value) {
        String encodedValues = encodeDynamicStructValues(value);

        StringBuilder result = new StringBuilder();
        result.append(encodedValues);
        return result.toString();
    }

    private static String encodeDynamicStructValues(final DynamicStruct value) {
        int staticSize = 0;
        for (int i = 0; i < value.getValue().size(); ++i) {
            final Type type = value.getValue().get(i);
            if (isDynamic(type)) {
                staticSize += 32;
            } else {
                staticSize += type.bytes32PaddedLength();
            }
        }
        int dynamicOffset = staticSize;
        final List<String> offsetsAndStaticValues = new ArrayList<>();
        final List<String> dynamicValues = new ArrayList<>();
        for (int i = 0; i < value.getValue().size(); ++i) {
            final Type type = value.getValue().get(i);
            if (isDynamic(type)) {
                offsetsAndStaticValues.add(
                        Numeric.toHexStringNoPrefix(
                                Numeric.toBytesPadded(
                                        new BigInteger(Long.toString(dynamicOffset)),
                                        MAX_BYTE_LENGTH)));
                dynamicValues.add(encode(type));
                dynamicOffset += type.bytes32PaddedLength();
            } else {
                offsetsAndStaticValues.add(encode(value.getValue().get(i)));
            }
        }
        final List<String> data = new ArrayList<>();
        data.addAll(offsetsAndStaticValues);
        data.addAll(dynamicValues);
        return String.join("", data);
    }

    static <T extends Type> String encodeDynamicArray(DynamicArray<T> value) {
        int size = value.getValue().size();
        String encodedLength = encode(new Uint(BigInteger.valueOf(size)));
        String valuesOffsets = encodeArrayValuesOffsets(value);
        String encodedValues = encodeArrayValues(value);

        StringBuilder result = new StringBuilder();
        result.append(encodedLength);
        result.append(valuesOffsets);
        result.append(encodedValues);
        return result.toString();
    }

    private static <T extends Type> String encodeArrayValuesOffsets(DynamicArray<T> value) {
        StringBuilder result = new StringBuilder();
        boolean arrayOfBytes =
                !value.getValue().isEmpty() && value.getValue().get(0) instanceof DynamicBytes;
        boolean arrayOfString =
                !value.getValue().isEmpty() && value.getValue().get(0) instanceof Utf8String;
        if (arrayOfBytes || arrayOfString) {
            long offset = 0;
            for (int i = 0; i < value.getValue().size(); i++) {
                if (i == 0) {
                    offset = value.getValue().size() * MAX_BYTE_LENGTH;
                } else {
                    int bytesLength =
                            arrayOfBytes
                                    ? ((byte[]) value.getValue().get(i - 1).getValue()).length
                                    : ((String) value.getValue().get(i - 1).getValue()).length();
                    int numberOfWords = (bytesLength + MAX_BYTE_LENGTH - 1) / MAX_BYTE_LENGTH;
                    int totalBytesLength = numberOfWords * MAX_BYTE_LENGTH;
                    offset += totalBytesLength + MAX_BYTE_LENGTH;
                }
                result.append(
                        Numeric.toHexStringNoPrefix(
                                Numeric.toBytesPadded(
                                        new BigInteger(Long.toString(offset)), MAX_BYTE_LENGTH)));
            }
        }
        return result.toString();
    }
}
