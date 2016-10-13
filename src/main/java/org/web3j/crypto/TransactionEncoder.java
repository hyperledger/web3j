package org.web3j.crypto;

import java.util.ArrayList;
import java.util.List;

import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

/**
 * Create RLP encoded transaction, implementation as per p4 of the
 * <a href="http://gavwood.com/paper.pdf">yellow paper</a>.
 */
public class TransactionEncoder {

    public static byte[] signMessage(Transaction transaction, ECKeyPair keyPair) {
        byte[] encodedTransaction = encode(transaction);
        Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, keyPair);

        return encode(transaction, signatureData);
    }

    public static byte[] encode(Transaction transaction) {
        return encode(transaction, null);
    }

    private static byte[] encode(Transaction transaction, Sign.SignatureData signatureData) {
        List<RlpType> values = asRlpValues(transaction, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    static List<RlpType> asRlpValues(
            Transaction transaction, Sign.SignatureData signatureData) {
        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(transaction.getNonce()));
        result.add(RlpString.create(transaction.getGasPrice()));
        result.add(RlpString.create(transaction.getGasLimit()));

        // an empty to address (contract creation) should not be encoded as a numeric 0 value
        String to = transaction.getTo();
        if (to.length() > 0) {
            result.add(RlpString.create(Numeric.toBigInt(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(transaction.getValue()));
        result.add(RlpString.create(transaction.getData()));

        if (signatureData != null) {
            result.add(RlpString.create(signatureData.getV()));
            result.add(RlpString.create(signatureData.getR()));
            result.add(RlpString.create(signatureData.getS()));
        }

        return result;
    }
}
