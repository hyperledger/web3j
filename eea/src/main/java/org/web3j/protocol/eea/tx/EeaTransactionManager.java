package org.web3j.protocol.eea.tx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.TransactionUtils;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.eea.Eea;
import org.web3j.protocol.eea.crypto.PrivateTransactionEncoder;
import org.web3j.protocol.eea.request.PrivateTransaction;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Numeric;

/**
 * TransactionManager implementation for using a Quorum node to transact.
 */
public class EeaTransactionManager extends TransactionManager {

    private static final int SLEEP_DURATION = 1000;
    private static final int ATTEMPTS = 20;

    private final Eea eea;
    private final Credentials credentials;
    private final String privateFrom;
    private List<String> privateFor;

    public EeaTransactionManager(
            Eea eea,
            Credentials credentials,
            String privateFrom,
            List<String> privateFor,
            int attempts,
            int sleepDuration) {
        super(eea, attempts, sleepDuration, credentials.getAddress());
        this.eea = eea;
        this.credentials = credentials;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
    }

    public EeaTransactionManager(
            Eea eea, Credentials credentials, String privateFrom, List<String> privateFor) {
        this(eea, credentials, privateFrom, privateFor, ATTEMPTS, SLEEP_DURATION);
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public List<String> getPrivateFor() {
        return privateFor;
    }

    public void setPrivateFor(List<String> privateFor) {
        this.privateFor = privateFor;
    }

    @Override
    public EthSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException {

        PrivateTransaction transaction = new PrivateTransaction(
                credentials.getAddress(), null, gasLimit, BigInteger.ZERO, to,
                BigInteger.ZERO, data, privateFrom, privateFor, "restricted");

        String rawSignedTransaction = Numeric.toHexString(PrivateTransactionEncoder.signMessage(transaction, credentials));

        return eea.eeaSendRawTransaction(rawSignedTransaction).send();
    }
}
