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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.web3j.protocol.Web3jService;

public class BatchRequest {

    private Web3jService web3jService;
    private List<Request<?, ? extends Response<?>>> requests = new ArrayList<>();

    public BatchRequest(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    public BatchRequest add(Request<?, ? extends Response<?>> request) {
        requests.add(request);
        return this;
    }

    public List<Request<?, ? extends Response<?>>> getRequests() {
        return requests;
    }

    public BatchResponse send() throws IOException {
        return web3jService.sendBatch(this);
    }

    public CompletableFuture<BatchResponse> sendAsync() {
        return web3jService.sendBatchAsync(this);
    }
}
