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
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Pair;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.exceptions.FailedReceiptException;
import org.web3j.tx.response.TransactionReceiptCallback;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Async;
import org.web3j.utils.Numeric;

/**
 * This class instance is returned after a successful submission of transaction. It contains the
 * transactionHash of the transaction and {@link #waitForReceipt()} can be called to get full detail
 * of the transaction after it appears in a block.
 */
public class EthTransactionReceiptInteraction implements RemoteCall<TransactionReceipt> {

    Logger logger = LoggerFactory.getLogger(EthTransactionReceiptInteraction.class);
    private final EthSendTransaction ethSendTransaction;
    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final BigInteger gasLimit;

    public EthTransactionReceiptInteraction(
            BigInteger gasLimit,
            EthSendTransaction ethSendTransaction,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        this.ethSendTransaction = ethSendTransaction;
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        ;
        this.gasLimit = gasLimit;
    }

    @Override
    public TransactionReceipt send()
            throws InterruptedException, IOException, TimeoutException, FailedReceiptException {
        return waitForReceipt();
    }

    /**
     * Send Async request and expect callback. This method returns immediately submitting the task
     * to a background thread. All the callbacks are handled by another thread.
     *
     * @param callback
     */
    public void sendAsync(TransactionReceiptCallback callback) {
        Async.submit(
                () -> {
                    try {
                        send(callback);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
    }

    /**
     * Use a callback mechanism to handle events. Note that everything will be blocking and all the
     * callbacks will be called on current thread.
     *
     * @param callback
     */
    public void send(TransactionReceiptCallback callback) throws InterruptedException {
        try {
            Pair<? extends TransactionReceipt, Optional<Response.Error>> optionalPair =
                    waitAndProcessResponse();
            try {
                callback.success(optionalPair.getFirst(), optionalPair.getSecond());
                // this is important!
                // Once I had a condition where success function threw Runtime exception.
                // then the exception handler was called,
                // This means both onSuccess and onFailure condition are called.
            } catch (Exception ex) {
                logger.error("Callback function threw Unexpected Exception", ex);
            }
        } catch (IOException | TimeoutException e) {
            callback.exception(e);
        }
    }

    public TransactionReceipt waitForReceipt()
            throws IOException, TimeoutException, InterruptedException, FailedReceiptException {
        Pair<? extends TransactionReceipt, Optional<Response.Error>> optionalPair =
                waitAndProcessResponse();
        if (optionalPair.getSecond().isPresent()) {
            throw new FailedReceiptException(
                    optionalPair.getSecond().get().getMessage(), optionalPair.getFirst());
        } else return optionalPair.getFirst();
    }

    public boolean hasError() {
        return ethSendTransaction.hasError();
    }

    public Response.Error getError() {
        return ethSendTransaction.getError();
    }

    public String getTransactionHash() {
        return ethSendTransaction.getTransactionHash();
    }

    private Pair<? extends TransactionReceipt, Optional<Response.Error>> waitAndProcessResponse()
            throws TimeoutException, InterruptedException, IOException {
        Pair<? extends TransactionReceipt, Optional<Response.Error>> optionalPair =
                transactionReceiptProcessor.waitForTransactionReceiptResponse(
                        ethSendTransaction.getTransactionHash());
        if (!optionalPair.getSecond().isPresent() && !optionalPair.getFirst().isStatusOK()) {
            TransactionReceipt first = optionalPair.getFirst();
            Response.Error error = new Response.Error();
            error.setCode(Numeric.decodeQuantity(first.getStatus()).intValue());
            error.setData(first.getStatus());
            if (gasLimit.equals(first.getGasUsed())) {
                error.setMessage(
                        "Transaction has failed with status: "
                                + first.getStatus()
                                + ".(not enough gas?)");
            } else {
                error.setMessage(
                        "Transaction has failed with status: "
                                + first.getStatus()
                                + ". Gas used: "
                                + first.getGasUsed());
            }
            return new Pair<>(first, Optional.of(error));
        }
        return optionalPair;
    }
}
