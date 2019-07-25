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
package org.web3j.protocol.exceptions;

import java.util.Optional;

/**
 * Transaction timeout exception indicates that we have breached some threshold waiting for a
 * transaction to execute.
 */
public class TransactionException extends Exception {

    private Optional<String> transactionHash = Optional.empty();

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, String transactionHash) {
        super(message);
        this.transactionHash = Optional.ofNullable(transactionHash);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    /**
     * Obtain the transaction hash .
     *
     * @return optional transaction hash .
     */
    public Optional<String> getTransactionHash() {
        return transactionHash;
    }
}
