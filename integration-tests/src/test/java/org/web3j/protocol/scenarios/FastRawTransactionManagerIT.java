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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.web3j.EVMTest;
import org.web3j.NodeType;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.response.Callback;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.QueuingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;

@EVMTest(type = NodeType.BESU)
@BenchmarkOptions(concurrency = 1, warmupRounds = 0, benchmarkRounds = 1)
public class FastRawTransactionManagerIT extends Scenario {

    private static final int COUNT = 10; // don't set too high if using a real Ethereum network
    private static final long POLLING_FREQUENCY = 15000;

    @BeforeAll
    public static void setUp(Web3j web3j) {
        Scenario.web3j = web3j;
    }

    @Test
    public void testTransactionPolling() throws Exception {

        final List<Future<TransactionReceipt>> transactionReceipts = new LinkedList<>();
        final FastRawTransactionManager transactionManager =
                new FastRawTransactionManager(
                        web3j,
                        ALICE,
                        new PollingTransactionReceiptProcessor(
                                web3j, POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH));

        final Transfer transfer = new Transfer(web3j, transactionManager);
        final BigInteger gasPrice = transfer.requestCurrentGasPrice();

        for (int i = 0; i < COUNT; i++) {

            final Future<TransactionReceipt> transactionReceiptFuture =
                    sendFunds(transfer, gasPrice).callAsync();
            transactionReceipts.add(transactionReceiptFuture);
        }

        for (int i = 0;
                i < DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH && !transactionReceipts.isEmpty();
                i++) {

            for (final Iterator<Future<TransactionReceipt>> iterator =
                            transactionReceipts.iterator();
                    iterator.hasNext(); ) {
                final Future<TransactionReceipt> transactionReceiptFuture = iterator.next();

                if (transactionReceiptFuture.isDone()) {
                    final TransactionReceipt transactionReceipt = transactionReceiptFuture.get();
                    assertFalse(transactionReceipt.getBlockHash().isEmpty());
                    iterator.remove();
                }
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(transactionReceipts.isEmpty());
    }

    @Test
    public void testTransactionQueuing() throws Exception {

        final Map<String, Object> pendingTransactions = new ConcurrentHashMap<>();
        final ConcurrentLinkedQueue<TransactionReceipt> transactionReceipts =
                new ConcurrentLinkedQueue<>();

        final FastRawTransactionManager transactionManager =
                new FastRawTransactionManager(
                        web3j,
                        ALICE,
                        new QueuingTransactionReceiptProcessor(
                                web3j,
                                new Callback() {
                                    @Override
                                    public void accept(
                                            final TransactionReceipt transactionReceipt) {
                                        transactionReceipts.add(transactionReceipt);
                                    }

                                    @Override
                                    public void exception(final Exception exception) {}
                                },
                                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH,
                                POLLING_FREQUENCY));

        final Transfer transfer = new Transfer(web3j, transactionManager);

        final BigInteger gasPrice = transfer.requestCurrentGasPrice();

        for (int i = 0; i < COUNT; i++) {
            final TransactionReceipt transactionReceipt = sendFunds(transfer, gasPrice).call();
            pendingTransactions.put(transactionReceipt.getTransactionHash(), new Object());
        }

        for (int i = 0;
                i < DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH && !pendingTransactions.isEmpty();
                i++) {
            for (final TransactionReceipt transactionReceipt : transactionReceipts) {
                assertFalse(transactionReceipt.getBlockHash().isEmpty());
                pendingTransactions.remove(transactionReceipt.getTransactionHash());
                transactionReceipts.remove(transactionReceipt);
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(transactionReceipts.isEmpty());
    }

    private RemoteCall<TransactionReceipt> sendFunds(
            final Transfer transfer, final BigInteger gasPrice) {
        return transfer.sendFunds(
                BOB.getAddress(),
                BigDecimal.valueOf(1.0),
                Convert.Unit.KWEI,
                gasPrice,
                Transfer.GAS_LIMIT);
    }
}
