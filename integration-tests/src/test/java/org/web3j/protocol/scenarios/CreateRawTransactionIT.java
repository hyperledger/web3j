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
package org.web3j.protocol.scenarios;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.test.contract.Fibonacci;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/** Create, sign and send a raw transaction. */
@EVMTest(type = NodeType.BESU)
public class CreateRawTransactionIT extends Scenario {

    @BeforeAll
    public static void setUp(Web3j web3j) {
        Scenario.web3j = web3j;
    }

    @Test
    public void testTransferEther() throws Exception {
        final BigInteger nonce = getNonce(ALICE.getAddress());
        final RawTransaction rawTransaction = createEtherTransaction(nonce, BOB.getAddress());

        final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, ALICE);
        final String hexValue = Numeric.toHexString(signedMessage);

        final EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        final String transactionHash = ethSendTransaction.getTransactionHash();

        assertFalse(transactionHash.isEmpty());

        final TransactionReceipt transactionReceipt = waitForTransactionReceipt(transactionHash);

        assertEquals(transactionHash, transactionReceipt.getTransactionHash());
    }

    @Test
    public void testDeploySmartContract() throws Exception {
        final BigInteger nonce = getNonce(ALICE.getAddress());
        final RawTransaction rawTransaction = createSmartContractTransaction(nonce);

        final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, ALICE);
        final String hexValue = Numeric.toHexString(signedMessage);

        final EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        final String transactionHash = ethSendTransaction.getTransactionHash();

        assertFalse(transactionHash.isEmpty());

        final TransactionReceipt transactionReceipt = waitForTransactionReceipt(transactionHash);

        assertEquals(transactionHash, transactionReceipt.getTransactionHash());

        assertFalse(
                rawTransaction.getGasLimit().equals(transactionReceipt.getGasUsed()),
                "Contract execution ran out of gas");
    }

    private static RawTransaction createEtherTransaction(
            final BigInteger nonce, final String toAddress) {
        final BigInteger value = Convert.toWei("0.5", Convert.Unit.ETHER).toBigInteger();

        return RawTransaction.createEtherTransaction(nonce, GAS_PRICE, GAS_LIMIT, toAddress, value);
    }

    private static RawTransaction createSmartContractTransaction(final BigInteger nonce)
            throws Exception {
        return RawTransaction.createContractTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, BigInteger.ZERO, Fibonacci.BINARY);
    }

    BigInteger getNonce(final String address) throws Exception {
        final EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();

        return ethGetTransactionCount.getTransactionCount();
    }
}
