package org.web3j.abi;

import java.util.ArrayList;
import java.util.List;

import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

/**
 * <p>Ethereum filter encoding.
 * Further limited details are available
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI#events">here</a>.
 * </p>
 */
public class EventEncoder {

    private EventEncoder() { }

    public static String encode(Event function) {
        List<TypeReference<Type>> indexedParameters = function.getIndexedParameters();
        List<TypeReference<Type>> nonIndexedParameters = function.getNonIndexedParameters();

        String methodSignature = buildMethodSignature(function.getName(),
                indexedParameters, nonIndexedParameters);

        return buildEventSignature(methodSignature);
    }

    static <T extends Type> String buildMethodSignature(
            String methodName, List<TypeReference<T>> indexParameters,
            List<TypeReference<T>> nonIndexedParameters) {

        List<TypeReference<T>> parameters = new ArrayList<TypeReference<T>>(indexParameters);
        parameters.addAll(nonIndexedParameters);

        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");

        String params = "";
        for (int i = 0; i < parameters.size(); i++) {
            TypeReference<T> typeReference = parameters.get(i);
            String typeName = Utils.getTypeName(typeReference);
            params += typeName;
            if (i + 1 < parameters.size()) {
                params += ",";
            }
        }

        result.append(params);
        result.append(")");
        return result.toString();
    }

    public static String buildEventSignature(String methodSignature) {
        byte[] input = methodSignature.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash);
    }
}
