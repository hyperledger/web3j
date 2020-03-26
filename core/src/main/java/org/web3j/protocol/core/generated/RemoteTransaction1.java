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
package org.web3j.protocol.core.generated;

import java.math.BigInteger;
import java.util.List;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.AbstractRemoteTransaction;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteTransaction;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

public class RemoteTransaction1<T> extends AbstractRemoteTransaction<T> {

    public RemoteTransaction1(
            final Web3j web3j,
            final Function function,
            final String contractAddress,
            final TransactionManager transactionManager,
            final DefaultBlockParameter defaultBlockParameter,
            final String data,
            final BigInteger weiValue,
            final boolean constructor,
            final ContractGasProvider gasProvider) {
        super(web3j, function, contractAddress, transactionManager, 
                defaultBlockParameter, data, weiValue, constructor, gasProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T convert(final List<Type<?>> values) {
        return (T) values.get(0);
    }
}
