package org.web3j.abi.spi;

import java.util.function.Supplier;

import org.web3j.abi.FunctionEncoder;

/**
 * Function encoding Service Provider Interface.
 */
public interface FunctionEncoderProvider extends Supplier<FunctionEncoder> {
}
