package org.web3j.protocol.scenarios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Test;

import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class FastRawTransactionManagerIT extends Scenario {

    private static final int COUNT = 1;  // don't set too high if using a real Ethereum network

    @Test
    public void testTransaction() throws Exception {

        List<Future<TransactionReceipt>> transactionReceipts = new LinkedList<>();

        FastRawTransactionManager transactionManager = new FastRawTransactionManager(
                parity, ALICE);

        // We need to explicitly reset the nonce here, due to a race condition is taking place in
        // this test, where we experience deadlock in the entry point of the
        // RawTransactionManager.getNonce() method called within the
        // FastRawTransactionManager.getNonce() method. Using a sync call to getNonce() instead
        // of async as is currently the case also stops the issue from occurring.
        //
        // Bizarrely this issue does not occur when  the gas price is not set on the transaction,
        // and getGasPrice() is called on prior to sending each transaction. i.e. instead we use:
        //
        //      Future<TransactionReceipt> transactionReceiptFuture = transfer.sendFundsAsync(
        //            BOB.getAddress(), BigDecimal.valueOf(0.1), Convert.Unit.ETHER);
        //
        // One fix would be to migrate to synchronous calls throughout the
        // ManagedTransaction parent and child classes.
        transactionManager.resetNonce();

        Transfer transfer = new Transfer(parity, transactionManager);

        BigInteger gasPrice = transfer.getGasPrice();

        for (int i = 0; i < COUNT; i++) {

            Future<TransactionReceipt> transactionReceiptFuture = transfer.sendFundsAsync(
                    BOB.getAddress(), BigDecimal.valueOf(1.0), Convert.Unit.ETHER,
                    gasPrice, Transfer.GAS_LIMIT);
            transactionReceipts.add(transactionReceiptFuture);
        }

        for (int i = 0; i < transfer.getAttempts() && !transactionReceipts.isEmpty(); i++) {
            Thread.sleep(transfer.getSleepDuration());

            for (Iterator<Future<TransactionReceipt>> iterator = transactionReceipts.iterator(); iterator.hasNext(); ) {
                Future<TransactionReceipt> transactionReceiptFuture = iterator.next();

                if (transactionReceiptFuture.isDone()) {
                    TransactionReceipt transactionReceipt = transactionReceiptFuture.get();
                    System.out.println(transactionReceipt.getBlockHash());
                    assertFalse(transactionReceipt.getBlockHash().isEmpty());
                    iterator.remove();
                }
            }
        }

        assertTrue(transactionReceipts.isEmpty());
    }
}
