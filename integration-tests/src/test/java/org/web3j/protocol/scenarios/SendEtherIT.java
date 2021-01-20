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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/** Simple integration test to demonstrate sending of Ether between parties. */
@EVMTest(type = NodeType.BESU)
public class SendEtherIT extends Scenario {

    @BeforeAll
    public static void beforeAll(Web3j web3j) {
        Scenario.web3j = web3j;
    }

    @Test
    public void testTransferEther() throws Exception {

        BigInteger nonce = getNonce(ALICE.getAddress());
        BigInteger value = Convert.toWei("0.5", Convert.Unit.ETHER).toBigInteger();

        RawTransaction transaction =
                RawTransaction.createEtherTransaction(
                        nonce, GAS_PRICE, GAS_LIMIT, BOB.getAddress(), value);

        byte[] signedTransaction = TransactionEncoder.signMessage(transaction, ALICE);

        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(Numeric.toHexString(signedTransaction))
                        .sendAsync()
                        .get();

        String transactionHash = ethSendTransaction.getTransactionHash();

        assertFalse(transactionHash.isEmpty());

        TransactionReceipt transactionReceipt = waitForTransactionReceipt(transactionHash);

        assertEquals(transactionReceipt.getTransactionHash(), (transactionHash));
    }

    /*
    Valid transaction receipt:
    "{"jsonrpc":"2.0",
        "id":1,
        "result":{
           "blockHash":"0x35a865cf2ba4efc3642b17a651f9e896dfebcdea39bfd0741b6f629e1be31a27",
           "blockNumber":"0x1c155f",
           "contractAddress":null,
           "cumulativeGasUsed":"0x5208",
           "from":"0x19e03255f667bdfd50a32722df860b1eeaf4d635",
           "gasUsed":"0x5208",
           "logs":[

           ],
           "root":"327e1e81c85cb710fe81cb8c0f824e9e49c3bf200e5e1149f589140145df10e3",
           "to":"0x9c98e381edc5fe1ac514935f3cc3edaa764cf004",
           "transactionHash":"0x16e41aa9d97d1c3374a4cb9599febdb24d4d5648b607c99e01a8e79e3eab2c34",
           "transactionIndex":"0x0"
        }
     */
    @Test
    public void testTransfer() throws Exception {
        TransactionReceipt transactionReceipt =
                Transfer.sendFunds(
                                web3j,
                                ALICE,
                                BOB.getAddress(),
                                BigDecimal.valueOf(0.2),
                                Convert.Unit.ETHER)
                        .send();
        assertFalse(transactionReceipt.getBlockHash().isEmpty());
    }
}
