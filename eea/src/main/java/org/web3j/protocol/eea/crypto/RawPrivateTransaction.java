package org.web3j.protocol.eea.crypto;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.RawTransaction;

/**
 * Transaction class used for signing transactions locally.<br>
 * For the specification, refer to p4 of the <a href="http://gavwood.com/paper.pdf">
 * yellow paper</a>.
 */
public class RawPrivateTransaction extends RawTransaction {

    private final String privateFrom;
    private final List<String> privateFor;
    private final String restriction;

    protected RawPrivateTransaction(final BigInteger nonce, final BigInteger gasPrice,
                                    final BigInteger gasLimit, final String to,
                                    final String data, final String privateFrom,
                                    final List<String> privateFor, final String restriction) {
        super(nonce, gasPrice, gasLimit, to, BigInteger.ZERO, data);
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.restriction = restriction;
    }

    protected RawPrivateTransaction(final RawTransaction rawTransaction, final String privateFrom,
                                    final List<String> privateFor, final String restriction) {
        this(rawTransaction.getNonce(), rawTransaction.getGasPrice(),
                rawTransaction.getGasLimit(), rawTransaction.getTo(),
                rawTransaction.getData(), privateFrom, privateFor, restriction);
    }

    public static RawPrivateTransaction createContractTransaction(
            final BigInteger nonce, final BigInteger gasPrice,
            final  BigInteger gasLimit, final String init,
            final String privateFrom, final List<String> privateFor,
            final String restriction) {

        return new RawPrivateTransaction(nonce, gasPrice, gasLimit, "",init,
                privateFrom, privateFor, restriction);
    }

    public static RawPrivateTransaction createTransaction(
            final BigInteger nonce, final BigInteger gasPrice,
            final BigInteger gasLimit, final String to,
            final String data, final String privateFrom,
            final List<String> privateFor, final String restriction) {

        return new RawPrivateTransaction(nonce, gasPrice, gasLimit, to, data,
                privateFrom, privateFor, restriction);
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public List<String> getPrivateFor() {
        return privateFor;
    }

    public String getRestriction() {
        return restriction;
    }

    RawTransaction asRawTransaction() {
        return RawTransaction.createTransaction(
                getNonce(),
                getGasPrice(),
                getGasLimit(),
                getTo(),
                BigInteger.ZERO,
                getData());
    }
}
