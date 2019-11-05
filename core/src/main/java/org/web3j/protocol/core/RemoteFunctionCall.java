/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.core;

import java.util.List;
import java.util.concurrent.Callable;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

/**
 * A wrapper for a callable function. Can also return the raw encoded function
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
     *
     * @return the function call, encoded.
     */
    public String encodeFunctionCall() {
        return FunctionEncoder.encode(function);
    }

    /**
     * decode a method response
     *
     * @param response
     * @return list of abi types
     */
    public List<Type> decodeFunctionResponse(String response) {
        return FunctionReturnDecoder.decode(response, function.getOutputParameters());
    }
}
