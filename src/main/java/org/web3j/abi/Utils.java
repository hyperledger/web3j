package org.web3j.abi;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.Type;

/**
 * Utility functions.
 */
public class Utils {
    private Utils() {}

    static <T extends Type> String getTypeName(TypeReference<T> typeReference) {
        try {
            java.lang.reflect.Type reflectedType = typeReference.getType();

            Class<?> type;
            if (reflectedType instanceof ParameterizedTypeImpl) {
                type = ((ParameterizedTypeImpl) reflectedType).getRawType();
                return getParameterizedTypeName(typeReference, type);
            } else {
                type = Class.forName(((Class) reflectedType).getName());
                return getSimpleTypeName(type);
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }

    static String getSimpleTypeName(Class<?> type) {
        String simpleName = type.getSimpleName().toLowerCase();

        if (type.equals(Uint.class) || type.equals(Int.class)
                || type.equals(Ufixed.class) || type.equals(Fixed.class)) {
            return simpleName + "256";
        } else if (type.equals(Utf8String.class)) {
            return "string";
        } else if (type.equals(DynamicBytes.class)) {
            return "bytes";
        } else {
            return simpleName;
        }
    }

    static <T extends Type, U extends Type> String getParameterizedTypeName(
            TypeReference<T> typeReference, Class<?> type) {

        try {
            if (type.equals(DynamicArray.class)) {
                Class<U> parameterizedType = getParameterizedTypeFromArray(typeReference);
                String parameterizedTypeName = getSimpleTypeName(parameterizedType);
                return parameterizedTypeName + "[]";
            } else if (type.equals(StaticArray.class)) {
                Class<U> parameterizedType = getParameterizedTypeFromArray(typeReference);
                String parameterizedTypeName = getSimpleTypeName(parameterizedType);
                return parameterizedTypeName +
                        "[" +
                        ((TypeReference.StaticArrayTypeReference) typeReference).getSize() +
                        "]";
            } else {
                throw new UnsupportedOperationException("Invalid type provided " + type.getName());
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }

    static <T extends Type> Class<T> getParameterizedTypeFromArray(
            TypeReference typeReference) throws ClassNotFoundException {

        java.lang.reflect.Type type = typeReference.getType();
        java.lang.reflect.Type[] typeArguments =
                ((ParameterizedTypeImpl) type).getActualTypeArguments();

        String parameterizedTypeName = ((Class) typeArguments[0]).getName();
        return (Class<T>) Class.forName(parameterizedTypeName);
    }
}
