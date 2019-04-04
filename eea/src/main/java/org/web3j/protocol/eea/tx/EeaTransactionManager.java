package org.web3j.protocol.eea.tx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.eea.crypto.PrivateTransactionEncoder;
import org.web3j.protocol.eea.crypto.RawPrivateTransaction;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Numeric;

/**
 * PrivateTransactionManager implementation for using a Quorum node to transact.
 */
public class EeaTransactionManager extends TransactionManager {

    private static final int SLEEP_DURATION = 1000;
    private static final int ATTEMPTS = 20;

    private final Eea eea;
    private final Credentials credentials;
    private final String privateFrom;
    private List<String> privateFor;

    public EeaTransactionManager(
            final Eea eea,
            final Credentials credentials,
            final String privateFrom,
            final List<String> privateFor,
            final int attempts,
            final int sleepDuration) {
        super(eea, attempts, sleepDuration, credentials.getAddress());
        this.eea = eea;
        this.credentials = credentials;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
    }

    public EeaTransactionManager(
            final Eea eea, final Credentials credentials,
            final String privateFrom, final List<String> privateFor) {
        this(eea, credentials, privateFrom, privateFor, ATTEMPTS, SLEEP_DURATION);
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

        final RawPrivateTransaction transaction = RawPrivateTransaction.createTransaction(
                null, gasLimit, BigInteger.ZERO, to,
                data, privateFrom, privateFor, "restricted");

        final String rawSignedTransaction =
                Numeric.toHexString(
                        PrivateTransactionEncoder.signMessage(transaction, credentials));

        return eea.eeaSendRawTransaction(rawSignedTransaction).send();
    }
}
