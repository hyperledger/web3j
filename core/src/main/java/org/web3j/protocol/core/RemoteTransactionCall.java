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

import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * A common type for wrapping both remote requests and transactions.
 *
 * @param <T> Our return type.
 */
public interface RemoteTransactionCall<T>
        extends RemoteTransaction<TransactionReceipt>, RemoteCall<T> {}
