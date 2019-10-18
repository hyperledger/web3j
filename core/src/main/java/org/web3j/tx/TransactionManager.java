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
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.EthCallException;
import org.web3j.protocol.exceptions.RpcErrorResponseException;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.interactions.EthCallInteraction;
import org.web3j.tx.interactions.EthTransactionReceiptInteraction;
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

    /**
     * Use {@link #submitTransaction(BigInteger, BigInteger, String, String, BigInteger)} instead
     * Code should be changed to submitTransaction().waitForReceipt() to get same result as before
     */
    @Deprecated
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException, TransactionException, InterruptedException, EthCallException {

        return executeTransaction(gasPrice, gasLimit, to, data, value, false);
    }

    /**
     * @deprecated use {@link #submitTransaction(BigInteger, BigInteger, String, String, BigInteger,
     *     boolean)} instead Code should be changed to submitTransaction().waitForReceipt() to get
     *     same result as before
     */
    @Deprecated
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException, TransactionException, InterruptedException {

        EthSendTransaction ethSendTransaction =
                sendTransaction(gasPrice, gasLimit, to, data, value, constructor);
        return processResponse(ethSendTransaction);
    }

    public EthSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException {
        return sendTransaction(gasPrice, gasLimit, to, data, value, false);
    }

    public EthSendTransaction sendTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger nonce,
            BigInteger value)
            throws IOException {
        return sendTransaction(gasPrice, gasLimit, to, data, value, nonce, false);
    }

    public EthSendTransaction sendTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException {
        return sendTransaction(gasPrice, gasLimit, to, data, null, value, constructor);
    }

    public abstract EthSendTransaction sendTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger nonce,
            BigInteger value,
            boolean constructor)
            throws IOException;

    public abstract String sendCall(
            String to, String data, DefaultBlockParameter defaultBlockParameter)
            throws IOException, EthCallException;

    public EthTransactionReceiptInteraction submitTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            BigInteger nonce)
            throws IOException, RpcErrorResponseException {
        return submitTransaction(gasPrice, gasLimit, to, data, value, nonce, false);
    }

    public EthTransactionReceiptInteraction submitTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            BigInteger nonce,
            boolean constructor)
            throws IOException, RpcErrorResponseException {
        EthSendTransaction ethSendTransaction =
                sendTransaction(gasPrice, gasLimit, to, data, nonce, value, constructor);
        if (ethSendTransaction.hasError()) {
            if (ethSendTransaction.hasError()) {
                throw new RpcErrorResponseException(ethSendTransaction.getError());
            }
        }
        return new EthTransactionReceiptInteraction(
                gasLimit, ethSendTransaction, this.transactionReceiptProcessor);
    }

    public EthTransactionReceiptInteraction submitTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException, RpcErrorResponseException {
        return submitTransaction(gasPrice, gasLimit, to, data, value, null, constructor);
    }

    public EthTransactionReceiptInteraction submitTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException, RpcErrorResponseException {
        return submitTransaction(gasPrice, gasLimit, to, data, value, false);
    }

    public EthCallInteraction<String> sendInteractiveCall(String to, String data) {
        return sendInteractiveCall(to, data, a -> a);
    }

    public <T> EthCallInteraction<T> sendInteractiveCall(
            String to, String data, EthCallInteraction.CallResponseParser<T> responseParser) {
        return new EthCallInteraction<T>(data, to, this, responseParser);
    }

    public String getFromAddress() {
        return fromAddress;
    }

    @Deprecated
    private TransactionReceipt processResponse(EthSendTransaction transactionResponse)
            throws IOException, TransactionException, InterruptedException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException(
                    "Error processing transaction request: "
                            + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }
}
