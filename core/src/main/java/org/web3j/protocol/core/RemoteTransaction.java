/*
 * Copyright 2020 Web3 Labs Ltd.
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

import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Async;

/**
 * A common type for wrapping remote transactions.
 *
 * @param <T> Our return type.
 */
public interface RemoteTransaction<T> extends RemoteCall<T> {

    /**
     * Execute transaction synchronously.
     *
     * @return result of enclosed transaction
     * @throws IOException if the transaction throws an exception
     */
    TransactionReceipt send() throws IOException, TransactionException;

    /**
     * Execute transaction asynchronously with a future.
     *
     * @return a future containing our transaction receipt
     */
    default CompletableFuture<TransactionReceipt> sendAsync() {
        return Async.run(this::send);
    }
}
