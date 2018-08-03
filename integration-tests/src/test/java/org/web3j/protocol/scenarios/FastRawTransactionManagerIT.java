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

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.response.Callback;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.QueuingTransactionReceiptProcessor;
import org.web3j.utils.Convert;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.web3j.protocol.core.TestParameters.BENCHMARK_TESTING_ON;
import static org.web3j.protocol.core.TestParameters.FAST_RAW_TRANSACTION_MANAGER_IT_COUNT;
import static org.web3j.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;

@RunWith(Parameterized.class)
public class FastRawTransactionManagerIT extends Scenario {

    private static final int COUNT = FAST_RAW_TRANSACTION_MANAGER_IT_COUNT;
    private static final long POLLING_FREQUENCY = 15000;

    @Rule
    public TestRule benchmarkRun = BENCHMARK_TESTING_ON ? new BenchmarkRule() :
            (base, description) -> base;

    private final Credentials sender;
    private final Credentials receipient;

    public FastRawTransactionManagerIT(
            @SuppressWarnings("unused") String ignoredTestName,
            Credentials sender,
            Credentials recipient) {
        this.sender = sender;
        this.receipient = recipient;
    }

    @Test
    public void testTransactionPolling() throws Exception {

        List<Future<TransactionReceipt>> transactionReceipts = new LinkedList<>();
        FastRawTransactionManager transactionManager = new FastRawTransactionManager(
                web3j, sender,
                new PollingTransactionReceiptProcessor(
                        web3j, POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH));

        Transfer transfer = new Transfer(web3j, transactionManager);
        BigInteger gasPrice = transfer.requestCurrentGasPrice();

        for (int i = 0; i < COUNT; i++) {

            Future<TransactionReceipt> transactionReceiptFuture =
                    createTransaction(transfer, gasPrice).sendAsync();
            transactionReceipts.add(transactionReceiptFuture);
        }

        for (int i = 0; i < DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
                && !transactionReceipts.isEmpty(); i++) {

            for (Iterator<Future<TransactionReceipt>> iterator = transactionReceipts.iterator();
                    iterator.hasNext(); ) {
                Future<TransactionReceipt> transactionReceiptFuture = iterator.next();

                if (transactionReceiptFuture.isDone()) {
                    TransactionReceipt transactionReceipt = transactionReceiptFuture.get();
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

        Map<String, Object> pendingTransactions = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<TransactionReceipt> transactionReceipts =
                new ConcurrentLinkedQueue<>();

        FastRawTransactionManager transactionManager = new FastRawTransactionManager(
                web3j, sender,
                new QueuingTransactionReceiptProcessor(web3j, new Callback() {
                    @Override
                    public void accept(TransactionReceipt transactionReceipt) {
                        transactionReceipts.add(transactionReceipt);
                    }

                    @Override
                    public void exception(Exception exception) {

                    }
                }, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH, POLLING_FREQUENCY));

        Transfer transfer = new Transfer(web3j, transactionManager);

        BigInteger gasPrice = transfer.requestCurrentGasPrice();

        for (int i = 0; i < COUNT; i++) {
            TransactionReceipt transactionReceipt = createTransaction(transfer, gasPrice).send();
            pendingTransactions.put(transactionReceipt.getTransactionHash(), new Object());
        }

        for (int i = 0; i < DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
                && !pendingTransactions.isEmpty(); i++) {
            for (TransactionReceipt transactionReceipt : transactionReceipts) {
                assertFalse(transactionReceipt.getBlockHash().isEmpty());
                pendingTransactions.remove(transactionReceipt.getTransactionHash());
                transactionReceipts.remove(transactionReceipt);
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(transactionReceipts.isEmpty());
    }


    private RemoteCall<TransactionReceipt> createTransaction(
            Transfer transfer, BigInteger gasPrice) {
        return transfer.sendFunds(
                receipient.getAddress(), BigDecimal.valueOf(1.0), Convert.Unit.KWEI,
                gasPrice, Transfer.GAS_LIMIT);
    }

    @Parameterized.Parameters(name = "Test #{index}: {0}")
    public static List<Object[]> parameters() {
        return transferTestParameters();
    }
}
