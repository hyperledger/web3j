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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.RpcErrorResponseException;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.FailedReceiptException;
import org.web3j.tx.response.TransactionCallBack;
import org.web3j.utils.Async;

/**
 * Interactions before submitting a transaction You can override - gasPrice and gasLimit instead of
 * using from the gasProvicer - nonce
 */
public final class EthTransactionInteraction implements RemoteFunctionCall<TransactionReceipt> {
    private static final Logger logger = LoggerFactory.getLogger(EthTransactionInteraction.class);
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private final String data;
    private final BigInteger weiValue;
    private final String to;
    private final TransactionManager transactionManager;
    private BigInteger nonce = null;

    public EthTransactionInteraction(
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger weiValue) {
        this.transactionManager = transactionManager;
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
        this.data = data;
        this.weiValue = weiValue;
        this.to = to;
    }

    public void setGasPriceInGWei(float price) {
        gasPrice =
                (BigDecimal.valueOf(price * 1_000_000_000).setScale(0, RoundingMode.FLOOR))
                        .toBigInteger();
    }

    public EthTransactionInteraction gasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
        return this;
    }

    public EthTransactionInteraction gasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
        return this;
    }

    public EthTransactionInteraction nonce(BigInteger nonce) {
        this.nonce = nonce;
        return this;
    }

    public EthTransactionReceiptInteraction submit() throws IOException, RpcErrorResponseException {
        return transactionManager.submitTransaction(gasPrice, gasLimit, to, data, weiValue, nonce);
    }

    /**
     * This should be deprecated so that send() should not be used for getting TransactionReceipt
     * directly. Later the deprecation will be remed and the function should return an instance of
     * EthTransactionReceiptInteraction
     *
     * @deprecated Deprecated in favor of {@link #submit()} then calling {@link
     *     EthTransactionReceiptInteraction#waitForReceipt()} for transaction receipt
     */
    @Deprecated
    public TransactionReceipt send()
            throws IOException, InterruptedException, TimeoutException, FailedReceiptException,
                    RpcErrorResponseException {
        return transactionManager
                .submitTransaction(gasPrice, gasLimit, to, data, weiValue, nonce)
                .waitForReceipt();
    }

    /**
     * Perform the
     *
     * @param callBack
     */
    public void submitAsync(TransactionCallBack callBack) {
        Async.submit(
                () -> {
                    submit(callBack);
                });
    }

    public String getTxData() {
        return data;
    }
    /**
     * same as send() but the code about what to do after send is passed as parameter. Note that
     * this methods blocks the current thread until the all the operation is not complete.
     *
     * @param callBack
     */
    public void submit(TransactionCallBack callBack) {
        try {
            EthTransactionReceiptInteraction send = this.submit();
            String transactionHash = send.getTransactionHash();
            try {
                callBack.transactionHash(transactionHash);
            } catch (Exception ex) {
                logger.error("Unexpected exception on transactionHash callback", ex);
            }
            // send For callback will not throw exception.
            send.send(callBack);

        } catch (Exception ex) {
            callBack.exception(ex);
        }
    }

    public String encodeFunctionCall() {
        return data;
    }
}
