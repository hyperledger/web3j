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

import java.io.IOException;

/** A listener used to notify about about new WebSocket messages. */
public interface WebSocketListener {

    /**
     * Called when a new WebSocket message is delivered.
     *
     * @param message new WebSocket message
     * @throws IOException thrown if an observer failed to process the message
     */
    void onMessage(String message) throws IOException;

    void onError(Exception e);

    void onClose();
}
