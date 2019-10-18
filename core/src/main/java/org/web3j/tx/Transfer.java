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
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.exceptions.RpcErrorResponseException;
import org.web3j.tx.interactions.EthTransactionReceiptInteraction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/** Class for performing Ether transactions on the Ethereum blockchain. */
public class Transfer extends ManagedTransaction {

    // This is the cost to send Ether between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);

    public Transfer(Web3j web3j, TransactionManager transactionManager) {
        super(web3j, transactionManager);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     * @return {@link Optional} containing our transaction receipt
     * @throws ExecutionException if the computation threw an exception
     */
    private EthTransactionReceiptInteraction send(
            String toAddress, BigDecimal value, Convert.Unit unit)
            throws IOException, RpcErrorResponseException {

        BigInteger gasPrice = requestCurrentGasPrice();
        return send(toAddress, value, unit, gasPrice, GAS_LIMIT);
    }

    private EthTransactionReceiptInteraction send(
            String toAddress,
            BigDecimal value,
            Convert.Unit unit,
            BigInteger gasPrice,
            BigInteger gasLimit)
            throws IOException, RpcErrorResponseException {

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
        return send(resolvedAddress, "", weiValue.toBigIntegerExact(), gasPrice, gasLimit);
    }

    public static EthTransactionReceiptInteraction sendFunds(
            Web3j web3j,
            Credentials credentials,
            String toAddress,
            BigDecimal value,
            Convert.Unit unit)
            throws IOException, RpcErrorResponseException {

        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

        return new Transfer(web3j, transactionManager).send(toAddress, value, unit);
    }

    /**
     * Execute the provided function as a transaction asynchronously. This is intended for one-off
     * fund transfers. For multiple, create an instance.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     * @return {@link RemoteCall} containing executing transaction
     */
    public EthTransactionReceiptInteraction sendFunds(
            String toAddress, BigDecimal value, Convert.Unit unit)
            throws IOException, RpcErrorResponseException {
        return send(toAddress, value, unit);
    }

    public EthTransactionReceiptInteraction sendFunds(
            String toAddress,
            BigDecimal value,
            Convert.Unit unit,
            BigInteger gasPrice,
            BigInteger gasLimit)
            throws IOException, RpcErrorResponseException {
        return send(toAddress, value, unit, gasPrice, gasLimit);
    }
}
