package org.web3j.abi;

import java.util.ArrayList;
import java.util.List;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.utils.Numeric;

/**
 * Decodes values returned by function or event calls.
 */
public class FunctionReturnDecoder {

    private FunctionReturnDecoder() { }

    public static <T extends Type> List<T> decode(
            String rawInput, List<Class<T>> outputParameters) {
        String input = Numeric.cleanHexPrefix(rawInput);

        List<T> results = new ArrayList<>(outputParameters.size());

        int offset = 0;
        for (Class<T> type:outputParameters) {
            T result = TypeDecoder.decode(input, offset, type);
            results.add(result);
            offset += (TypeDecoder.MAX_BYTE_LENGTH_FOR_HEX_STRING); // remember we're working with hex string
        }

        return results;
    }
}
