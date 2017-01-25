package org.web3j.tx;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.exceptions.TransactionTimeoutException;
import org.web3j.tx.TransactionManager;

/**
 * <p>TransactionManager implementation for using an Ethereum node to transact.</p>
 *
 * <p><b>Note</b>: accounts must be unlocked on the node for transactions to be successful.</p>
 */
public class ClientTransactionManager implements TransactionManager {

    private final Web3j web3j;
    private final String fromAddress;

    public ClientTransactionManager(
            Web3j web3j, String fromAddress) {
        this.web3j = web3j;
        this.fromAddress = fromAddress;
    }

    @Override
    public EthSendTransaction executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws ExecutionException, InterruptedException, TransactionTimeoutException {

        Transaction transaction = new Transaction(
                fromAddress, null, gasPrice, gasLimit, to, value, data);

        return web3j.ethSendTransaction(transaction)
                .sendAsync().get();
    }
}
