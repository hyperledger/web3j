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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.utils.Numeric;
import org.web3j.utils.Strings;

import static org.web3j.abi.TypeDecoder.MAX_BYTE_LENGTH_FOR_HEX_STRING;

/**
 * Ethereum Contract Application Binary Interface (ABI) encoding for functions. Further details are
 * available <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 */
public class DefaultFunctionReturnDecoder extends FunctionReturnDecoder {

    public List<Type> decodeFunctionResult(
            String rawInput, List<TypeReference<Type>> outputParameters) {

        String input = Numeric.cleanHexPrefix(rawInput);

        if (Strings.isEmpty(input)) {
            return Collections.emptyList();
        } else {
            return build(input, outputParameters);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Type> Type decodeEventParameter(
            String rawInput, TypeReference<T> typeReference) {

        String input = Numeric.cleanHexPrefix(rawInput);

        try {
            Class<T> type = typeReference.getClassType();

            if (Bytes.class.isAssignableFrom(type)) {
                Class<Bytes> bytesClass = (Class<Bytes>) Class.forName(type.getName());
                return TypeDecoder.decodeBytes(input, bytesClass);
            } else if (Array.class.isAssignableFrom(type)
                    || BytesType.class.isAssignableFrom(type)
                    || Utf8String.class.isAssignableFrom(type)) {
                return TypeDecoder.decodeBytes(input, Bytes32.class);
            } else {
                return TypeDecoder.decode(input, type);
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }

    private static List<Type> build(String input, List<TypeReference<Type>> outputParameters) {
        List<Type> results = new ArrayList<>(outputParameters.size());

        int offset = 0;
        for (TypeReference<?> typeReference : outputParameters) {
            try {
                int hexStringDataOffset = getDataOffset(input, offset, typeReference);

                @SuppressWarnings("unchecked")
                Class<Type> classType = (Class<Type>) typeReference.getClassType();

                Type result;
                if (DynamicStruct.class.isAssignableFrom(classType)) {
                    result =
                            TypeDecoder.decodeDynamicStruct(
                                    input, hexStringDataOffset, typeReference);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;

                } else if (DynamicArray.class.isAssignableFrom(classType)) {
                    result =
                            TypeDecoder.decodeDynamicArray(
                                    input, hexStringDataOffset, typeReference);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;

                } else if (typeReference instanceof TypeReference.StaticArrayTypeReference) {
                    int length = ((TypeReference.StaticArrayTypeReference) typeReference).getSize();
                    result =
                            TypeDecoder.decodeStaticArray(
                                    input, hexStringDataOffset, typeReference, length);
                    offset += length * MAX_BYTE_LENGTH_FOR_HEX_STRING;

                } else if (StaticStruct.class.isAssignableFrom(classType)) {
                    result =
                            TypeDecoder.decodeStaticStruct(
                                    input, hexStringDataOffset, typeReference);
                    offset +=
                            staticStructCanonicalFieldsCount(classType)
                                    * MAX_BYTE_LENGTH_FOR_HEX_STRING;
                } else if (StaticArray.class.isAssignableFrom(classType)) {
                    int length =
                            Integer.parseInt(
                                    classType
                                            .getSimpleName()
                                            .substring(StaticArray.class.getSimpleName().length()));
                    result =
                            TypeDecoder.decodeStaticArray(
                                    input, hexStringDataOffset, typeReference, length);
                    if (isParametrizedTypeStruct(typeReference) && offset == 0) {
                        offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;
                    } else if (isParametrizedTypeStruct(typeReference)) {
                        offset +=
                                (int)
                                                staticStructCanonicalFieldsCount(
                                                        Utils.getParameterizedTypeFromArray(
                                                                typeReference))
                                        * length
                                        * MAX_BYTE_LENGTH_FOR_HEX_STRING;
                    } else {
                        offset += length * MAX_BYTE_LENGTH_FOR_HEX_STRING;
                    }
                } else {
                    result = TypeDecoder.decode(input, hexStringDataOffset, classType);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;
                }
                results.add(result);

            } catch (ClassNotFoundException e) {
                throw new UnsupportedOperationException("Invalid class reference provided", e);
            }
        }
        return results;
    }

    /**
     * Returns number of canonical fields in a static struct. Example: struct Baz { Struct Bar { int
     * a, int b }, int c } will return three fields count.
     *
     * @param classType
     * @return
     */
    public static long staticStructCanonicalFieldsCount(Class<Type> classType) {
        return staticStructsNestedFieldsFlatList(classType).stream()
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .count();
    }

    /**
     * Goes over a static structs and enumerates all of its fields and nested structs fields
     * recursively.
     *
     * @param classType
     * @return Flat list of all the fields nested in the struct
     */
    private static List<Field> staticStructsNestedFieldsFlatList(Class<Type> classType) {
        List<Field> canonicalFields =
                Arrays.stream(classType.getDeclaredFields())
                        .filter(field -> !StaticStruct.class.isAssignableFrom(field.getType()))
                        .collect(Collectors.toList());
        List<Field> nestedFields =
                Arrays.stream(classType.getDeclaredFields())
                        .filter(field -> StaticStruct.class.isAssignableFrom(field.getType()))
                        .map(
                                field ->
                                        staticStructsNestedFieldsFlatList(
                                                (Class<Type>) field.getType()))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        return Stream.concat(canonicalFields.stream(), nestedFields.stream())
                .collect(Collectors.toList());
    }

    public static <T extends Type> int getDataOffset(
            String input, int offset, TypeReference<?> typeReference)
            throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<Type> type = (Class<Type>) typeReference.getClassType();
        if (DynamicBytes.class.isAssignableFrom(type)
                || Utf8String.class.isAssignableFrom(type)
                || DynamicArray.class.isAssignableFrom(type)
                || hasStructOffsetInStaticArray(typeReference, offset)) {
            return TypeDecoder.decodeUintAsInt(input, offset) << 1;
        } else {
            return offset;
        }
    }

    /**
     * Checks if the parametrized type is offsetted in case of static array containing structs.
     *
     * @param typeReference
     * @return
     * @throws ClassNotFoundException
     */
    private static boolean hasStructOffsetInStaticArray(TypeReference<?> typeReference, int offset)
            throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<Type> type = (Class<Type>) typeReference.getClassType();
        try {
            return StaticArray.class.isAssignableFrom(type)
                    && (DynamicStruct.class.isAssignableFrom(
                                    Utils.getParameterizedTypeFromArray(typeReference))
                            || (StaticStruct.class.isAssignableFrom(
                                            Utils.getParameterizedTypeFromArray(typeReference))
                                    && offset == 0));
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Checks if the parametrized type is a static struct.
     *
     * @param typeReference
     * @return True if parametrized type is a struct
     * @throws ClassNotFoundException
     */
    private static boolean isParametrizedTypeStruct(TypeReference<?> typeReference)
            throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<Type> type = (Class<Type>) typeReference.getClassType();
        try {
            return StaticArray.class.isAssignableFrom(type);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
