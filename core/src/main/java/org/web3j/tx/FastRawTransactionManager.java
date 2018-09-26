package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.response.Callback;
import org.web3j.tx.response.QueuingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

/**
 * Simple RawTransactionManager derivative that manages nonces to facilitate multiple transactions
 * per block.
 */
public class FastRawTransactionManager extends RawTransactionManager {

    private volatile BigInteger nonce = BigInteger.valueOf(-1);

    public FastRawTransactionManager(Web3j web3j, Credentials credentials, byte chainId) {
        super(web3j, credentials, chainId);
    }

    public FastRawTransactionManager(Web3j web3j, Credentials credentials) {
        super(web3j, credentials);
    }

    public FastRawTransactionManager(
            Web3j web3j, Credentials credentials,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(web3j, credentials, ChainId.NONE, transactionReceiptProcessor);
    }

    public FastRawTransactionManager(
            Web3j web3j, Credentials credentials, byte chainId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(web3j, credentials, chainId, transactionReceiptProcessor);
    }

    @Override
    protected synchronized BigInteger getNonce() throws IOException {
        if (nonce.signum() == -1) {
            // obtain lock
            nonce = super.getNonce();
        } else {
            nonce = nonce.add(BigInteger.ONE);
        }
        return nonce;
    }

    public BigInteger getCurrentNonce() {
        return nonce;
    }

    public synchronized void resetNonce() throws IOException {
        nonce = super.getNonce();
    }

    public synchronized void setNonce(BigInteger value) {
        nonce = value;
    }
}
