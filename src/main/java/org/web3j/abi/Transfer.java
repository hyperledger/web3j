package org.web3j.abi;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.utils.Async;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;


/**
 * Class for performing Ether transactions on the Ethereum blockchain.
 */
public class Transfer extends ManagedTransaction {

    private static final BigInteger GAS_PRICE = BigInteger.valueOf(50_000_000_000L);
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_000_000);

    private Transfer(Web3j web3j, Credentials credentials) {
        super(web3j, credentials);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Transfer#sendFundsAsync(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     *
     * @return the transaction receipt
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @throws TransactionTimeoutException if the transaction was not mined while waiting
     */
    private TransactionReceipt send(
            String toAddress, BigDecimal value, Convert.Unit unit) throws ExecutionException, InterruptedException,
            TransactionTimeoutException {

        BigDecimal weiValue = Convert.toWei(value, unit);

        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: " + value + " " + unit.toString() +
                            " = " + weiValue + " Wei");
        }

        BigInteger nonce = getNonce(credentials.getAddress());

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                GAS_PRICE,
                GAS_LIMIT,
                toAddress,
                weiValue.toBigIntegerExact());

        return signAndSend(rawTransaction);
    }

    /**
     * Execute the provided function as a transaction asynchronously. This is intended for one-off
     * fund transfers. For multiple, create an insta
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     *
     * @return {@link Future} containing executing transaction
     */
    public Future<TransactionReceipt> sendFundsAsync(
            final String toAddress, final BigDecimal value, final Convert.Unit unit) {

        return Async.run(new Callable<TransactionReceipt>() {
            @Override
            public TransactionReceipt call() throws Exception {
                return send(toAddress, value, unit);
            }
        });
    }

    public static TransactionReceipt sendFunds(
            Web3j web3j, Credentials credentials,
            String toAddress, BigDecimal value, Convert.Unit unit) throws InterruptedException,
            ExecutionException, TransactionTimeoutException {

        return new Transfer(web3j, credentials).send(toAddress, value, unit);
    }

    public static TransactionReceipt sendFundsAsync(
            Web3j web3j, Credentials credentials,
            String toAddress, BigDecimal value, Convert.Unit unit) throws InterruptedException,
            ExecutionException, TransactionTimeoutException {

        return new Transfer(web3j, credentials).send(toAddress, value, unit);
    }
}
