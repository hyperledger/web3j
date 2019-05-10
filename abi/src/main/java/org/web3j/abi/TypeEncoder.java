package org.web3j.abi;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.CompositeDataHolderType;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.NumericType;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.StaticStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Ufixed;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.web3j.abi.datatypes.Type.MAX_BIT_LENGTH;
import static org.web3j.abi.datatypes.Type.MAX_BYTE_LENGTH;

/**
 * <p>Ethereum Contract Application Binary Interface (ABI) encoding for types.
 * Further details are available
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 * </p>
 */
public class TypeEncoder {

    private TypeEncoder() {
    }

//    public static boolean isDynamic(Class<Type> parameter) {
////        if (StaticStructType.class.isAssignableFrom(parameter.getClass())) {
////            // A static struct with at least one dynamic member is deemed dynamic.
////            final StaticStructType t = (StaticStructType) parameter;
////            for (Type type : t.getValue()) {
////                if (isDynamic(type)) {
////                    return true;
////                }
////            }
////            return false;
////        }
////
////        return parameter instanceof DynamicBytes
////                || parameter instanceof Utf8String
////                || parameter instanceof DynamicArray
////                || parameter instanceof DynamicStructType;
//
//        return false;
//    }


    public static boolean isDynamic(Type parameter) {
        if (StaticStructType.class.isAssignableFrom(parameter.getClass())) {
            // A static struct with at least one dynamic member is deemed dynamic.
            final StaticStructType t = (StaticStructType) parameter;
            for (Type type : t.getValue()) {
                if (isDynamic(type)) {
                    return true;
                }
            }
            return false;
        }

        return parameter instanceof DynamicBytes
                || parameter instanceof Utf8String
                || parameter instanceof DynamicArray
                || parameter instanceof DynamicStructType;
    }

    // May not be the best place for this, but we'll move things around later.
    public static boolean isDynamic(TypeReference typeReference) {
        final Class typeClass = ((Class) typeReference.getType());

        // TODO: do static struct first.
        if (typeClass.isAssignableFrom(StaticStructType.class)) {
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
                || DynamicStructType.class.isAssignableFrom(typeClass);
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
        } else if (parameter instanceof StaticStructType || parameter instanceof StaticArray) {
            return encodeArrayValues((StaticArray) parameter);
        } else if (parameter instanceof DynamicStructType) {
            if (CompositeDataHolderType.class.isAssignableFrom(parameter.getClass())) {
                return encodeCompositeDataHolder((CompositeDataHolderType) parameter);
            } else {
                return encodeDynamicStruct((DynamicStructType) parameter);
            }
        } else if (parameter instanceof DynamicArray) {
            return encodeDynamicArray((DynamicArray) parameter);
        } else {
            throw new UnsupportedOperationException(
                    "Type cannot be encoded: " + parameter.getClass());
        }
    }

    static String encodeAddress(Address address) {
        return encodeNumeric(address.toUint160());
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
                rawValue, 0,
                paddedRawValue, MAX_BYTE_LENGTH - rawValue.length,
                rawValue.length);
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
            result.append(TypeEncoder.encode(type));
        }
        return result.toString();
    }

    static <T extends Type> String encodeDynamicArray(DynamicArray<T> value) {
        final int size = value.getValue().size();
        String encodedLength = encode(new Uint(BigInteger.valueOf(size)));
        String valuesOffsets = encodeArrayValuesOffsets(value);
        String encodedValues = encodeArrayValues(value);

        StringBuilder result = new StringBuilder();
        result.append(encodedLength);
        result.append(valuesOffsets);
        result.append(encodedValues);
        return result.toString();
    }


    private static String encodeCompositeDataHolder(CompositeDataHolderType value) {
        return encodeDynamicStruct(value, false);
    }

    private static String encodeDynamicStruct(DynamicStructType value) {
        return encodeDynamicStruct(value, true);
    }

    /**
     * A struct is encoded by writing a preamble (0x20), followed by offsets for dynamic data or in-place encoding
     * for static data.
     *
     * @param value            the struct value.
     * @param isTopLevelStruct wheter this is a top-level (not a nested/embedded).
     * @return the encoding of the struct.
     */
    private static String encodeDynamicStruct(DynamicStructType value, boolean isTopLevelStruct) {
        final StringBuilder structOffsetsAndStaticData = new StringBuilder();
        if (isTopLevelStruct) {
            // 0000000000000000000000000000000000000000000000000000000000000020
            structOffsetsAndStaticData.append(encode(new Uint(BigInteger.valueOf(MAX_BYTE_LENGTH))));
        }

        // Struct args.
        final int numStructArgs = value.getValue().size();

        // The first block is offset by 32 bytes to allow for the preamble.
        final int currentBlockOffset = isTopLevelStruct ? MAX_BYTE_LENGTH : 0;
        int dynamicDataOffset = numStructArgs * MAX_BYTE_LENGTH + currentBlockOffset;


        final StringBuilder structDynamicData = new StringBuilder();
        // We want to encode the data in a BFS-manner, encoding the current struct meta-data (offsets for dynamic values,
        // and in-place encoding for static values), and then follow it with the encoded data segment for the dynamic values.
        for (Type structFieldType : value.getValue())
            if (isDynamic(structFieldType)) {
                String encodedData = "";
                if (DynamicStructType.class.isAssignableFrom(structFieldType.getClass())) {
                    // Embedded struct.
                    encodedData = encodeDynamicStruct((DynamicStructType) structFieldType, false);
                } else {
                    encodedData = encode(structFieldType);
                }

                structOffsetsAndStaticData.append(encode(new Uint(BigInteger.valueOf(dynamicDataOffset - currentBlockOffset)))); // The offset.
                structDynamicData.append(encodedData); // Add the dynamic data to another stringbuilder for appending later on.
                dynamicDataOffset += encodedData.length() / 2;

            } else {
                // Static type - encode and append in-place.
                structOffsetsAndStaticData.append(encode(structFieldType));
            }
        // Finally
        return structOffsetsAndStaticData.toString() + structDynamicData.toString();
    }

    private static <T extends Type> String encodeArrayValuesOffsets(Array<T> value) {
        final StringBuilder result = new StringBuilder();
        final List<T> valuesList = value.getValue();
        boolean arrayOfBytes = !valuesList.isEmpty()
                && valuesList.get(0) instanceof DynamicBytes;
        boolean arrayOfString = !valuesList.isEmpty()
                && valuesList.get(0) instanceof Utf8String;
        if (arrayOfBytes || arrayOfString) {
            long offset = 0;
            for (int i = 0; i < valuesList.size(); i++) {
                if (i == 0) {
                    offset = valuesList.size() * MAX_BYTE_LENGTH;
                } else {
                    final T previousValueType = valuesList.get(i - 1);
                    final int bytesLength = arrayOfBytes
                            ? ((byte[]) previousValueType.getValue()).length
                            : ((String) previousValueType.getValue()).length();
                    int numberOfWords = (bytesLength + MAX_BYTE_LENGTH - 1) / MAX_BYTE_LENGTH;
                    int totalBytesLength = numberOfWords * MAX_BYTE_LENGTH;
                    offset += totalBytesLength + MAX_BYTE_LENGTH;
                }
                result.append(
                        Numeric.toHexStringNoPrefix(
                                Numeric.toBytesPadded(
                                        new BigInteger(Long.toString(offset)), MAX_BYTE_LENGTH
                                )
                        )
                );
            }
        }
        return result.toString();
    }

}