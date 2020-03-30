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
import java.util.concurrent.CompletableFuture;

import io.reactivex.Flowable;

import org.web3j.abi.datatypes.Function;
import org.web3j.utils.Async;

/**
 * A common type for wrapping remote requests.
 *
 * @param <T> Our return type.
 */
public interface RemoteCall<T> {

    /**
     * Perform request synchronously.
     *
     * @return result of enclosed function
     * @throws IOException if the function throws an exception
     */
    default T call() throws IOException {
        return call(null);
    }

    /**
     * Perform request synchronously.
     *
     * @param blockParameter block number or block name. Can be <code>null</code>
     * @return result of enclosed function
     * @throws IOException if the function throws an exception
     */
    T call(DefaultBlockParameter blockParameter) throws IOException;

    /**
     * Perform request asynchronously with a future.
     *
     * @return a future containing our function
     */
    default CompletableFuture<T> callAsync() {
        return Async.run(this::call);
    }

    /**
     * Provide an flowable to emit result from our function.
     *
     * @return an flowable
     */
    default Flowable<T> flowable() {
        return Flowable.fromCallable(this::call);
    }

    /**
     * Provide the remote call internal function.
     * 
     * @return this call internal function
     */
    Function getFunction();
}
