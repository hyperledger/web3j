package org.web3j.crypto;

import java.math.BigInteger;

import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

public class TransactionDecoder {

    public static RawTransaction decode(String hexTransaction) {
        byte[] transaction = Numeric.hexStringToByteArray(hexTransaction);
        RlpList rlpList = RlpDecoder.decode(transaction);
        RlpList values = (RlpList) rlpList.getValues().get(0);
        BigInteger nonce = ((RlpString) values.getValues().get(0)).asPositiveBigInteger();
        BigInteger gasPrice = ((RlpString) values.getValues().get(1)).asPositiveBigInteger();
        BigInteger gasLimit = ((RlpString) values.getValues().get(2)).asPositiveBigInteger();
        String to = ((RlpString) values.getValues().get(3)).asString();
        BigInteger value = ((RlpString) values.getValues().get(4)).asPositiveBigInteger();
        String data = ((RlpString) values.getValues().get(5)).asString();
        if (values.getValues().size() > 6  && values.getValues().get(7) instanceof RlpString) {
            byte[] v = ((RlpString) values.getValues().get(6)).getBytes();
            byte[] r = Numeric.toBytesPadded(
                Numeric.toBigInt(((RlpString) values.getValues().get(7)).getBytes()), 32);
            byte[] s = Numeric.toBytesPadded(
                Numeric.toBigInt(((RlpString) values.getValues().get(8)).getBytes()), 32);
            Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
            return new SignedRawTransaction(nonce, gasPrice, gasLimit,
                to, value, data, signatureData);
        } else {
            return RawTransaction.createTransaction(nonce,
                gasPrice, gasLimit, to, value, data);
        }
    }
    
}
