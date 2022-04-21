/*
 * Copyright 2021 Web3 Labs Ltd.
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
package org.web3j.protocol.exceptions;

import org.web3j.protocol.core.Response;

/**
 * Exception encapsulating JSON-RPC error object.
 *
 * @see <a href="https://www.jsonrpc.org/specification#error_object">error_object</a>
 */
public class JsonRpcError extends RuntimeException {

    private final int code;
    private final Object data;

    public JsonRpcError(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public JsonRpcError(Response.Error error) {
        this(error.getCode(), error.getMessage(), error.getData());
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
