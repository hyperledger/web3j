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
package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

/** Transaction manager implementation for read-only operations on smart contracts. */
public class ReadonlyTransactionManager extends TransactionManager {
    private final Web3j web3j;
    private final String fromAddress;

    public ReadonlyTransactionManager(final Web3j web3j, final String fromAddress) {
        super(web3j, fromAddress);
        this.web3j = web3j;
        this.fromAddress = fromAddress;
    }

    @Override
    public EthSendTransaction sendTransaction(
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final BigInteger value,
            final boolean constructor)
            throws IOException {
        throw new UnsupportedOperationException(
                "Only read operations are supported by this transaction manager");
    }

    @Override
    public EthSendTransaction sendEIP1559Transaction(
            long chainId,
            final BigInteger maxPriorityFeePerGas,
            final BigInteger maxFeePerGas,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final BigInteger value,
            final boolean constructor)
            throws IOException {
        throw new UnsupportedOperationException(
                "Only read operations are supported by this transaction manager");
    }

    @Override
    public String sendCall(
            final String to, final String data, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        final EthCall ethCall =
                web3j.ethCall(
                                Transaction.createEthCallTransaction(fromAddress, to, data),
                                defaultBlockParameter)
                        .send();

        assertCallNotReverted(ethCall);
        return ethCall.getValue();
    }

    @Override
    public EthGetCode getCode(
            final String contractAddress, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        return web3j.ethGetCode(contractAddress, defaultBlockParameter).send();
    }
}
