package org.web3j.abi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.datatypes.*;
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
     * @param <T> types returned
     * @return {@link List} of values returned by function, {@link Collections#emptyList()} if
     *         invalid response
     */
    public static <T extends Type> List<T> decode(
            String rawInput, List<TypeReference<T>> outputParameters) {
        String input = Numeric.cleanHexPrefix(rawInput);

        if (input.isEmpty()) {
            return Collections.emptyList();
        } else {
            return build(input, outputParameters);
        }
    }

    private static <T extends Type> List<T> build(
            String input, List<TypeReference<T>> outputParameters) {
        List<T> results = new ArrayList<>(outputParameters.size());

        int offset = 0;
        for (TypeReference<T> typeReference:outputParameters) {
            try {
                Class<T> type = typeReference.getClassType();

                int hexStringDataOffset = getDataOffset(input, offset, type);

                T result;
                if (DynamicArray.class.isAssignableFrom(type)) {
                    result = TypeDecoder.decodeDynamicArray(
                            input, hexStringDataOffset, typeReference);
                } else if (StaticArray.class.isAssignableFrom(type)) {
                    int length = ((TypeReference.StaticArrayTypeReference) typeReference).getSize();
                    result = TypeDecoder.decodeStaticArray(
                            input, hexStringDataOffset, typeReference, length);
                } else {
                    result = TypeDecoder.decode(input, hexStringDataOffset, type);
                }
                results.add(result);

                offset += TypeDecoder.getSingleElementLength(
                        input, hexStringDataOffset + MAX_BYTE_LENGTH_FOR_HEX_STRING, type)
                        * MAX_BYTE_LENGTH_FOR_HEX_STRING;
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
