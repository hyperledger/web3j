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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.privacy.PrivateEnclaveKey;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.eea.crypto.PrivateTransactionEncoder;
import org.web3j.protocol.eea.crypto.RawPrivateTransaction;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.BesuPrivacyGasProvider;
import org.web3j.tx.response.PollingPrivateTransactionReceiptProcessor;
import org.web3j.tx.response.PrivateTransactionReceiptProcessor;
import org.web3j.utils.Base64String;
import org.web3j.utils.Numeric;

import static org.web3j.utils.Restriction.RESTRICTED;

public abstract class PrivateTransactionManager extends TransactionManager {

    private static final Logger log = LoggerFactory.getLogger(PrivateTransactionManager.class);

    private final PrivateTransactionReceiptProcessor transactionReceiptProcessor;

    private final Besu besu;
    private final BesuPrivacyGasProvider gasProvider;
    private final Credentials credentials;
    private final long chainId;
    private final Base64String privateFrom;

    protected PrivateTransactionManager(
            final Besu besu,
            final BesuPrivacyGasProvider gasProvider,
            final Credentials credentials,
            final long chainId,
            final Base64String privateFrom,
            final PrivateTransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());
        this.besu = besu;
        this.gasProvider = gasProvider;
        this.credentials = credentials;
        this.chainId = chainId;
        this.privateFrom = privateFrom;
        this.transactionReceiptProcessor = transactionReceiptProcessor;
    }

    protected PrivateTransactionManager(
            final Besu besu,
            final BesuPrivacyGasProvider gasProvider,
            final Credentials credentials,
            final long chainId,
            final Base64String privateFrom,
            final int attempts,
            final int sleepDuration) {
        this(
                besu,
                gasProvider,
                credentials,
                chainId,
                privateFrom,
                new PollingPrivateTransactionReceiptProcessor(besu, sleepDuration, attempts));
    }

    protected PrivateTransactionManager(
            final Besu besu,
            final BesuPrivacyGasProvider gasProvider,
            final Credentials credentials,
            final long chainId,
            final Base64String privateFrom) {
        this(
                besu,
                gasProvider,
                credentials,
                chainId,
                privateFrom,
                new PollingPrivateTransactionReceiptProcessor(
                        besu, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH));
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
            final BigInteger value,
            boolean constructor)
            throws IOException {

        final BigInteger nonce =
                besu.privGetTransactionCount(credentials.getAddress(), getPrivacyGroupId())
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

        return signAndSend(transaction);
    }

    public EthSendTransaction sendTransactionEIP1559(
            BigInteger gasPremium,
            BigInteger feeCap,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException {
        final BigInteger nonce =
                besu.privGetTransactionCount(credentials.getAddress(), getPrivacyGroupId())
                        .send()
                        .getTransactionCount();

        final Object privacyGroupIdOrPrivateFor = privacyGroupIdOrPrivateFor();

        final RawPrivateTransaction transaction;
        if (privacyGroupIdOrPrivateFor instanceof Base64String) {
            transaction =
                    RawPrivateTransaction.createTransactionEIP1559(
                            nonce,
                            gasPremium,
                            feeCap,
                            gasLimit,
                            to,
                            data,
                            privateFrom,
                            (Base64String) privacyGroupIdOrPrivateFor,
                            RESTRICTED);
        } else {
            transaction =
                    RawPrivateTransaction.createTransactionEIP1559(
                            nonce,
                            gasPremium,
                            feeCap,
                            gasLimit,
                            to,
                            data,
                            privateFrom,
                            (List<Base64String>) privacyGroupIdOrPrivateFor,
                            RESTRICTED);
        }

        return signAndSend(transaction);
    }

    @Override
    public String sendCall(
            final String to, final String data, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        EthCall ethCall =
                besu.privCall(
                                getPrivacyGroupId().toString(),
                                Transaction.createEthCallTransaction(getFromAddress(), to, data),
                                defaultBlockParameter)
                        .send();

        assertCallNotReverted(ethCall);
        return ethCall.getValue();
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

    @Override
    public EthGetCode getCode(
            final String contractAddress, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        return this.besu
                .privGetCode(
                        this.getPrivacyGroupId().toString(), contractAddress, defaultBlockParameter)
                .send();
    }

    public String sign(RawPrivateTransaction rawTransaction) {

        byte[] signedMessage;

        if (chainId > ChainIdLong.NONE) {
            signedMessage =
                    PrivateTransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = PrivateTransactionEncoder.signMessage(rawTransaction, credentials);
        }

        return Numeric.toHexString(signedMessage);
    }

    public EthSendTransaction signAndSend(RawPrivateTransaction rawTransaction) throws IOException {
        String hexValue = sign(rawTransaction);
        return this.besu.eeaSendRawTransaction(hexValue).send();
    }

    public PrivateEnclaveKey signAndDistribute(RawPrivateTransaction rawTransaction)
            throws IOException {
        String hexValue = sign(rawTransaction);
        return this.besu.privDistributeRawTransaction(hexValue).send();
    }
}
