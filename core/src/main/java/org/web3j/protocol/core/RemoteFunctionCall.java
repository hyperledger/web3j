package org.web3j.protocol.core;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * A wrapper for a callable function.
 * Can also return the raw encoded function
 *
 * @param <T> Our return type.
 */
public class RemoteFunctionCall<T> extends RemoteCall<T> {

    private final Function function;

    public RemoteFunctionCall(Function function, Callable<T> callable) {
        super(callable);
        this.function = function;
    }

    /**
     * return an encoded function, so it can be manually signed and transmitted
     * @return the function call, encoded.
     */
    public String encodeFunctionCall() {
        return FunctionEncoder.encode(function);
    }

    /**
     * decode a method response
     * @param response
     * @return
     */
    public List<Type> decodeFunctionResponse(String response) {
        return FunctionReturnDecoder.decode(response, function.getOutputParameters());
    }
}
