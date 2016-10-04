package org.web3j.abi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.abi.datatypes.*;
import org.web3j.crypto.Hash;
import org.web3j.utils.Hex;

/**
 * <p>Ethereum filter encoding.
 * Further limited details are available
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI#events">here</a>.
 * </p>
 */
public class EventEncoder {

    private EventEncoder() { }

    public static <T extends Type> String encode(Event<T> function) {
        List<Class<T>> indexedParameters = function.getIndexedParameters();
        List<Class<T>> nonIndexedParameters = function.getNonIndexedParameters();

        String methodSignature = buildMethodSignature(function.getName(),
                indexedParameters, nonIndexedParameters);
        String eventSignature = buildEventSignature(methodSignature);

        return eventSignature;
    }

    static <T extends Type> String buildMethodSignature(
            String methodName, List<Class<T>> indexParameters,
            List<Class<T>> nonIndexedParameters) {

        List<Class<T>> parameters = new ArrayList<>(indexParameters);
        parameters.addAll(nonIndexedParameters);

        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        String params = parameters.stream()
                .map(p -> getTypeName(p))
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    static <T extends Type> String getTypeName(Class<T> type) {
        // TODO: Array support will need to be added here too

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

    static String buildEventSignature(String methodSignature) {
        byte[] input = methodSignature.getBytes();
        byte[] hash = Hash.sha3(input);
        return Hex.toHexString(hash);
    }
}
