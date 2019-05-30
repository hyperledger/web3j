package org.web3j.abi;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.web3j.abi.datatypes.Type;
import org.web3j.abi.spi.FunctionDecoderProvider;

/**
 * Decodes values returned by function or event calls.
 */
public abstract class FunctionReturnDecoder {

    private static FunctionReturnDecoder DEFAULT_DECODER;

    private static final ServiceLoader<FunctionDecoderProvider> loader =
            ServiceLoader.load(FunctionDecoderProvider.class);

    /**
     * Decode ABI encoded return values from smart contract function call.
     *
     * @param rawInput ABI encoded input
     * @param outputParameters list of return types as {@link TypeReference}
     * @return {@link List} of values returned by function, {@link Collections#emptyList()} if
     *         invalid response
     */
    public static List<Type> decode(String rawInput, List<TypeReference<Type>> outputParameters) {
        return decoder().decodeFunctionResult(rawInput, outputParameters);
    }

    /**
     * <p>Decodes an indexed parameter associated with an event. Indexed parameters are individually
     * encoded, unlike non-indexed parameters which are encoded as per ABI-encoded function
     * parameters and return values.</p>
     *
     * <p>If any of the following types are indexed, the Keccak-256 hashes of the values are
     * returned instead. These are returned as a bytes32 value.</p>
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
        return decoder().decodeEventParameter(rawInput, typeReference);
    }

    protected abstract List<Type> decodeFunctionResult(
            String rawInput, List<TypeReference<Type>> outputParameters);

    protected abstract <T extends Type> Type decodeEventParameter(
            String rawInput, TypeReference<T> typeReference);

    private static FunctionReturnDecoder decoder() {
        final Iterator<FunctionDecoderProvider> iterator = loader.iterator();
        return iterator.hasNext() ? iterator.next().get() : defaultDecoder();
    }

    private static FunctionReturnDecoder defaultDecoder() {
        if (DEFAULT_DECODER == null) {
            DEFAULT_DECODER = new DefaultFunctionReturnDecoder();
        }
        return DEFAULT_DECODER;
    }

}
