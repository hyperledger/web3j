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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.eea.crypto.PrivateTransactionEncoder;
import org.web3j.protocol.eea.crypto.RawPrivateTransaction;
import org.web3j.protocol.eea.response.PrivateTransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.pantheon.Pantheon;
import org.web3j.tx.gas.PantheonPrivacyGasProvider;
import org.web3j.tx.response.PollingPrivateTransactionReceiptProcessor;
import org.web3j.tx.response.PrivateTransactionReceiptProcessor;
import org.web3j.utils.Base64String;
import org.web3j.utils.Numeric;

import static org.web3j.utils.Restriction.RESTRICTED;

public abstract class PrivateTransactionManager extends TransactionManager {
    private static final Logger log = LoggerFactory.getLogger(PrivateTransactionManager.class);

    private final PrivateTransactionReceiptProcessor transactionReceiptProcessor;

    private final Pantheon pantheon;
    private final PantheonPrivacyGasProvider gasProvider;
    private final Credentials credentials;
    private final long chainId;
    private final Base64String privateFrom;

    protected PrivateTransactionManager(
            final Pantheon pantheon,
            final PantheonPrivacyGasProvider gasProvider,
            final Credentials credentials,
            final long chainId,
            final Base64String privateFrom,
            final PrivateTransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());
        this.pantheon = pantheon;
        this.gasProvider = gasProvider;
        this.credentials = credentials;
        this.chainId = chainId;
        this.privateFrom = privateFrom;
        this.transactionReceiptProcessor = transactionReceiptProcessor;
    }

    protected PrivateTransactionManager(
            final Pantheon pantheon,
            final PantheonPrivacyGasProvider gasProvider,
            final Credentials credentials,
            final long chainId,
            final Base64String privateFrom,
            final int attempts,
            final int sleepDuration) {
        this(
                pantheon,
                gasProvider,
                credentials,
                chainId,
                privateFrom,
                new PollingPrivateTransactionReceiptProcessor(pantheon, attempts, sleepDuration));
    }

    protected PrivateTransactionManager(
            final Pantheon pantheon,
            final PantheonPrivacyGasProvider gasProvider,
            final Credentials credentials,
            final long chainId,
            final Base64String privateFrom) {
        this(
                pantheon,
                gasProvider,
                credentials,
                chainId,
                privateFrom,
                new PollingPrivateTransactionReceiptProcessor(
                        pantheon, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH));
    }

    @Override
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException, TransactionException {

        EthSendTransaction ethSendTransaction =
                sendTransaction(gasPrice, gasLimit, to, data, value);
        return processResponse(ethSendTransaction);
    }

    public Base64String getPrivateFrom() {
        return privateFrom;
    }

    protected abstract Base64String getPrivacyGroupId();

    protected abstract Object privacyGroupIdOrPrivateFor();

    @SuppressWarnings("unchecked")
    @Override
    public EthSendTransaction sendTransaction(
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final BigInteger value)
            throws IOException {

        final BigInteger nonce =
                pantheon.privGetTransactionCount(credentials.getAddress(), getPrivacyGroupId())
                        .send()
                        .getTransactionCount();

        final Object privacyGroupIdOrPrivateFor = privacyGroupIdOrPrivateFor();

        final RawPrivateTransaction transaction;
        if (privacyGroupIdOrPrivateFor instanceof Base64String) {
            transaction =
                    RawPrivateTransaction.createTransaction(
                            nonce,
                            gasPrice,
                            gasLimit,
                            to,
                            data,
                            privateFrom,
                            (Base64String) privacyGroupIdOrPrivateFor,
                            RESTRICTED);
        } else {
            transaction =
                    RawPrivateTransaction.createTransaction(
                            nonce,
                            gasPrice,
                            gasLimit,
                            to,
                            data,
                            privateFrom,
                            (List<Base64String>) privacyGroupIdOrPrivateFor,
                            RESTRICTED);
        }

        final String rawSignedTransaction =
                Numeric.toHexString(
                        PrivateTransactionEncoder.signMessage(transaction, chainId, credentials));

        return pantheon.eeaSendRawTransaction(rawSignedTransaction).send();
    }

    @Override
    public String sendCall(
            final String to, final String data, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        try {
            EthSendTransaction est =
                    sendTransaction(
                            gasProvider.getGasPrice(),
                            gasProvider.getGasLimit(),
                            to,
                            data,
                            BigInteger.ZERO);
            final TransactionReceipt ptr = processResponse(est);
            return ((PrivateTransactionReceipt) ptr).getOutput();
        } catch (TransactionException e) {
            log.error("Failed to execute call", e);
            return null;
        }
    }

    private TransactionReceipt processResponse(final EthSendTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException(
                    "Error processing transaction request: "
                            + transactionResponse.getError().getMessage());
        }

        final String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }
}
