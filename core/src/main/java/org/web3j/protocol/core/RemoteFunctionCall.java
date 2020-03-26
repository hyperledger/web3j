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

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.tx.TransactionManager;

/**
 * A wrapper for a callable function. Can also return the raw encoded function
 *
 * @param <T> Our return type.
 */
public abstract class RemoteFunctionCall<T> extends AbstractRemoteCall<T> {

    public RemoteFunctionCall(
            final Function function,
            final String address,
            final TransactionManager transactionManager,
            final DefaultBlockParameter defaultBlockParameter) {
        super(function, address, transactionManager, defaultBlockParameter);
    }

    /**
     * Return an encoded function, so it can be manually signed and transmitted.
     *
     * @return the function call, encoded.
     */
    public String encodeFunctionCall() {
        return FunctionEncoder.encode(function);
    }

    /**
     * Decode a method response.
     *
     * @param response the encoded response
     * @return list of abi types
     */
    public List<Type<?>> decodeFunctionResponse(final String response) {
        return FunctionReturnDecoder.decode(response, function.getOutputParameters());
    }
}
