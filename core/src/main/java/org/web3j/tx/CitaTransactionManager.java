package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

public class CitaTransactionManager extends TransactionManager {

    private final Web3j web3j;
    final Credentials credentials;

    private long currentHeight;

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

        Transaction transaction = new Transaction(to, nonce, quota.longValue(), this.currentHeight + 80, data);

        return web3j.ethSendRawTransaction(transaction.sign(credentials)).send();
    }

    public void setCurrentHeight(long validUntilBlock) {
        this.currentHeight = validUntilBlock;
    }

    @Override
    public String getFromAddress() {
        return credentials.getAddress();
    }
}
