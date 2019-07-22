/*
 * Copyright 2019 Web3 Labs LTD.
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
package org.web3j.protocol.websocket;

import java.util.concurrent.CompletableFuture;

/**
 * Objects necessary to process a reply for a request sent via WebSocket protocol.
 *
 * @param <T> type of a data item that should be returned by the sent request
 */
class WebSocketRequest<T> {
    private CompletableFuture<T> onReply;
    private Class<T> responseType;

    public WebSocketRequest(CompletableFuture<T> onReply, Class<T> responseType) {
        this.onReply = onReply;
        this.responseType = responseType;
    }

    public CompletableFuture<T> getOnReply() {
        return onReply;
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}
