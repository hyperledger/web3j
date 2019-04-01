package org.web3j.protocol.eea.request;

import org.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;
import java.util.List;

/**
 * Eea Private Transaction object.
 *
 * <p>This is almost identical to the Ethereum
 * {@link org.web3j.protocol.core.methods.request.Transaction} with the exception that it provides
 * the privateFrom, PrivateFor and Restriction fields
 */
public class PrivateTransaction extends Transaction {

    private String privateFrom;
    private List<String> privateFor;
    private String restriction;

    public PrivateTransaction(
            String from, BigInteger nonce, BigInteger gasLimit, BigInteger gasPrice, String to,
            BigInteger value, String data, String privateFrom, List<String> privateFor, String restriction) {
        super(from, nonce, gasPrice, gasLimit, to, value, data);
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.restriction = restriction;
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public void setPrivateFrom(final String privateFrom) {
        this.privateFrom = privateFrom;
    }

    public List<String> getPrivateFor() {
        return privateFor;
    }

    public void setPrivateFor(List<String> privateFor) {
        this.privateFor = privateFor;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }
}
