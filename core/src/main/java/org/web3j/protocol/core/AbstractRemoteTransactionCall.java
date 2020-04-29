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
import java.math.BigInteger;

import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.web3j.utils.RevertReasonExtractor.extractRevertReason;

/**
 * A common type for wrapping remote transactions.
 *
 * @param <T> Our return type.
 */
public abstract class AbstractRemoteTransactionCall<T> extends AbstractRemoteCall<T>
        implements RemoteTransactionCall<T> {

    private final Web3j web3j;
    private final String data;
    private final BigInteger value;
    private final boolean constructor;
    private final ContractGasProvider gasProvider;

    public AbstractRemoteTransactionCall(
            final Web3j web3j,
            final Function function,
            final String address,
            final TransactionManager transactionManager,
            final DefaultBlockParameter defaultBlockParameter,
            final String data,
            final BigInteger value,
            final boolean constructor,
            final ContractGasProvider gasProvider) {
        super(function, address, transactionManager, defaultBlockParameter);
        this.web3j = web3j;
        this.data = data;
        this.value = value;
        this.constructor = constructor;
        this.gasProvider = gasProvider;
    }

    /**
     * Execute transaction synchronously.
     *
     * @return result of enclosed transaction
     * @throws IOException if the transaction throws an exception
     */
    public TransactionReceipt send() throws IOException, TransactionException {
        final TransactionReceipt receipt =
                transactionManager.executeTransaction(
                        gasProvider.getGasPrice(function.getName()),
                        gasProvider.getGasLimit(function.getName()),
                        address,
                        data,
                        value,
                        constructor);

        if (!receipt.isStatusOK()) {
            throw new TransactionException(
                    String.format(
                            "Transaction %s has failed with status: %s. "
                                    + "Gas used: %s. "
                                    + "Revert reason: '%s'.",
                            receipt.getTransactionHash(),
                            receipt.getStatus(),
                            receipt.getGasUsedRaw() != null
                                    ? receipt.getGasUsed().toString()
                                    : "unknown",
                            extractRevertReason(receipt, data, web3j, true)),
                    receipt);
        } else {
            return receipt;
        }
    }
}
