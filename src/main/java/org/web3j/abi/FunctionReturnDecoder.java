package org.web3j.abi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.utils.Numeric;

import static org.web3j.abi.TypeDecoder.MAX_BYTE_LENGTH_FOR_HEX_STRING;

/**
 * Decodes values returned by function or event calls.
 */
public class FunctionReturnDecoder {

    private FunctionReturnDecoder() { }

    /**
     * Decode ABI encoded return values from smart contract function call.
     *
     * @param rawInput ABI encoded input
     * @param outputParameters list of return types as {@link TypeReference}
     * @return {@link List} of values returned by function, {@link Collections#emptyList()} if
     *         invalid response
     */
    public static List<Type> decode(
            String rawInput, List<TypeReference<Type>> outputParameters) {
        String input = Numeric.cleanHexPrefix(rawInput);

        if (input.isEmpty()) {
            return Collections.emptyList();
        } else {
            return build(input, outputParameters);
        }
    }

    /**
     * <p>Decodes an indexed parameter associated with an event. Indexed parameters are individually
     * encoded, unlike non-indexed parameters which are encoded as per ABI-encoded function
     * parameters and return values.</p>
     *
     * <p>If any of the following types are indexed, the Keccak-256 hashes of the values are returned
     * instead. These are returned as a bytes32 value.</p>
     *
     * <ul>
     *     <li>Arrays</li>
     *     <li>Strings</li>
     *     <li>Bytes</li>
     * </ul>
     *
     * <p>See the
     * <a href="http://solidity.readthedocs.io/en/latest/contracts.html#events">
     * Solidity documentation</a> for further information.</p>
     *
     * @param rawInput ABI encoded input
     * @param typeReference of expected result type
     * @param <T> type of TypeReference
     * @return the decode value
     */
    public static <T extends Type> Type decodeIndexedValue(
            String rawInput, TypeReference<T> typeReference) {
        String input = Numeric.cleanHexPrefix(rawInput);

        try {
            Class<T> type = typeReference.getClassType();

            if (Array.class.isAssignableFrom(type) ||
                    BytesType.class.isAssignableFrom(type) ||
                    Utf8String.class.isAssignableFrom(type)) {
                return TypeDecoder.decodeBytes(input, Bytes32.class);
            } else {
                return TypeDecoder.decode(input, type);
            }
        } catch (ClassNotFoundException e) {
            throw new UnsupportedOperationException("Invalid class reference provided", e);
        }
    }

    private static List<Type> build(
            String input, List<TypeReference<Type>> outputParameters) {
        List<Type> results = new ArrayList<>(outputParameters.size());

        int offset = 0;
        for (TypeReference<?> typeReference:outputParameters) {
            try {
                Class<Type> type = (Class<Type>) typeReference.getClassType();

                int hexStringDataOffset = getDataOffset(input, offset, type);

                Type result;
                if (DynamicArray.class.isAssignableFrom(type)) {
                    result = TypeDecoder.decodeDynamicArray(
                            input, hexStringDataOffset, typeReference);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;
                } else if (StaticArray.class.isAssignableFrom(type)) {
                    int length = ((TypeReference.StaticArrayTypeReference) typeReference).getSize();
                    result = TypeDecoder.decodeStaticArray(
                            input, hexStringDataOffset, typeReference, length);
                    offset += length * MAX_BYTE_LENGTH_FOR_HEX_STRING;
                } else {
                    result = TypeDecoder.decode(input, hexStringDataOffset, type);
                    offset += MAX_BYTE_LENGTH_FOR_HEX_STRING;
                }
                results.add(result);

            } catch (ClassNotFoundException e) {
                throw new UnsupportedOperationException("Invalid class reference provided", e);
            }
        }
        return results;
    }

    private static <T extends Type> int getDataOffset(String input, int offset, Class<T> type) {
        if (DynamicBytes.class.isAssignableFrom(type)
                || Utf8String.class.isAssignableFrom(type)
                || DynamicArray.class.isAssignableFrom(type)) {
            return TypeDecoder.decodeUintAsInt(input, offset) << 1;
        } else {
            return offset;
        }
    }
}
