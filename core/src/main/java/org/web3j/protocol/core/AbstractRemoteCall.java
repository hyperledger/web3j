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

import java.io.IOException;
import java.util.List;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;

/**
 * A common type for wrapping remote requests.
 *
 * @param <T> Our return type.
 */
public abstract class AbstractRemoteCall<T> implements RemoteCall<T> {

    private final Function function;
    private final String address;
    private final TransactionManager transactionManager;
    private final DefaultBlockParameter defaultBlockParameter;

    public AbstractRemoteCall(
            final Function function,
            final String address,
            final TransactionManager transactionManager,
            final DefaultBlockParameter defaultBlockParameter) {
        this.function = function;
        this.address = address;
        this.transactionManager = transactionManager;
        this.defaultBlockParameter = defaultBlockParameter;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Function getFunction() {
        return function;
    }

    /**
     * Perform request synchronously.
     *
     * @param blockParameter block number or block name. Can be <code>null</code>
     * @return result of enclosed function
     * @throws IOException if the function throws an exception
     */
    public T call(DefaultBlockParameter blockParameter) throws IOException {

        if (blockParameter == null) {
            // Still support contract-defined block
            blockParameter = defaultBlockParameter;
        }

        final String encodedFunction = FunctionEncoder.encode(function);
        final String value = transactionManager.sendCall(address, encodedFunction, blockParameter);

        if (value == null) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }

        final List<Type<?>> values =
                FunctionReturnDecoder.decode(value, function.getOutputParameters());

        return convert(values);
    }

    protected TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Convert return values to the function-specific types.
     *
     * @param values Untyped function return values
     * @return Statically typed return values
     */
    protected abstract T convert(List<Type<?>> values);
}
