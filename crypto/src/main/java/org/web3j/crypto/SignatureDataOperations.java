package org.web3j.crypto;

import java.math.BigInteger;
import java.security.SignatureException;

import org.web3j.utils.Numeric;

public interface SignatureDataOperations {

    int CHAIN_ID_INC = 35;
    int LOWER_REAL_V = 27;

    Sign.SignatureData getSignatureData();

    byte[] getEncodedTransaction(Integer chainId);

    default String getFrom() throws SignatureException {
        byte[] encodedTransaction = getEncodedTransaction(getChainId());
        BigInteger v = Numeric.toBigInt(getSignatureData().getV());
        byte[] r = getSignatureData().getR();
        byte[] s = getSignatureData().getS();
        Sign.SignatureData signatureDataV = new Sign.SignatureData(getRealV(v), r, s);
        BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureDataV);
        return "0x" + Keys.getAddress(key);
    }

    default void verify(String from) throws SignatureException {
        String actualFrom = getFrom();
        if (!actualFrom.equals(from)) {
            throw new SignatureException("from mismatch");
        }
    }

    default byte getRealV(BigInteger bv) {
        long v = bv.longValue();
        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return (byte) v;
        }
        byte realV = LOWER_REAL_V;
        int inc = 0;
        if ((int) v % 2 == 0) {
            inc = 1;
        }
        return (byte) (realV + inc);
    }

    default Integer getChainId() {
        BigInteger bv = Numeric.toBigInt(getSignatureData().getV());
        long v = bv.longValue();
        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return null;
        }
        Integer chainId = (int)((v - CHAIN_ID_INC) / 2);
        return chainId;
    }
}
