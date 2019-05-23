package org.web3j.abi.spi;

import java.util.function.Supplier;

import org.web3j.abi.FunctionReturnDecoder;

/**
 * Function decoding Service Provider Interface.
 */
public interface FunctionDecoderProvider extends Supplier<FunctionReturnDecoder> {
}
