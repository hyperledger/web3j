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
package org.web3j.protocol.websocket;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Objects necessary to process a new item received via a WebSocket subscription.
 *
 * @param <T> type of a data item that should be returned by a WebSocket subscription.
 */
public class WebSocketSubscription<T> {
    private final BehaviorSubject<T> subject;
    private final Class<T> responseType;

    /**
     * Creates WebSocketSubscription.
     *
     * @param subject used to send new data items to listeners
     * @param responseType type of a data item returned by a WebSocket subscription
     */
    public WebSocketSubscription(final BehaviorSubject<T> subject, final Class<T> responseType) {
        this.subject = subject;
        this.responseType = responseType;
    }

    public BehaviorSubject<T> getSubject() {
        return subject;
    }

    public Class<T> getResponseType() {
        return responseType;
    }
}
