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

import org.web3j.abi.datatypes.*;
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
                || parameter instanceof DynamicArray
                || parameter instanceof DynamicStruct;
    }

    // May not be the best place for this, but we'll move things around later.
    public static boolean isDynamic(TypeReference typeReference) {
        final Class typeClass = ((Class) typeReference.getType());

        // TODO: do static struct first.
        if (typeClass.isAssignableFrom(StaticStruct.class)) {
            // A static struct with at least one dynamic member is deemed dynamic.
            //            final StaticStructType t = (StaticStructType) parameter;
            //            for (Type type : t.getValue()) {
            //                if (isDynamic(type)) {
            //                    return true;
            //                }
            //            }
            //            return false;
            throw new UnsupportedOperationException("TODO: add support for static structs!");
        }

        return typeClass.isAssignableFrom(DynamicBytes.class)
                || typeClass.isAssignableFrom(Utf8String.class)
                || typeClass.isAssignableFrom(DynamicArray.class)
                || DynamicStruct.class.isAssignableFrom(typeClass);
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
        } else if (parameter instanceof DynamicStruct) {
            if (parameter instanceof CompositeDataHolder) {
                return encodeCompositeDataHolder((CompositeDataHolder) parameter);
            } else {
                return encodeDynamicStruct((DynamicStruct) parameter);
            }
        } else if (parameter instanceof DynamicBytes) {
            return encodeDynamicBytes((DynamicBytes) parameter);
        } else if (parameter instanceof Utf8String) {
            return encodeString((Utf8String) parameter);
        } else if (parameter instanceof StaticArray) {
            return encodeArrayValues((StaticArray) parameter);
        } else if (parameter instanceof DynamicArray) {
            return encodeDynamicArray((DynamicArray) parameter);
        } else if (parameter instanceof PrimitiveType) {
            return encode(((PrimitiveType) parameter).toSolidityType());
        } else {
            throw new UnsupportedOperationException(
                    "Type cannot be encoded: " + parameter.getClass());
        }
    }

    private static String encodeCompositeDataHolder(CompositeDataHolder value) {
        return encodeDynamicStruct(value, false);
    }

    private static String encodeDynamicStruct(DynamicStruct value) {
        return encodeDynamicStruct(value, true);
    }

    /**
     * A struct is encoded by writing a preamble (0x20), followed by offsets for dynamic data or
     * in-place encoding for static data.
     *
     * @param value the struct value.
     * @param isTopLevelStruct wheter this is a top-level (not a nested/embedded).
     * @return the encoding of the struct.
     */
    private static String encodeDynamicStruct(DynamicStruct value, boolean isTopLevelStruct) {
        final StringBuilder structOffsetsAndStaticData = new StringBuilder();
        if (isTopLevelStruct) {
            // 0000000000000000000000000000000000000000000000000000000000000020
            structOffsetsAndStaticData.append(
                    encode(new Uint(BigInteger.valueOf(MAX_BYTE_LENGTH))));
        }

        // Struct args.
        final int numStructArgs = value.getValue().size();

        // The first block is offset by 32 bytes to allow for the preamble.
        final int currentBlockOffset = isTopLevelStruct ? MAX_BYTE_LENGTH : 0;
        int dynamicDataOffset = numStructArgs * MAX_BYTE_LENGTH + currentBlockOffset;

        final StringBuilder structDynamicData = new StringBuilder();
        // We want to encode the data in a BFS-manner, encoding the current struct meta-data
        // (offsets for dynamic values,
        // and in-place encoding for static values), and then follow it with the encoded data
        // segment for the dynamic values.
        for (Type structFieldType : value.getValue())
            if (isDynamic(structFieldType)) {
                String encodedData = "";
                if (DynamicStruct.class.isAssignableFrom(structFieldType.getClass())) {
                    // Embedded struct.
                    encodedData = encodeDynamicStruct((DynamicStruct) structFieldType, false);
                } else {
                    encodedData = encode(structFieldType);
                }

                structOffsetsAndStaticData.append(
                        encode(
                                new Uint(
                                        BigInteger.valueOf(
                                                dynamicDataOffset
                                                        - currentBlockOffset)))); // The offset.
                structDynamicData.append(
                        encodedData); // Add the dynamic data to another stringbuilder for appending
                // later on.
                dynamicDataOffset += encodedData.length() / 2;

            } else {
                // Static type - encode and append in-place.
                structOffsetsAndStaticData.append(encode(structFieldType));
            }
        // Finally
        return structOffsetsAndStaticData.toString() + structDynamicData.toString();
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
