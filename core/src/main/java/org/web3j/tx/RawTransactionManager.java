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

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.exceptions.TxHashMismatchException;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Numeric;
import org.web3j.utils.TxHashVerifier;

/**
 * TransactionManager implementation using Ethereum wallet file to create and sign transactions
 * locally.
 *
 * <p>This transaction manager provides support for specifying the chain id for transactions as per
 * <a href="https://github.com/ethereum/EIPs/issues/155">EIP155</a>, as well as for locally signing
 * RawTransaction instances without broadcasting them.
 */
public class RawTransactionManager extends TransactionManager {

    private final Web3j web3j;
    final Credentials credentials;

    private final long chainId;

    protected TxHashVerifier txHashVerifier = new TxHashVerifier();

    public RawTransactionManager(final Web3j web3j, final Credentials credentials, final long chainId) {
        super(web3j, credentials.getAddress());

        this.web3j = web3j;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(
            final Web3j web3j,
            final Credentials credentials,
            final long chainId,
            final TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());

        this.web3j = web3j;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(
            final Web3j web3j, final Credentials credentials, final long chainId, final int attempts, final long sleepDuration) {
        super(web3j, attempts, sleepDuration, credentials.getAddress());

        this.web3j = web3j;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(final Web3j web3j, final Credentials credentials) {
        this(web3j, credentials, ChainId.NONE);
    }

    public RawTransactionManager(
            final Web3j web3j, final Credentials credentials, final int attempts, final int sleepDuration) {
        this(web3j, credentials, ChainId.NONE, attempts, sleepDuration);
    }

    protected BigInteger getNonce() throws IOException {
        final EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(
                                credentials.getAddress(), DefaultBlockParameterName.PENDING)
                        .send();

        return ethGetTransactionCount.getTransactionCount();
    }

    public TxHashVerifier getTxHashVerifier() {
        return txHashVerifier;
    }

    public void setTxHashVerifier(final TxHashVerifier txHashVerifier) {
        this.txHashVerifier = txHashVerifier;
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

        final BigInteger nonce = getNonce();

        final RawTransaction rawTransaction =
                RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

        return signAndSend(rawTransaction);
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

        final BigInteger nonce = getNonce();

        final RawTransaction rawTransaction =
                RawTransaction.createTransaction(
                        chainId,
                        nonce,
                        gasLimit,
                        to,
                        value,
                        data,
                        maxPriorityFeePerGas,
                        maxFeePerGas);

        return signAndSend(rawTransaction);
    }

    @Override
    public String sendCall(final String to, final String data, final DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        final EthCall ethCall =
                web3j.ethCall(
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
        return web3j.ethGetCode(contractAddress, defaultBlockParameter).send();
    }

    /*
     * @param rawTransaction a RawTransaction istance to be signed
     * @return The transaction signed and encoded without ever broadcasting it
     */
    public String sign(final RawTransaction rawTransaction) {

        final byte[] signedMessage;

        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        return Numeric.toHexString(signedMessage);
    }

    public EthSendTransaction signAndSend(final RawTransaction rawTransaction) throws IOException {
        final String hexValue = sign(rawTransaction);
        final EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        if (ethSendTransaction != null && !ethSendTransaction.hasError()) {
            final String txHashLocal = Hash.sha3(hexValue);
            final String txHashRemote = ethSendTransaction.getTransactionHash();
            if (!txHashVerifier.verify(txHashLocal, txHashRemote)) {
                throw new TxHashMismatchException(txHashLocal, txHashRemote);
            }
        }

        return ethSendTransaction;
    }
}
