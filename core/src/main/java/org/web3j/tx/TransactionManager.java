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
package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import static org.web3j.protocol.core.JsonRpc2_0Web3j.DEFAULT_BLOCK_TIME;

/**
 * Transaction manager abstraction for executing transactions with Ethereum client via various
 * mechanisms.
 */
public abstract class TransactionManager {

    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = DEFAULT_BLOCK_TIME;
    public static final String REVERT_ERR_STR = "Contract Call has been reverted by the EVM with the reason: '%s.'";

    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final String fromAddress;

    protected TransactionManager(
            TransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        this.fromAddress = fromAddress;
    }

    protected TransactionManager(Web3j web3j, String fromAddress) {
        this(
                new PollingTransactionReceiptProcessor(
                        web3j, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH),
                fromAddress);
    }

    protected TransactionManager(
            Web3j web3j, int attempts, long sleepDuration, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(web3j, sleepDuration, attempts), fromAddress);
    }

    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException, TransactionException {

        EthSendTransaction ethSendTransaction =
                sendTransaction(gasPrice, gasLimit, to, data, value);
        return processResponse(ethSendTransaction);
    }

    public abstract EthSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException;

    public abstract String sendCall(
            String to, String data, DefaultBlockParameter defaultBlockParameter) throws IOException;

    public String getFromAddress() {
        return fromAddress;
    }

    private TransactionReceipt processResponse(EthSendTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException(
                    "Error processing transaction request: "
                            + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }

    static void assertCallNotReverted(EthCall ethCall) {
        if (ethCall.reverts()) {
            throw new ContractCallException(String.format(REVERT_ERR_STR, ethCall.getRevertReason()));
        }
    }
}
