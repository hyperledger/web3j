package org.web3j.crypto;

import java.math.BigInteger;
import java.security.SignatureException;

public class SignedRawTransaction extends RawTransaction {

    private Sign.SignatureData signatureData;

    public SignedRawTransaction(BigInteger nonce, BigInteger gasPrice,
            BigInteger gasLimit, String to, BigInteger value, String data,
            Sign.SignatureData signatureData) {
        super(nonce, gasPrice, gasLimit, to, value, data);
        this.signatureData = signatureData;
    }

    public Sign.SignatureData getSignatureData() {
        return signatureData;
    }

    public String getFrom() throws SignatureException {
        byte[] encodedTransaction = TransactionEncoder.encode(this);
        BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureData);
        return "0x" + Keys.getAddress(key);
    }

    public void verify(String from) throws SignatureException {
        String actualFrom = getFrom();
        if (!actualFrom.equals(from)) {
            throw new SignatureException("from mismatch");
        }
    }
}
