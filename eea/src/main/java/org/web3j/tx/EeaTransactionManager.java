package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.eea.crypto.PrivateTransactionEncoder;
import org.web3j.protocol.eea.crypto.RawPrivateTransaction;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

/**
 * PrivateTransactionManager implementation for using a Quorum node to transact.
 */
public class EeaTransactionManager extends PrivateTransactionManager {

    private static final Logger log = LoggerFactory.getLogger(EeaTransactionManager.class);

    private static final int SLEEP_DURATION = 1000;
    private static final int ATTEMPTS = 20;

    private final Eea eea;
    private final Credentials credentials;
    private final long chainId;
    private final String privateFrom;
    private List<String> privateFor;

    public EeaTransactionManager(
            final Eea eea,
            final Credentials credentials,
            final long chainId,
            final String privateFrom,
            final List<String> privateFor,
            final int attempts,
            final int sleepDuration) {
        super(eea, privateFrom, attempts, sleepDuration, credentials.getAddress());
        this.eea = eea;
        this.credentials = credentials;
        this.chainId = chainId;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
    }

    public EeaTransactionManager(
            final Eea eea, final Credentials credentials, final long chainId,
            final String privateFrom, final List<String> privateFor) {
        this(eea, credentials, chainId, privateFrom, privateFor, ATTEMPTS, SLEEP_DURATION);
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public List<String> getPrivateFor() {
        return privateFor;
    }

    public void setPrivateFor(final List<String> privateFor) {
        this.privateFor = privateFor;
    }

    @Override
    public EthSendTransaction sendTransaction(
            final BigInteger gasPrice, final BigInteger gasLimit, final String to,
            final String data, final BigInteger value)
            throws IOException {

        final BigInteger nonce = eea.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .send().getTransactionCount();

        final RawPrivateTransaction transaction = RawPrivateTransaction.createTransaction(
                nonce, gasPrice, gasLimit, to,
                data, privateFrom, privateFor, "restricted");

        final String rawSignedTransaction =
                Numeric.toHexString(
                        PrivateTransactionEncoder.signMessage(transaction, chainId, credentials));

        return eea.eeaSendRawTransaction(rawSignedTransaction).send();
    }

    @Override
    public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        try {
            return executeCall(to, data);
        } catch (TransactionException e) {
            log.error("Failed to execute call", e);
            return null;
        }
    }
}
