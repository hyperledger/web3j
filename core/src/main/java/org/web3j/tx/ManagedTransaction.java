package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;


/**
 * Generic transaction manager.
 */
public abstract class ManagedTransaction {

    public static final BigInteger GAS_PRICE = BigInteger.valueOf(22000000000L);

    protected Web3j web3j;

    protected TransactionManager transactionManager;

    protected EnsResolver ensResolver;

    protected ManagedTransaction(Web3j web3j, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.web3j = web3j;
        this.ensResolver = new EnsResolver(web3j);
    }

    /**
     * This should only be used in case you need to get the {@link EnsResolver#syncThreshold}
     * parameter, which dictates the threshold in milliseconds since the last processed block
     * timestamp should be to considered in sync the blockchain.
     *
     * <p>It is currently experimental and only used in ENS name resolution, but will probably
     * be made available for read calls in the future.
     *
     * @return sync threshold value in milliseconds
     */
    public long getSyncThreshold() {
        return ensResolver.getSyncThreshold();
    }

    /**
     * This should only be used in case you need to modify the {@link EnsResolver#syncThreshold}
     * parameter, which dictates the threshold in milliseconds since the last processed block
     * timestamp should be to considered in sync the blockchain.
     *
     * <p>It is currently experimental and only used in ENS name resolution, but will probably
     * be made available for read calls in the future.
     *
     * @param syncThreshold the sync threshold in milliseconds
     */
    public void setSyncThreshold(long syncThreshold) {
        ensResolver.setSyncThreshold(syncThreshold);
    }

    /**
     * Return the current gas price from the ethereum node.
     * <p>
     *     Note: this method was previously called {@code getGasPrice} but was renamed to
     *     distinguish it when a bean accessor method on {@link Contract} was added with that name.
     *     If you have a Contract subclass that is calling this method (unlikely since those
     *     classes are usually generated and until very recently those generated subclasses were
     *     marked {@code final}), then you will need to change your code to call this method
     *     instead, if you want the dynamic behavior.
     * </p>
     * @return the current gas price, determined dynamically at invocation
     * @throws IOException if there's a problem communicating with the ethereum node
     */
    public BigInteger requestCurrentGasPrice() throws IOException {
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();

        return ethGasPrice.getGasPrice();
    }

    protected TransactionReceipt send(
            String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit)
            throws IOException, TransactionException {

        return transactionManager.executeTransaction(
                gasPrice, gasLimit, to, data, value);
    }
}
