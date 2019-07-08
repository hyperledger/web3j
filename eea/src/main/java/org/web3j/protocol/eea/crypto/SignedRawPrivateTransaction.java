package org.web3j.protocol.eea.crypto;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Sign;
import org.web3j.crypto.SignatureDataOperations;
import org.web3j.crypto.SignedRawTransaction;

public class SignedRawPrivateTransaction
        extends RawPrivateTransaction
        implements SignatureDataOperations {

    private final Sign.SignatureData signatureData;

    public SignedRawPrivateTransaction(final BigInteger nonce, final BigInteger gasPrice,
                                       final BigInteger gasLimit, final String to,
                                       final String data, final String privateFrom,
                                       final List<String> privateFor, final String restriction,
                                       final Sign.SignatureData signatureData) {
        super(nonce, gasPrice, gasLimit, to, data, privateFrom, privateFor, restriction);
        this.signatureData = signatureData;
    }

    public SignedRawPrivateTransaction(final SignedRawTransaction signedRawTransaction,
                                       final String privateFrom, final List<String> privateFor,
                                       final String restriction) {
        this(signedRawTransaction.getNonce(), signedRawTransaction.getGasPrice(),
                signedRawTransaction.getGasLimit(), signedRawTransaction.getTo(),
                signedRawTransaction.getData(), privateFrom, privateFor, restriction,
                signedRawTransaction.getSignatureData());
    }

    public Sign.SignatureData getSignatureData() {
        return signatureData;
    }

    @Override
    public byte[] getEncodedTransaction(Integer chainId) {
        if (null == chainId) {
            return PrivateTransactionEncoder.encode(this);
        } else {
            return PrivateTransactionEncoder.encode(this, chainId.byteValue());
        }
    }
}
