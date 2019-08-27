/*
 * Copyright 2019 Web3 Labs LTD.
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Fixed;
import org.web3j.abi.datatypes.FixedPointType;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.IntType;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint160;
import org.web3j.utils.Numeric;

import static org.web3j.abi.TypeReference.makeTypeReference;

/**
 * Ethereum Contract Application Binary Interface (ABI) decoding for types. Decoding is not
 * documented, but is the reverse of the encoding details located <a
 * href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 */
public class TypeDecoder {

    static final int MAX_BYTE_LENGTH_FOR_HEX_STRING = Type.MAX_BYTE_LENGTH << 1;

    public static Type instantiateType(String solidityType, Object value)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException,
                    IllegalAccessException, ClassNotFoundException {
        return instantiateType(makeTypeReference(solidityType), value);
    }

    public static Type instantiateType(TypeReference ref, Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
                    InstantiationException, ClassNotFoundException {
        Class rc = ref.getClassType();
        if (Array.class.isAssignableFrom(rc)) {
            return instantiateArrayType(ref, value);
        }
        return instantiateAtomicType(rc, value);
    }

    public static <T extends Array> T decode(
            String input, int offset, TypeReference<T> typeReference) {
        Class cls = ((ParameterizedType) typeReference.getType()).getRawType().getClass();
        if (StaticArray.class.isAssignableFrom(cls)) {
            return decodeStaticArray(input, offset, typeReference, 1);
        } else if (DynamicArray.class.isAssignableFrom(cls)) {
            return decodeDynamicArray(input, offset, typeReference);
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported TypeReference: "
                            + cls.getName()
                            + ", only Array types can be passed as TypeReferences");
        }
    }

    @SuppressWarnings("unchecked")
    static <T extends Type> T decode(String input, int offset, Class<T> type) {
        if (NumericType.class.isAssignableFrom(type)) {
            return (T) decodeNumeric(input.substring(offset), (Class<NumericType>) type);
        } else if (Address.class.isAssignableFrom(type)) {
            return (T) decodeAddress(input.substring(offset));
        } else if (Bool.class.isAssignableFrom(type)) {
            return (T) decodeBool(input, offset);
        } else if (Bytes.class.isAssignableFrom(type)) {
            return (T) decodeBytes(input, offset, (Class<Bytes>) type);
        } else if (DynamicBytes.class.isAssignableFrom(type)) {
            return (T) decodeDynamicBytes(input, offset);
        } else if (Utf8String.class.isAssignableFrom(type)) {
            return (T) decodeUtf8String(input, offset);
        } else if (Array.class.isAssignableFrom(type)) {
            throw new UnsupportedOperationException(
                    "Array types must be wrapped in a TypeReference");
        } else {
            throw new UnsupportedOperationException("Type cannot be encoded: " + type.getClass());
        }
    }

    static <T extends Type> T decode(String input, Class<T> type) {
        return decode(input, 0, type);
    }

    static Address decodeAddress(String input) {
        return new Address(decodeNumeric(input, Uint160.class));
    }

    static <T extends NumericType> T decodeNumeric(String input, Class<T> type) {
        try {
            byte[] inputByteArray = Numeric.hexStringToByteArray(input);
            int typeLengthAsBytes = getTypeLengthInBytes(type);

            byte[] resultByteArray = new byte[typeLengthAsBytes + 1];

            if (Int.class.isAssignableFrom(type) || Fixed.class.isAssignableFrom(type)) {
                resultByteArray[0] = inputByteArray[0]; // take MSB as sign bit
            }

            int valueOffset = Type.MAX_BYTE_LENGTH - typeLengthAsBytes;
            System.arraycopy(inputByteArray, valueOffset, resultByteArray, 1, typeLengthAsBytes);

            BigInteger numericValue = new BigInteger(resultByteArray);
            return type.getConstructor(BigInteger.class).newInstance(numericValue);

        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            throw new UnsupportedOperationException(
                    "Unable to create instance of " + type.getName(), e);
        }
    }

    static <T extends NumericType> int getTypeLengthInBytes(Class<T> type) {
        return getTypeLength(type) >> 3; // divide by 8
    }

    static <T extends NumericType> int getTypeLength(Class<T> type) {
        if (IntType.class.isAssignableFrom(type)) {
            String regex = "(" + Uint.class.getSimpleName() + "|" + Int.class.getSimpleName() + ")";
            String[] splitName = type.getSimpleName().split(regex);
            if (splitName.length == 2) {
                return Integer.parseInt(splitName[1]);
            }
        } else if (FixedPointType.class.isAssignableFrom(type)) {
            String regex =
                    "(" + Ufixed.class.getSimpleName() + "|" + Fixed.class.getSimpleName() + ")";
            String[] splitName = type.getSimpleName().split(regex);
            if (splitName.length == 2) {
                String[] bitsCounts = splitName[1].split("x");
                return Integer.parseInt(bitsCounts[0]) + Integer.parseInt(bitsCounts[1]);
            }
        }
        return Type.MAX_BIT_LENGTH;
    }

    static Type instantiateArrayType(TypeReference ref, Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
                    InstantiationException, ClassNotFoundException {
        List values;
        if (value instanceof List) {
            values = (List) value;
        } else if (value.getClass().isArray()) {
            values = arrayToList(value);
        } else {
            throw new ClassCastException(
                    "Arg of type "
                            + value.getClass()
                            + " should be a list to instantiate web3j Array");
        }
        Constructor listcons;
        int arraySize =
                ref instanceof TypeReference.StaticArrayTypeReference
                        ? ((TypeReference.StaticArrayTypeReference) ref).getSize()
                        : -1;
        if (arraySize <= 0) {
            listcons = DynamicArray.class.getConstructor(new Class[] {Class.class, List.class});
        } else {
            Class arrayClass =
                    Class.forName("org.web3j.abi.datatypes.generated.StaticArray" + arraySize);
            listcons = arrayClass.getConstructor(new Class[] {Class.class, List.class});
        }
        // create a list of arguments coerced to the correct type of sub-TypeReference
        ArrayList transformedList = new ArrayList(values.size());
        TypeReference subTypeReference = ref.getSubTypeReference();
        for (Object o : values) {
            transformedList.add(instantiateType(subTypeReference, o));
        }
        return (Type) listcons.newInstance(ref.getClassType(), transformedList);
    }

    static Type instantiateAtomicType(Class referenceClass, Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
                    InstantiationException, ClassNotFoundException {
        Object constructorArg = null;
        if (NumericType.class.isAssignableFrom(referenceClass)) {
            constructorArg = asBigInteger(value);
        } else if (BytesType.class.isAssignableFrom(referenceClass)) {
            if (value instanceof byte[]) {
                constructorArg = value;
            } else if (value instanceof BigInteger) {
                constructorArg = ((BigInteger) value).toByteArray();
            } else if (value instanceof String) {
                constructorArg = Numeric.hexStringToByteArray((String) value);
            }
        } else if (Utf8String.class.isAssignableFrom(referenceClass)) {
            constructorArg = value.toString();
        } else if (Address.class.isAssignableFrom(referenceClass)) {
            if (value instanceof BigInteger || value instanceof Uint160) {
                constructorArg = value;
            } else {
                constructorArg = value.toString();
            }
        } else if (Bool.class.isAssignableFrom(referenceClass)) {
            if (value instanceof Boolean) {
                constructorArg = value;
            } else {
                BigInteger bival = asBigInteger(value);
                constructorArg = bival == null ? null : !bival.equals(BigInteger.ZERO);
            }
        }
        if (constructorArg == null) {
            throw new InstantiationException(
                    "Could not create type "
                            + referenceClass
                            + " from arg "
                            + value.toString()
                            + " of type "
                            + value.getClass());
        }
        Constructor cons = referenceClass.getConstructor(new Class[] {constructorArg.getClass()});
        return (Type) cons.newInstance(constructorArg);
    }

    static <T extends Type> int getSingleElementLength(String input, int offset, Class<T> type) {
        if (input.length() == offset) {
            return 0;
        } else if (DynamicBytes.class.isAssignableFrom(type)
                || Utf8String.class.isAssignableFrom(type)) {
            // length field + data value
            return (decodeUintAsInt(input, offset) / Type.MAX_BYTE_LENGTH) + 2;
        } else {
            return 1;
        }
    }

    static int decodeUintAsInt(String rawInput, int offset) {
        String input = rawInput.substring(offset, offset + MAX_BYTE_LENGTH_FOR_HEX_STRING);
        return decode(input, 0, Uint.class).getValue().intValue();
    }

    static Bool decodeBool(String rawInput, int offset) {
        String input = rawInput.substring(offset, offset + MAX_BYTE_LENGTH_FOR_HEX_STRING);
        BigInteger numericValue = Numeric.toBigInt(input);
        boolean value = numericValue.equals(BigInteger.ONE);
        return new Bool(value);
    }

    static <T extends Bytes> T decodeBytes(String input, Class<T> type) {
        return decodeBytes(input, 0, type);
    }

    static <T extends Bytes> T decodeBytes(String input, int offset, Class<T> type) {
        try {
            String simpleName = type.getSimpleName();
            String[] splitName = simpleName.split(Bytes.class.getSimpleName());
            int length = Integer.parseInt(splitName[1]);
            int hexStringLength = length << 1;

            byte[] bytes =
                    Numeric.hexStringToByteArray(input.substring(offset, offset + hexStringLength));
            return type.getConstructor(byte[].class).newInstance(bytes);
        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            throw new UnsupportedOperationException(
                    "Unable to create instance of " + type.getName(), e);
        }
    }

    static DynamicBytes decodeDynamicBytes(String input, int offset) {
        int encodedLength = decodeUintAsInt(input, offset);
        int hexStringEncodedLength = encodedLength << 1;

        int valueOffset = offset + MAX_BYTE_LENGTH_FOR_HEX_STRING;

        String data = input.substring(valueOffset, valueOffset + hexStringEncodedLength);
        byte[] bytes = Numeric.hexStringToByteArray(data);

        return new DynamicBytes(bytes);
    }

    static Utf8String decodeUtf8String(String input, int offset) {
        DynamicBytes dynamicBytesResult = decodeDynamicBytes(input, offset);
        byte[] bytes = dynamicBytesResult.getValue();

        return new Utf8String(new String(bytes, StandardCharsets.UTF_8));
    }

    /** Static array length cannot be passed as a type. */
    @SuppressWarnings("unchecked")
    static <T extends Type> T decodeStaticArray(
            String input, int offset, TypeReference<T> typeReference, int length) {

        BiFunction<List<T>, String, T> function =
                (elements, typeName) -> {
                    if (elements.isEmpty()) {
                        throw new UnsupportedOperationException(
                                "Zero length fixed array is invalid type");
                    } else {
                        return instantiateStaticArray(typeReference, elements, length);
                    }
                };

        return decodeArrayElements(input, offset, typeReference, length, function);
    }

    @SuppressWarnings("unchecked")
    static <T extends Type> T decodeDynamicArray(
            String input, int offset, TypeReference<T> typeReference) {

        int length = decodeUintAsInt(input, offset);

        BiFunction<List<T>, String, T> function =
                (elements, typeName) -> {
                    if (elements.isEmpty()) {
                        return (T) DynamicArray.empty(typeName);
                    } else {
                        return (T) new DynamicArray<>(elements);
                    }
                };

        int valueOffset = offset + MAX_BYTE_LENGTH_FOR_HEX_STRING;

        return decodeArrayElements(input, valueOffset, typeReference, length, function);
    }

    static BigInteger asBigInteger(Object arg) {
        if (arg instanceof BigInteger) {
            return (BigInteger) arg;
        } else if (arg instanceof BigDecimal) {
            return ((BigDecimal) arg).toBigInteger();
        } else if (arg instanceof String) {
            return Numeric.toBigInt((String) arg);
        } else if (arg instanceof byte[]) {
            return Numeric.toBigInt((byte[]) arg);
        } else if (arg instanceof Number) {
            return BigInteger.valueOf(((Number) arg).longValue());
        }
        return null;
    }

    static List arrayToList(Object array) {
        int len = java.lang.reflect.Array.getLength(array);
        ArrayList rslt = new ArrayList(len);
        for (int i = 0; i < len; i++) {
            rslt.add(java.lang.reflect.Array.get(array, i));
        }
        return rslt;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Type> T instantiateStaticArray(
            TypeReference<T> typeReference, List<T> elements, int length) {
        try {
            Class<? extends StaticArray> arrayClass =
                    (Class<? extends StaticArray>)
                            Class.forName("org.web3j.abi.datatypes.generated.StaticArray" + length);

            return (T) arrayClass.getConstructor(List.class).newInstance(elements);
        } catch (ReflectiveOperationException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static <T extends Type> T decodeArrayElements(
            String input,
            int offset,
            TypeReference<T> typeReference,
            int length,
            BiFunction<List<T>, String, T> consumer) {

        try {
            Class<T> cls = Utils.getParameterizedTypeFromArray(typeReference);
            if (Array.class.isAssignableFrom(cls)) {
                throw new UnsupportedOperationException(
                        "Arrays of arrays are not currently supported for external functions, see"
                                + "http://solidity.readthedocs.io/en/develop/types.html#members");
            } else {
                List<T> elements = new ArrayList<>(length);

                for (int i = 0, currOffset = offset;
                        i < length;
                        i++,
                                currOffset +=
                                        getSingleElementLength(input, currOffset, cls)
                                                * MAX_BYTE_LENGTH_FOR_HEX_STRING) {
                    T value = decode(input, currOffset, cls);
                    elements.add(value);
                }

                String typeName = Utils.getSimpleTypeName(cls);

                return consumer.apply(elements, typeName);
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException(
                    "Unable to access parameterized type " + typeReference.getType().getTypeName(),
                    e);
        }
    }
}
