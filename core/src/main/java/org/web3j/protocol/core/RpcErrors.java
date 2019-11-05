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

public final class RpcErrors {
    private RpcErrors() {}

    public static final int FILTER_NOT_FOUND = -32000;

    public static final int PARSE_ERROR = -32700;

    public static final int INVALID_REQUEST = -32601;

    public static final int INVALID_PARAMS = -32602;

    public static final int INTERNAL_ERROR = -32603;
}
