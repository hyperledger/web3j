package org.web3j.tx;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.utils.Async;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;


/**
 * Class for performing Ether transactions on the Ethereum blockchain.
 */
public class Transfer extends ManagedTransaction {

    // This is the cost to send Ether between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);

    public Transfer(Web3j web3j, TransactionManager transactionManager) {
        super(web3j, transactionManager);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Transfer#sendFundsAsync(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     *
     * @return {@link Optional} containing our transaction receipt
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @throws TransactionTimeoutException if the transaction was not mined while waiting
     */
    private TransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit)
            throws ExecutionException, InterruptedException,
            TransactionTimeoutException {

        BigInteger gasPrice = getGasPrice();
        return send(toAddress, value, unit, gasPrice, GAS_LIMIT);
    }

    private TransactionReceipt send(
            String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice,
            BigInteger gasLimit) throws ExecutionException, InterruptedException,
            TransactionTimeoutException {

        BigDecimal weiValue = Convert.toWei(value, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: " + value + " " + unit.toString() +
                            " = " + weiValue + " Wei");
        }

        return send(toAddress, "", weiValue.toBigIntegerExact(), gasPrice, gasLimit);
    }

    /**
     * Execute the provided function as a transaction asynchronously. This is intended for one-off
     * fund transfers. For multiple, create an instance.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     *
     * @return {@link Future} containing executing transaction
     */
    public Future<TransactionReceipt> sendFundsAsync(
            String toAddress, BigDecimal value, Convert.Unit unit) {
        return Async.run(() -> send(toAddress, value, unit));
    }

    public Future<TransactionReceipt> sendFundsAsync(
            String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice,
            BigInteger gasLimit) {
        return Async.run(() -> send(toAddress, value, unit, gasPrice, gasLimit));
    }

    public static TransactionReceipt sendFunds(
            Web3j web3j, Credentials credentials,
            String toAddress, BigDecimal value, Convert.Unit unit) throws InterruptedException,
            ExecutionException, TransactionTimeoutException {

        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

        return new Transfer(web3j, transactionManager).send(toAddress, value, unit);
    }

    public static Future<TransactionReceipt> sendFundsAsync(
            Web3j web3j, Credentials credentials,
            String toAddress, BigDecimal value, Convert.Unit unit) throws InterruptedException,
            ExecutionException, TransactionTimeoutException {

        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

        return new Transfer(web3j, transactionManager)
                .sendFundsAsync(toAddress, value, unit);
    }
}
