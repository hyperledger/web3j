package org.web3j.abi;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.spi.FunctionEncoderProvider;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

/**
 * <p>Ethereum Contract Application Binary Interface (ABI) encoding for functions.
 * Further details are available
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 * </p>
 * <p>Delegates to {@link DefaultFunctionEncoder} unless a {@link FunctionEncoderProvider} SPI
 * is found, in which case the first implementation found will be used.
 * </p>
 *
 * @see DefaultFunctionEncoder
 * @see FunctionEncoderProvider
 */
public abstract class FunctionEncoder {

    private static FunctionEncoder DEFAULT_ENCODER;

    private static final ServiceLoader<FunctionEncoderProvider> loader =
            ServiceLoader.load(FunctionEncoderProvider.class);

    public static String encode(final Function function) {
        return encoder().encodeFunction(function);
    }

    public static String encodeConstructor(final List<Type> parameters) {
        return encoder().encodeParameters(parameters);
    }

    protected abstract String encodeFunction(Function function);

    protected abstract String encodeParameters(List<Type> parameters);

    protected static String buildMethodSignature(
            final String methodName, final List<Type> parameters) {

        final StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        final String params = parameters.stream()
                .map(Type::getTypeAsString)
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    protected static String buildMethodId(final String methodSignature) {
        final byte[] input = methodSignature.getBytes();
        final byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash).substring(0, 10);
    }

    private static FunctionEncoder encoder() {
        final Iterator<FunctionEncoderProvider> iterator = loader.iterator();
        return iterator.hasNext() ? iterator.next().get() : defaultEncoder();
    }

    private static FunctionEncoder defaultEncoder() {
        if (DEFAULT_ENCODER == null) {
            DEFAULT_ENCODER = new DefaultFunctionEncoder();
        }
        return DEFAULT_ENCODER;
    }
}
