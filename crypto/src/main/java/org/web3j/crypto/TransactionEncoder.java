package org.web3j.crypto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

/**
 * Create RLP encoded transaction, implementation as per p4 of the
 * <a href="http://gavwood.com/paper.pdf">yellow paper</a>.
 */
public class TransactionEncoder {

    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;

    public static byte[] signMessage(RawTransaction rawTransaction, Credentials credentials) {
        byte[] encodedTransaction = encode(rawTransaction);
        Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());

        return encode(rawTransaction, signatureData);
    }

    public static byte[] signMessage(
            RawTransaction rawTransaction, byte chainId, Credentials credentials) {
        byte[] encodedTransaction = encode(rawTransaction, chainId);
        Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());

        Sign.SignatureData eip155SignatureData = createEip155SignatureData(signatureData, chainId);
        return encode(rawTransaction, eip155SignatureData);
    }

    public static Sign.SignatureData createEip155SignatureData(
            Sign.SignatureData signatureData, byte chainId) {
        BigInteger v = Numeric.toBigInt(signatureData.getV());
        v = v.subtract(BigInteger.valueOf(LOWER_REAL_V));
        v = v.add(BigInteger.valueOf(chainId * 2));
        v = v.add(BigInteger.valueOf(CHAIN_ID_INC));

        return new Sign.SignatureData(
                v.toByteArray(), signatureData.getR(), signatureData.getS());
    }

    public static byte[] encode(RawTransaction rawTransaction) {
        return encode(rawTransaction, null);
    }

    public static byte[] encode(RawTransaction rawTransaction, byte chainId) {
        Sign.SignatureData signatureData = new Sign.SignatureData(
                new byte[]{chainId}, new byte[] {}, new byte[] {});
        return encode(rawTransaction, signatureData);
    }

    private static byte[] encode(RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> values = asRlpValues(rawTransaction, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    static List<RlpType> asRlpValues(
            RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(rawTransaction.getNonce()));
        result.add(RlpString.create(rawTransaction.getGasPrice()));
        result.add(RlpString.create(rawTransaction.getGasLimit()));

        // an empty to address (contract creation) should not be encoded as a numeric 0 value
        String to = rawTransaction.getTo();
        if (to != null && to.length() > 0) {
            // addresses that start with zeros should be encoded with the zeros included, not
            // as numeric values
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(rawTransaction.getValue()));

        // value field will already be hex encoded, so we need to convert into binary first
        byte[] data = Numeric.hexStringToByteArray(rawTransaction.getData());
        result.add(RlpString.create(data));

        if (signatureData != null) {
            result.add(RlpString.create(signatureData.getV()));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        return result;
    }
}
