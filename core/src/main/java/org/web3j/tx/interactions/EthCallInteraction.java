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
package org.web3j.tx.interactions;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.exceptions.EthCallException;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;

/**
 * This class is created for making a ethCall. Before sending the call the block number at which the
 * call is to be executed can be set.
 *
 * @param <T> Type of response returned by the call
 */
public class EthCallInteraction<T> implements RemoteFunctionCall<T> {
    public interface CallResponseParser<T> {
        T processSendResponse(String data) throws ContractCallException;
    }

    protected final String data;
    protected final String to;
    protected final TransactionManager transactionManager;
    private DefaultBlockParameter blockParameter = DefaultBlockParameterName.LATEST;
    private final CallResponseParser<T> callResponseParser;

    public EthCallInteraction(
            String data,
            String to,
            TransactionManager transactionManager,
            CallResponseParser<T> callResponseParser) {
        this.data = data;
        this.to = to;
        this.transactionManager = transactionManager;
        this.callResponseParser = callResponseParser;
    }

    /** Set the block number at which the call is to be executed. */
    public EthCallInteraction<T> atBlockNumber(BigInteger blockNumber) {
        blockParameter = DefaultBlockParameter.valueOf(blockNumber);
        return this;
    }

    /** Set the block number at which the call is to be executed. */
    public EthCallInteraction<T> atBlockNumber(long blockNumber) {
        return atBlockNumber(BigInteger.valueOf(blockNumber));
    }

    public T send() throws IOException, EthCallException {
        String s = transactionManager.sendCall(to, data, blockParameter);
        if (s == null) {
            throw new EthCallException(null, "Empty value (0x) returned from contract");
        }
        return callResponseParser.processSendResponse(s);
    }

    @Override
    public String encodeFunctionCall() {
        return data;
    }

    /**
     * decode a method response
     *
     * @param rawResponse
     * @return
     */
    public T decodeRawResponse(String rawResponse) {
        return callResponseParser.processSendResponse(data);
    }
}
