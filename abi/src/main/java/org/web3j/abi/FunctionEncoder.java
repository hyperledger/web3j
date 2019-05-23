package org.web3j.abi;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.spi.FunctionEncoderProvider;

/**
 * <p>Ethereum Contract Application Binary Interface (ABI) encoding for functions.
 * Further details are available
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 * </p>
 * <p>
 * Delegates to {@link DefaultFunctionEncoder} unless a {@link FunctionEncoderProvider} SPI
 * is found, in which case the first implementation found will be used.
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

    public abstract String encodeFunction(Function function);

    public abstract String encodeParameters(List<Type> parameters);

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
