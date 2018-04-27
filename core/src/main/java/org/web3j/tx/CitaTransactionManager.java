package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

public class CitaTransactionManager extends TransactionManager {

    private final Web3j web3j;
    final Credentials credentials;

    public CitaTransactionManager(Web3j web3j, Credentials credentials) {
        super(web3j, credentials.getAddress());
        this.web3j = web3j;
        this.credentials = credentials;

    }

    public CitaTransactionManager(
            Web3j web3j, Credentials credentials, int attempts, int sleepDuration) {
        super(web3j, attempts, sleepDuration, credentials.getAddress());
        this.web3j = web3j;
        this.credentials = credentials;
    }

    BigInteger getNonce() throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        return ethGetTransactionCount.getTransactionCount();
    }

    @Override
    public EthSendTransaction sendTransaction(
            BigInteger quota, BigInteger nonce, String to,
            String data, BigInteger value) throws IOException {
        return new EthSendTransaction();
    }

    // adapt to cita
    @Override
    public EthSendTransaction sendTransaction(
         String to, String data, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, BigInteger version, BigInteger chainId) throws IOException {
        Transaction transaction = new Transaction(to, nonce, quota.longValue(), validUntilBlock.longValue(), version.intValue(), chainId.intValue(), data);
        return web3j.ethSendRawTransaction(transaction.sign(credentials)).send();
    }

    // adapt to cita
    public CompletableFuture<EthSendTransaction> sendTransactionAsync(
         String to, String data, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, BigInteger version, int chainId) throws IOException {
        Transaction transaction = new Transaction(to, nonce, quota.longValue(), validUntilBlock.longValue(), version.intValue(), chainId, data);
        return web3j.ethSendRawTransaction(transaction.sign(credentials)).sendAsync();
    }

    @Override
    public String getFromAddress() {
        return credentials.getAddress();
    }
}
