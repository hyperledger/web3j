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

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthChainId;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/** Class for performing Ether transactions on the Ethereum blockchain. */
public class Transfer extends ManagedTransaction {

    // This is the cost to send Ether between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);

    public Transfer(Web3j web3j, TransactionManager transactionManager) {
        super(web3j, transactionManager);
    }

    public static RemoteCall<TransactionReceipt> sendFunds(
            final Web3j web3j,
            final Credentials credentials,
            final String toAddress,
            final BigDecimal value,
            final Convert.Unit unit)
            throws IOException {

        final TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);
        final Transfer transfer = new Transfer(web3j, transactionManager);

        return transfer.sendFunds(toAddress, value, unit);
    }

    /**
     * Execute the provided function as a transaction asynchronously. This is intended for one-off
     * fund transfers. For multiple, create an instance.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     * @return {@link RemoteTransaction} containing executing transaction
     */
    public RemoteCall<TransactionReceipt> sendFunds(
            final String toAddress, final BigDecimal value, final Convert.Unit unit) {
        return sendFunds(toAddress, value, unit, null, null);
    }

    /**
     * Given the duration required to execute a transaction, asynchronous execution is strongly
     * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     * @param gasPrice transaction gas price
     * @param gasLimit transaction gas limit
     * @return {@link RemoteTransaction} containing executing transaction
     */
    public RemoteCall<TransactionReceipt> sendFunds(
            final String toAddress,
            final BigDecimal value,
            final Convert.Unit unit,
            final BigInteger gasPrice,
            final BigInteger gasLimit) {

        Objects.requireNonNull(toAddress);
        Objects.requireNonNull(value);
        Objects.requireNonNull(unit);

        return new TransferCall(toAddress, value, unit, gasPrice, gasLimit);
    }

    private class TransferCall implements RemoteCall<TransactionReceipt> {

        private String toAddress;
        private BigDecimal value;
        private Convert.Unit unit;
        private BigInteger gasPrice;
        private BigInteger gasLimit;

        private TransferCall(
                final String toAddress,
                final BigDecimal value,
                final Convert.Unit unit,
                final BigInteger gasPrice,
                final BigInteger gasLimit) {
            this.toAddress = toAddress;
            this.value = value;
            this.unit = unit;
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
        }

        @Override
        public TransactionReceipt call(DefaultBlockParameter blockParameter) throws IOException {
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
            final String resolvedAddress = ensResolver.resolve(toAddress);
            if (gasPrice == null) {
                gasPrice = requestCurrentGasPrice();
            }
            if (gasLimit == null) {
                gasLimit = GAS_LIMIT;
            }
            try {
                return Transfer.this.send(
                        resolvedAddress, "", weiValue.toBigIntegerExact(), gasPrice, gasLimit);
            } catch (TransactionException e) {
                throw new IOException(e);
            }
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
