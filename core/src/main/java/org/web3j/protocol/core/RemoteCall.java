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

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import io.reactivex.Flowable;

import org.web3j.utils.Async;

/**
 * A common type for wrapping remote requests.
 *
 * @param <T> Our return type.
 */
public class RemoteCall<T> {

    private Callable<T> callable;

    public RemoteCall(Callable<T> callable) {
        this.callable = callable;
    }

    /**
     * Perform request synchronously.
     *
     * @return result of enclosed function
     * @throws Exception if the function throws an exception
     */
    public T send() throws Exception {
        return callable.call();
    }

    /**
     * Perform request asynchronously with a future.
     *
     * @return a future containing our function
     */
    public CompletableFuture<T> sendAsync() {
        return Async.run(this::send);
    }

    /**
     * Provide an flowable to emit result from our function.
     *
     * @return an flowable
     */
    public Flowable<T> flowable() {
        return Flowable.fromCallable(this::send);
    }
}
