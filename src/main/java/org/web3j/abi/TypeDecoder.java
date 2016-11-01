package org.web3j.abi;

import java.lang.reflect.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Type;
import org.web3j.utils.Numeric;

/**
 * <p>Ethereum Contract Application Binary Interface (ABI) decoding for types.
 * Decoding is not documented, but is the reverse of the encoding details located
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 * </p>
 */
class TypeDecoder {

    static final int MAX_BYTE_LENGTH_FOR_HEX_STRING = Type.MAX_BYTE_LENGTH << 1;

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

    @SuppressWarnings("unchecked")
    static <T extends Type> T decode(String input, int offset, Class<T> type) {
        if (NumericType.class.isAssignableFrom(type)) {
            return (T) decodeNumeric(input.substring(offset), (Class<NumericType>) type);
        } else if (Bool.class.isAssignableFrom(type)) {
            return (T) decodeBool(input);
        } else if (Bytes.class.isAssignableFrom(type)) {
            return (T) decodeBytes(input, (Class<Bytes>) type);
        } else if (DynamicBytes.class.isAssignableFrom(type)) {
            return (T) decodeDynamicBytes(input, offset);
        } else if (Utf8String.class.isAssignableFrom(type)) {
            return (T) decodeUtf8String(input, offset);
        } else if (Array.class.isAssignableFrom(type)) {
            throw new UnsupportedOperationException(
                    "Array types must be wrapped in a TypeReference");
        } else {
            throw new UnsupportedOperationException(
                    "Type cannot be encoded: " + type.getClass());
        }
    }

    public static <T extends Array> T decode(String input, int offset, TypeReference<T> typeReference) {
        Class cls = ((ParameterizedType) typeReference.getType()).getRawType().getClass();
        if (StaticArray.class.isAssignableFrom(cls)) {
            return decodeStaticArray(input, offset, typeReference, 1);
        } else if (DynamicArray.class.isAssignableFrom(cls)) {
            return decodeDynamicArray(input, offset, typeReference);
        } else {
            throw new UnsupportedOperationException("Unsupported TypeReference: " +
                    cls.getName() + ", only Array types can be passed as TypeReferences");
        }
    }

    static <T extends Type> T decode(String input, Class<T> type) {
        return decode(input, 0, type);
    }

    static <T extends NumericType> T decodeNumeric(String input, Class<T> type) {
        try {
            byte[] inputByteArray = Numeric.hexStringToByteArray(input);
            int typeLengthAsBytes = getTypeLengthInBytes(type);

            byte[] resultByteArray = new byte[typeLengthAsBytes + 1];

            if (Int.class.isAssignableFrom(type) || Fixed.class.isAssignableFrom(type)) {
                resultByteArray[0] = inputByteArray[0];  // take MSB as sign bit
            }

            int valueOffset = Type.MAX_BYTE_LENGTH - typeLengthAsBytes;
            System.arraycopy(inputByteArray, valueOffset, resultByteArray, 1, typeLengthAsBytes);

            BigInteger numericValue = new BigInteger(resultByteArray);
            return type.getConstructor(BigInteger.class).newInstance(numericValue);

        } catch (NoSuchMethodException | SecurityException |
                InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException e) {
            throw new UnsupportedOperationException(
                    "Unable to create instance of " + type.getName(), e);
        }
    }

    static <T extends NumericType> int getTypeLengthInBytes(Class<T> type) {
        return getTypeLength(type) >> 3;  // divide by 8
    }

    static <T extends NumericType> int getTypeLength(Class<T> type) {
        if (IntType.class.isAssignableFrom(type)) {
            String regex = "(" + Uint.class.getSimpleName() + "|" + Int.class.getSimpleName() + ")";
            String[] splitName = type.getSimpleName().split(regex);
            if (splitName.length == 2) {
                return Integer.parseInt(splitName[1]);
            }
        } else if (FixedPointType.class.isAssignableFrom(type)) {
            String regex = "(" + Ufixed.class.getSimpleName() + "|" + Fixed.class.getSimpleName() + ")";
            String[] splitName = type.getSimpleName().split(regex);
            if (splitName.length == 2) {
                String[] bitsCounts = splitName[1].split("x");
                return Integer.parseInt(bitsCounts[0]) + Integer.parseInt(bitsCounts[1]);
            }
        } else if (Address.class.isAssignableFrom(type)) {
            return Address.LENGTH;
        }
        return Type.MAX_BIT_LENGTH;
    }

    static int decodeUintAsInt(String rawInput, int offset) {
        String input = rawInput.substring(offset, offset + MAX_BYTE_LENGTH_FOR_HEX_STRING);
        return decode(input, 0, Uint.class).getValue().intValue();
    }

    static Bool decodeBool(String input) {
        BigInteger numericValue = Numeric.toBigInt(input);
        boolean value = numericValue.equals(BigInteger.ONE);
        return new Bool(value);
    }

    static <T extends Bytes> T decodeBytes(String input, Class<T> type) {
        try {
            String simpleName = type.getSimpleName();
            String[] splitName = simpleName.split(Bytes.class.getSimpleName());
            int length = Integer.parseInt(splitName[1]);
            int hexStringLength = length << 1;

            byte[] bytes = Numeric.hexStringToByteArray(input.substring(0, hexStringLength));
            return type.getConstructor(byte[].class).newInstance(bytes);
        } catch (NoSuchMethodException | SecurityException |
                InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException e) {
            throw new UnsupportedOperationException(
                    "Unable to create instance of " + type.getName(), e);
        }
    }

    static DynamicBytes decodeDynamicBytes(String input, int offset) {
        int encodedLength = decodeUintAsInt(input, offset);
        int hexStringEncodedLength = encodedLength << 1;

        int valueOffset = offset + MAX_BYTE_LENGTH_FOR_HEX_STRING;

        String data = input.substring(valueOffset,
                valueOffset + hexStringEncodedLength);
        byte[] bytes = Numeric.hexStringToByteArray(data);

        return new DynamicBytes(bytes);
    }

    static Utf8String decodeUtf8String(String input, int offset) {
        DynamicBytes dynamicBytesResult = decodeDynamicBytes(input, offset);
        byte[] bytes = dynamicBytesResult.getValue();

        return new Utf8String(new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * Static array length cannot be passed as a type.
     */
    @SuppressWarnings("unchecked")
    static <T extends Type> T decodeStaticArray(
            String input, int offset, TypeReference<T> typeReference, int length) {

        BiFunction<List<T>, String, T> function = (elements, typeName) -> {
            if (elements.isEmpty()) {
                throw new UnsupportedOperationException("Zero length fixed array is invalid type");
            } else {
                return (T) new StaticArray<>(elements);
            }
        };

        return decodeArrayElements(input, offset, typeReference, length, function);
    }

    @SuppressWarnings("unchecked")
    static <T extends Type> T decodeDynamicArray(
            String input, int offset, TypeReference<T> typeReference) {

        int length = decodeUintAsInt(input, offset);

        BiFunction<List<T>, String, T> function = (elements, typeName) -> {
            if (elements.isEmpty()) {
                return (T) DynamicArray.empty(typeName);
            } else {
                return (T) new DynamicArray<>(elements);
            }
        };

        int valueOffset = offset + MAX_BYTE_LENGTH_FOR_HEX_STRING;

        return decodeArrayElements(input, valueOffset, typeReference, length, function);
    }

    private static <T extends Type> T decodeArrayElements(
            String input, int offset, TypeReference<T> typeReference, int length,
            BiFunction<List<T>, String, T> consumer) {

        try {
            Class<T> cls = Utils.getParameterizedTypeFromArray(typeReference);
            if (Array.class.isAssignableFrom(cls)) {
                throw new UnsupportedOperationException(
                        "Arrays of arrays are not currently supported for external functions, " +
                                "see http://solidity.readthedocs.io/en/develop/types.html#members");
            } else {
                List<T> elements = new ArrayList<>(length);

                for (int i = 0, currOffset = offset;
                     i < length;
                     i++, currOffset += getSingleElementLength(input, currOffset, cls)
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
