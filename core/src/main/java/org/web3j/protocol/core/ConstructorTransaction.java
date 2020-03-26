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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.generated.RemoteTransaction1;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static java.util.Collections.emptyList;

public class ConstructorTransaction<T extends Contract> implements RemoteTransaction<T> {

    private static final Function FUNC_DEPLOY =
            new Function(Contract.FUNC_DEPLOY, emptyList(), emptyList());

    private final Web3j web3j;
    private final Class<T> type;
    private final Credentials credentials;
    private final TransactionManager transactionManager;
    private final ContractGasProvider contractGasProvider;
    private final String binary;
    private final String encodedConstructor;
    private final BigInteger value;

    public ConstructorTransaction(
            final Web3j web3j,
            final Class<T> type,
            final Credentials credentials,
            final TransactionManager transactionManager,
            final ContractGasProvider contractGasProvider,
            final String binary,
            final String encodedConstructor,
            final BigInteger value) {
        this.web3j = web3j;
        this.type = type;
        this.credentials = credentials;
        this.transactionManager = transactionManager;
        this.contractGasProvider = contractGasProvider;
        this.binary = binary;
        this.encodedConstructor = encodedConstructor;
        this.value = value;
    }

    @Override
    public T call(final DefaultBlockParameter blockParameter) throws IOException {
        try {
            final TransactionReceipt transactionReceipt = send();
            final String contractAddress = transactionReceipt.getContractAddress();

            if (contractAddress == null) {
                throw new NullPointerException("Empty contract address returned");
            }

            final T contract = createContract(contractAddress);
            contract.setTransactionReceipt(transactionReceipt);
            return contract;

        } catch (final TransactionException | ReflectiveOperationException e) {
            throw new IOException(e);
        }
    }

    @Override
    public TransactionReceipt send() throws IOException, TransactionException {
        return new RemoteTransaction1<Void>(
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
    }

    private T createContract(final String contractAddress) throws ReflectiveOperationException {
        final Constructor<T> constructor;
        final T contract;
        if (transactionManager != null) {
            constructor =
                    type.getDeclaredConstructor(
                            String.class,
                            String.class,
                            Web3j.class,
                            TransactionManager.class,
                            ContractGasProvider.class);
            constructor.setAccessible(true);
            contract =
                    constructor.newInstance(
                            binary,
                            contractAddress,
                            web3j,
                            transactionManager,
                            contractGasProvider);
        } else if (credentials != null) {
            constructor =
                    type.getDeclaredConstructor(
                            String.class,
                            String.class,
                            Web3j.class,
                            Credentials.class,
                            ContractGasProvider.class);
            constructor.setAccessible(true);
            contract =
                    constructor.newInstance(
                            binary, contractAddress, web3j, credentials, contractGasProvider);
        } else {
            throw new IllegalStateException(
                    "Either transaction manager or credentials"
                            + "must be provided to deploy a contract");
        }
        return contract;
    }
}
