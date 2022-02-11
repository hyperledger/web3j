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
package org.web3j.protocol.hedera;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import io.reactivex.Flowable;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.BatchRequest;
import org.web3j.protocol.core.BatchResponse;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.websocket.events.Notification;
import org.web3j.utils.Async;

/** Base service implementation. */
public class HederaService implements Web3jService {

    public HederaService() {
    }

    protected InputStream performIO(String payload) throws IOException {
        return null;
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        return null;
    }

    @Override
    public <T extends Response> CompletableFuture<T> sendAsync(
            Request jsonRpc20Request, Class<T> responseType) {
        return Async.run(() -> send(jsonRpc20Request, responseType));
    }

    @Override
    public BatchResponse sendBatch(BatchRequest batchRequest) throws IOException {
        return null;
    }

    @Override
    public CompletableFuture<BatchResponse> sendBatchAsync(BatchRequest batchRequest) {
        return Async.run(() -> sendBatch(batchRequest));
    }

    @Override
    public <T extends Notification<?>> Flowable<T> subscribe(
            Request request, String unsubscribeMethod, Class<T> responseType) {
        throw new UnsupportedOperationException(
                String.format(
                        "Service %s does not support subscriptions",
                        this.getClass().getSimpleName()));
    }

    @Override
    public void close() throws IOException {

    }

}
