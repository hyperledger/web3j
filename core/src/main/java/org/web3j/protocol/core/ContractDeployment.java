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
import java.lang.reflect.Constructor;
import java.math.BigInteger;

import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.generated.RemoteTransactionCall1;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

public class ContractDeployment<T extends Contract> implements RemoteTransaction<T> {

    private static final Function FUNC_DEPLOY =
            new Function(Contract.FUNC_DEPLOY, emptyList(), emptyList());

    private final Web3j web3j;
    private final Class<T> type;

    private final TransactionManager transactionManager;
    private final ContractGasProvider contractGasProvider;

    private final String binary;
    private final String encodedConstructor;

    private final BigInteger value;

    public ContractDeployment(
            final Web3j web3j,
            final Class<T> type,
            final TransactionManager transactionManager,
            final ContractGasProvider contractGasProvider,
            final String binary,
            final String encodedConstructor,
            final BigInteger value) {

        requireNonNull(web3j);
        requireNonNull(type);
        requireNonNull(transactionManager);
        requireNonNull(binary);
        requireNonNull(encodedConstructor);

        this.web3j = web3j;
        this.type = type;
        this.transactionManager = transactionManager;
        this.contractGasProvider = contractGasProvider;
        this.binary = binary;
        this.encodedConstructor = encodedConstructor;
        this.value = value != null ? value : BigInteger.ZERO;
    }

    public T send() throws IOException, TransactionException {
        final TransactionReceipt transactionReceipt =
                new RemoteTransactionCall1<Void>(
                                web3j,
                                FUNC_DEPLOY,
                                null,
                                transactionManager,
                                null,
                                binary + encodedConstructor,
                                value,
                                true,
                                contractGasProvider)
                        .send();

        final String contractAddress = transactionReceipt.getContractAddress();

        if (contractAddress == null) {
            throw new TransactionException("Empty contract address returned", transactionReceipt);
        }

        try {
            return createContract(contractAddress, transactionReceipt);
        } catch (final ReflectiveOperationException e) {
            throw new TransactionException(
                    "Could not create contract wrapper: " + e.getMessage(), transactionReceipt);
        }
    }

    private T createContract(
            final String contractAddress, final TransactionReceipt transactionReceipt)
            throws ReflectiveOperationException {

        final Constructor<T> constructor =
                type.getDeclaredConstructor(
                        String.class,
                        Web3j.class,
                        TransactionManager.class,
                        ContractGasProvider.class,
                        TransactionReceipt.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                contractAddress,
                web3j,
                transactionManager,
                contractGasProvider,
                transactionReceipt);
    }
}
