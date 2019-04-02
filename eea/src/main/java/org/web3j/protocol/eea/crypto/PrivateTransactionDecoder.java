package org.web3j.protocol.eea.crypto;

import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.crypto.TransactionDecoder;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.util.List;
import java.util.stream.Collectors;

public class PrivateTransactionDecoder {

    public static RawPrivateTransaction decode(final String hexTransaction) {
        final byte[] transaction = Numeric.hexStringToByteArray(hexTransaction);
        final RlpList rlpList = RlpDecoder.decode(transaction);
        final RlpList values = (RlpList) rlpList.getValues().get(0);

        final RawTransaction rawTransaction = TransactionDecoder.decode(hexTransaction);

        if (values.getValues().size() > 9) {
            return new SignedRawPrivateTransaction((SignedRawTransaction) rawTransaction,
                    extractString(values, 9),  extractStringList(values, 10),
                    extractString(values, 11));
        } else {
            return new RawPrivateTransaction(rawTransaction, extractString(values, 6),
                    extractStringList(values, 7),  extractString(values, 8));
        }
    }

    private static String extractString(final RlpList values, int i) {
        return new String(((RlpString) values.getValues().get(i)).getBytes());
    }

    private static List<String> extractStringList(final RlpList values, int i) {
        return ((RlpList) values.getValues().get(i)).getValues().stream()
                .map(rlp -> new String(((RlpString) rlp).getBytes()))
                .collect(Collectors.toList());
    }

}
