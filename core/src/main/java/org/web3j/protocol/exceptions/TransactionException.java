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
package org.web3j.protocol.exceptions;

import java.util.Optional;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * Transaction timeout exception indicates that we have breached some threshold waiting for a
 * transaction to execute.
 */
public class TransactionException extends Exception {

    private String transactionHash = null;
    private TransactionReceipt transactionReceipt = null;

    public TransactionException(final String message) {
        super(message);
    }

    public TransactionException(final String message, final String transactionHash) {
        super(message);
        this.transactionHash = transactionHash;
    }

    public TransactionException(final String message, final TransactionReceipt transactionReceipt) {
        super(message);
        this.transactionReceipt = transactionReceipt;
    }

    public TransactionException(final Throwable cause) {
        super(cause);
    }

    /**
     * Obtain the transaction hash .
     *
     * @return optional transaction hash .
     */
    public Optional<String> getTransactionHash() {
        return Optional.ofNullable(transactionHash);
    }

    /**
     * Obtain the transaction receipt.
     *
     * @return optional transaction receipt.
     */
    public Optional<TransactionReceipt> getTransactionReceipt() {
        return Optional.ofNullable(transactionReceipt);
    }
}
