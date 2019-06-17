package org.web3j.abi;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

public class DefaultFunctionEncoder extends FunctionEncoder {

    @Override
    public String encodeFunction(final Function function) {
        final List<Type> parameters = function.getInputParameters();

        final String methodSignature = buildMethodSignature(function.getName(), parameters);
        final String methodId = buildMethodId(methodSignature);

        final StringBuilder result = new StringBuilder();
        result.append(methodId);

        return encodeParameters(parameters, result);
    }

    @Override
    public String encodeParameters(final List<Type> parameters) {
        return encodeParameters(parameters, new StringBuilder());
    }

    private static String encodeParameters(
            final List<Type> parameters, final StringBuilder result) {

        int dynamicDataOffset = getLength(parameters) * Type.MAX_BYTE_LENGTH;
        final StringBuilder dynamicData = new StringBuilder();

        for (Type parameter : parameters) {
            final String encodedValue = TypeEncoder.encode(parameter);

            if (TypeEncoder.isDynamic(parameter)) {
                final String encodedDataOffset = TypeEncoder.encodeNumeric(
                        new Uint(BigInteger.valueOf(dynamicDataOffset)));
                result.append(encodedDataOffset);
                dynamicData.append(encodedValue);
                dynamicDataOffset += encodedValue.length() >> 1;
            } else {
                result.append(encodedValue);
            }
        }
        result.append(dynamicData);

        return result.toString();
    }

    private static int getLength(final List<Type> parameters) {
        int count = 0;
        for (final Type type : parameters) {
            if (type instanceof StaticArray) {
                count += ((StaticArray) type).getValue().size();
            } else {
                count++;
            }
        }
        return count;
    }
}
