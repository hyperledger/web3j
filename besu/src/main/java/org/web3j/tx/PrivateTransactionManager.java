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

import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.privacy.PrivateEnclaveKey;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.crypto.RawPrivateTransaction;
import org.web3j.service.TxSignService;
import org.web3j.service.TxSignServiceImpl;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Base64String;
import org.web3j.utils.Numeric;
import org.web3j.utils.PrivacyGroupUtils;
import org.web3j.utils.Restriction;

public class PrivateTransactionManager extends TransactionManager {

    private final Besu besu;

    private final TxSignService txSignService;
    private final long chainId;

    private final Base64String privateFrom;

    private final List<Base64String> privateFor;
    private final Base64String privacyGroupId;

    private final Restriction restriction;

    public PrivateTransactionManager(
            final Besu besu,
            final Credentials credentials,
            final TransactionReceiptProcessor transactionReceiptProcessor,
            final long chainId,
            final Base64String privateFrom,
            final Base64String privacyGroupId,
            final Restriction restriction) {
        super(transactionReceiptProcessor, credentials.getAddress());
        this.besu = besu;
        this.chainId = chainId;
        this.privateFrom = privateFrom;
        this.privateFor = null;
        this.privacyGroupId = privacyGroupId;
        this.restriction = restriction;
        this.txSignService = new TxSignServiceImpl(credentials);
    }

    public PrivateTransactionManager(
            final Besu besu,
            final Credentials credentials,
            final TransactionReceiptProcessor transactionReceiptProcessor,
            final long chainId,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Restriction restriction) {
        super(transactionReceiptProcessor, credentials.getAddress());
        this.besu = besu;
        this.chainId = chainId;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.privacyGroupId = PrivacyGroupUtils.generateLegacyGroup(privateFrom, privateFor);
        this.restriction = restriction;
        this.txSignService = new TxSignServiceImpl(credentials);
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

        final BigInteger nonce =
                besu.privGetTransactionCount(txSignService.getAddress(), privacyGroupId)
                        .send()
                        .getTransactionCount();

        final RawPrivateTransaction transaction;

        if (privateFor != null) {
            transaction =
                    RawPrivateTransaction.createTransaction(
                            nonce,
                            gasPrice,
                            gasLimit,
                            to,
                            data,
                            privateFrom,
                            privateFor,
                            restriction);
        } else {
            transaction =
                    RawPrivateTransaction.createTransaction(
                            nonce,
                            gasPrice,
                            gasLimit,
                            to,
                            data,
                            privateFrom,
                            privacyGroupId,
                            restriction);
        }

        return signAndSend(transaction);
    }

    @Override
    public EthSendTransaction sendEIP1559Transaction(
            long chainId,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException {
        final BigInteger nonce =
                besu.privGetTransactionCount(txSignService.getAddress(), privacyGroupId)
                        .send()
                        .getTransactionCount();

        final RawPrivateTransaction transaction;
        if (privateFor != null) {
            transaction =
                    RawPrivateTransaction.createTransaction(
                            chainId,
                            nonce,
                            maxPriorityFeePerGas,
                            maxFeePerGas,
                            gasLimit,
                            to,
                            data,
                            privateFrom,
                            privateFor,
                            restriction);
        } else {
            transaction =
                    RawPrivateTransaction.createTransaction(
                            chainId,
                            nonce,
                            maxPriorityFeePerGas,
                            maxFeePerGas,
                            gasLimit,
                            to,
                            data,
                            privateFrom,
                            privacyGroupId,
                            restriction);
        }

        return signAndSend(transaction);
    }

    @Override
    public String sendCall(
            final String to, final String data, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        final EthCall ethCall =
                besu.privCall(
                                privacyGroupId.toString(),
                                Transaction.createEthCallTransaction(getFromAddress(), to, data),
                                defaultBlockParameter)
                        .send();

        assertCallNotReverted(ethCall);
        return ethCall.getValue();
    }

    @Override
    public EthGetCode getCode(
            final String contractAddress, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        return this.besu
                .privGetCode(privacyGroupId.toString(), contractAddress, defaultBlockParameter)
                .send();
    }

    public String sign(final RawPrivateTransaction rawTransaction) {

        final byte[] signedMessage = txSignService.sign(rawTransaction, chainId);

        return Numeric.toHexString(signedMessage);
    }

    public EthSendTransaction signAndSend(final RawPrivateTransaction rawTransaction)
            throws IOException {
        final String hexValue = sign(rawTransaction);
        return this.besu.eeaSendRawTransaction(hexValue).send();
    }

    public PrivateEnclaveKey signAndDistribute(RawPrivateTransaction rawTransaction)
            throws IOException {
        String hexValue = sign(rawTransaction);
        return this.besu.privDistributeRawTransaction(hexValue).send();
    }
}
