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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Fixed;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.StructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

/** Utility functions. */
public class Utils {
    private Utils() {}

    static <T extends Type> String getTypeName(final TypeReference<T> typeReference) {
        try {
            final java.lang.reflect.Type reflectedType = typeReference.getType();

            final Class<?> type;
            if (reflectedType instanceof ParameterizedType) {
                type = (Class<?>) ((ParameterizedType) reflectedType).getRawType();
                return getParameterizedTypeName(typeReference, type);
            } else {
                type = Class.forName(reflectedType.getTypeName());
                return getSimpleTypeName(type);
            }
        } catch (final ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }

    static String getSimpleTypeName(final Class<?> type) {
        final String simpleName = type.getSimpleName().toLowerCase();

        if (type.equals(Uint.class)
                || type.equals(Int.class)
                || type.equals(Ufixed.class)
                || type.equals(Fixed.class)) {
            return simpleName + "256";
        } else if (type.equals(Utf8String.class)) {
            return "string";
        } else if (type.equals(DynamicBytes.class)) {
            return "bytes";
        } else if (StructType.class.isAssignableFrom(type)) {
            return type.getName();
        } else {
            return simpleName;
        }
    }

    static <T extends Type, U extends Type> String getParameterizedTypeName(
            final TypeReference<T> typeReference, final Class<?> type) {

        try {
            if (type.equals(DynamicArray.class)) {
                final Class<U> parameterizedType = getParameterizedTypeFromArray(typeReference);
                final String parameterizedTypeName = getSimpleTypeName(parameterizedType);
                return parameterizedTypeName + "[]";
            } else if (type.equals(StaticArray.class)) {
                final Class<U> parameterizedType = getParameterizedTypeFromArray(typeReference);
                final String parameterizedTypeName = getSimpleTypeName(parameterizedType);
                return parameterizedTypeName
                        + "["
                        + ((TypeReference.StaticArrayTypeReference) typeReference).getSize()
                        + "]";
            } else {
                throw new UnsupportedOperationException("Invalid type provided " + type.getName());
            }
        } catch (final ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends Type<?>> Class<T> getParameterizedTypeFromArray(
            final TypeReference<?> typeReference) throws ClassNotFoundException {

        final java.lang.reflect.Type type = typeReference.getType();
        final java.lang.reflect.Type[] typeArguments =
                ((ParameterizedType) type).getActualTypeArguments();

        final String parameterizedTypeName = typeArguments[0].getTypeName();
        return (Class<T>) Class.forName(parameterizedTypeName);
    }

    @SuppressWarnings("unchecked")
    public static List<TypeReference<Type<?>>> convert(final List<TypeReference<?>> input) {
        final List<TypeReference<Type<?>>> result = new ArrayList<>(input.size());
        result.addAll(
                input.stream()
                        .map(typeReference -> (TypeReference<Type<?>>) typeReference)
                        .collect(Collectors.toList()));
        return result;
    }

    public static <T, R extends Type<T>, E extends Type<T>> List<E> typeMap(
            final List<List<T>> input, final Class<E> outerDestType, final Class<R> innerType) {
        final List<E> result = new ArrayList<>();
        try {
            final Constructor<E> constructor =
                    outerDestType.getDeclaredConstructor(Class.class, List.class);
            for (final List<T> ts : input) {
                final E e = constructor.newInstance(innerType, typeMap(ts, innerType));
                result.add(e);
            }
        } catch (final NoSuchMethodException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            throw new TypeMappingException(e);
        }
        return result;
    }

    public static <T, R extends Type<T>> List<R> typeMap(
            final List<T> input, final Class<R> destType) throws TypeMappingException {

        final List<R> result = new ArrayList<>(input.size());

        if (!input.isEmpty()) {
            try {
                final Constructor<R> constructor =
                        destType.getDeclaredConstructor(input.get(0).getClass());
                for (final T value : input) {
                    result.add(constructor.newInstance(value));
                }
            } catch (final NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException
                    | InstantiationException e) {
                throw new TypeMappingException(e);
            }
        }
        return result;
    }

    /**
     * Returns flat list of canonical fields in a static struct. Example: struct Baz { Struct Bar {
     * int a, int b }, int c } will return {a, b, c}.
     *
     * @param classType Static struct type
     * @return Flat list of canonical fields in a nested struct
     */
    public static List<Field> staticStructNestedPublicFieldsFlatList(Class<Type> classType) {
        return staticStructsNestedFieldsFlatList(classType).stream()
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .collect(Collectors.toList());
    }

    /**
     * Goes over a static structs and enumerates all of its fields and nested structs fields
     * recursively.
     *
     * @param classType Static struct type
     * @return Flat list of all the fields nested in the struct
     */
    @SuppressWarnings("unchecked")
    public static List<Field> staticStructsNestedFieldsFlatList(Class<Type> classType) {
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
}
