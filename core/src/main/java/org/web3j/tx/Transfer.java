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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import static java.util.Collections.emptyList;

/** Class for performing Ether transactions on the Ethereum blockchain. */
public class Transfer {

    // This is the cost to send Ether between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);

    private static final Function FUNC_TRANSFER =
            new Function("transfer", emptyList(), emptyList());

    private final Web3j web3j;
    private final TransactionManager transactionManager;
    private final EnsResolver ensResolver;

    public Transfer(final Web3j web3j, final TransactionManager transactionManager) {
        this.web3j = web3j;
        this.transactionManager = transactionManager;
        this.ensResolver = new EnsResolver(web3j);
    }

    /**
     * Execute the provided function as a transaction synchronously. This is intended for one-off
     * fund transfers. For multiple, create an instance.
     *
     * <p>Given the duration required to execute a transaction, asynchronous execution is strongly
     * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit, BigInteger,
     * BigInteger)}.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     * @return {@link RemoteTransaction} containing executing transaction
     */
    public static RemoteTransaction<TransactionReceipt> sendFunds(
            final Web3j web3j,
            final Credentials credentials,
            final String toAddress,
            final BigDecimal value,
            final Convert.Unit unit) {

        final TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        return new Transfer(web3j, transactionManager).sendFunds(toAddress, value, unit);
    }

    /**
     * Execute the provided function as a transaction.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     * @return {@link RemoteTransaction} containing executing transaction
     */
    public RemoteTransaction<TransactionReceipt> sendFunds(
            final String toAddress, final BigDecimal value, final Convert.Unit unit) {
        return sendFunds(toAddress, value, unit, null, null);
    }

    /**
     * Execute the provided function as a transaction.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     * @param gasPrice transaction gas price
     * @param gasLimit transaction gas limit
     * @return {@link RemoteTransaction} containing executing transaction
     */
    public RemoteTransaction<TransactionReceipt> sendFunds(
            final String toAddress,
            final BigDecimal value,
            final Convert.Unit unit,
            final BigInteger gasPrice,
            final BigInteger gasLimit) {

        Objects.requireNonNull(toAddress);
        Objects.requireNonNull(value);
        Objects.requireNonNull(unit);

        final BigDecimal weiValue = Convert.toWei(value, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: "
                            + value
                            + " "
                            + unit.toString()
                            + " = "
                            + weiValue
                            + " Wei");
        }

        return new TransferFunds(toAddress, weiValue.toBigIntegerExact(), gasPrice, gasLimit);
    }

    /**
     * Return the current gas price from the ethereum node.
     *
     * <p>Note: this method was previously called {@code getGasPrice} but was renamed to distinguish
     * it when a bean accessor method on {@link Contract} was added with that name. If you have a
     * Contract subclass that is calling this method (unlikely since those classes are usually
     * generated and until very recently those generated subclasses were marked {@code final}), then
     * you will need to change your code to call this method instead, if you want the dynamic
     * behavior.
     *
     * @return the current gas price, determined dynamically at invocation
     * @throws IOException if there's a problem communicating with the ethereum node
     */
    public BigInteger requestCurrentGasPrice() throws IOException {
        final EthGasPrice ethGasPrice = web3j.ethGasPrice().send();

        return ethGasPrice.getGasPrice();
    }

    private class TransferFunds implements RemoteTransaction<TransactionReceipt> {

        private final String toAddress;
        private final BigInteger value;
        private BigInteger gasPrice;
        private BigInteger gasLimit;

        private TransferFunds(
                final String toAddress,
                final BigInteger value,
                final BigInteger gasPrice,
                final BigInteger gasLimit) {
            this.toAddress = toAddress;
            this.value = value;
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
        }

        @Override
        public TransactionReceipt send() throws IOException, TransactionException {
            final String resolvedAddress = ensResolver.resolve(toAddress);
            if (gasPrice == null) {
                gasPrice = requestCurrentGasPrice();
            }
            if (gasLimit == null) {
                gasLimit = GAS_LIMIT;
            }
            return new RemoteTransactionCall0(
                            web3j,
                            FUNC_TRANSFER,
                            resolvedAddress,
                            transactionManager,
                            null,
                            "",
                            value,
                            false,
                            new StaticGasProvider(gasPrice, gasLimit))
                    .send();
        }
    }

    public static RemoteCall<TransactionReceipt> sendFundsEIP1559(
            Web3j web3j,
            Credentials credentials,
            String toAddress,
            BigDecimal value,
            Convert.Unit unit,
            BigInteger gasLimit,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas)
            throws IOException {
        EthChainId chainId = web3j.ethChainId().send();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

        return new RemoteCall<>(
                () ->
                        new Transfer(web3j, transactionManager)
                                .sendEIP1559(
                                        chainId.getChainId().longValue(),
                                        toAddress,
                                        value,
                                        unit,
                                        gasLimit,
                                        maxPriorityFeePerGas,
                                        maxFeePerGas));
    }

    private TransactionReceipt sendEIP1559(
            long chainId,
            String toAddress,
            BigDecimal value,
            Convert.Unit unit,
            BigInteger gasLimit,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas)
            throws IOException, InterruptedException, TransactionException {

        BigDecimal weiValue = Convert.toWei(value, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: "
                            + value
                            + " "
                            + unit.toString()
                            + " = "
                            + weiValue
                            + " Wei");
        }

        String resolvedAddress = ensResolver.resolve(toAddress);
        return sendEIP1559(
                chainId,
                resolvedAddress,
                "",
                weiValue.toBigIntegerExact(),
                gasLimit,
                maxPriorityFeePerGas,
                maxFeePerGas);
    }
}
