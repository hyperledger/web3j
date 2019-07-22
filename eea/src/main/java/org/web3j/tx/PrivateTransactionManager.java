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

import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.eea.response.PrivateTransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.response.PollingPrivateTransactionReceiptProcessor;
import org.web3j.tx.response.PrivateTransactionReceiptProcessor;

public abstract class PrivateTransactionManager extends TransactionManager {
    private final PrivateTransactionReceiptProcessor transactionReceiptProcessor;

    protected PrivateTransactionManager(
            PrivateTransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        super(transactionReceiptProcessor, fromAddress);
        this.transactionReceiptProcessor = transactionReceiptProcessor;
    }

    protected PrivateTransactionManager(Eea eea, String enclavePublicKey, String fromAddress) {
        this(
                new PollingPrivateTransactionReceiptProcessor(
                        eea, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH),
                fromAddress);
    }

    protected PrivateTransactionManager(
            Eea eea,
            String enclavePublicKey,
            int attempts,
            long sleepDuration,
            String fromAddress) {
        this(
                new PollingPrivateTransactionReceiptProcessor(eea, sleepDuration, attempts),
                fromAddress);
    }

    @Override
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException, TransactionException {

        EthSendTransaction ethSendTransaction =
                sendTransaction(gasPrice, gasLimit, to, data, value);
        return processResponse(ethSendTransaction);
    }

    protected String executeCall(String to, String data) throws IOException, TransactionException {
        EthSendTransaction est =
                sendTransaction(
                        BigInteger.valueOf(5000),
                        BigInteger.valueOf(3000000),
                        to,
                        data,
                        BigInteger.ZERO);
        TransactionReceipt ptr = processResponse(est);
        return ((PrivateTransactionReceipt) ptr).getOutput();
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
}
