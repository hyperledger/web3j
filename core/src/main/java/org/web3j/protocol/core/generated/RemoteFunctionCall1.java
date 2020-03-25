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

import java.util.List;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.AbstractRemoteCall;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tx.TransactionManager;

public class RemoteFunctionCall1<T> extends RemoteFunctionCall<T> {

    public RemoteFunctionCall1(
            final Function function,
            final String contractAddress,
            final TransactionManager transactionManager,
            final DefaultBlockParameter defaultBlockParameter) {
        super(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T convert(final List<Type<?>> values) {
        return (T) values.get(0).getValue();
    }
}
