package org.web3j.crypto;

import java.util.ArrayList;
import java.util.List;

import org.web3j.protocol.core.methods.request.RawTransaction;
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
        byte v = (byte) (signatureData.getV() + (chainId << 1) + 8);

        return new Sign.SignatureData(
                v, signatureData.getR(), signatureData.getS());
    }

    public static byte[] encode(RawTransaction rawTransaction) {
        return encode(rawTransaction, null);
    }

    public static byte[] encode(RawTransaction rawTransaction, byte chainId) {
        Sign.SignatureData signatureData = new Sign.SignatureData(
                chainId, new byte[] {}, new byte[] {});
        return encode(rawTransaction, signatureData);
    }

    private static byte[] encode(RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> values = asRlpValues(rawTransaction, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    static List<RlpType> asRlpValues(
            RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> result = new ArrayList<RlpType>();

        result.add(RlpString.create(rawTransaction.getNonce()));
        result.add(RlpString.create(rawTransaction.getGasPrice()));
        result.add(RlpString.create(rawTransaction.getGasLimit()));

        // an empty to address (contract creation) should not be encoded as a numeric 0 value
        String to = rawTransaction.getTo();
        if (to != null && to.length() > 0) {
            result.add(RlpString.create(Numeric.toBigInt(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(rawTransaction.getValue()));

        // value field will already be hex encoded, so we need to convert into binary first
        byte[] data = Numeric.hexStringToByteArray(rawTransaction.getData());
        result.add(RlpString.create(data));

        if (signatureData != null) {
            result.add(RlpString.create(signatureData.getV()));
            result.add(RlpString.create(signatureData.getR()));
            result.add(RlpString.create(signatureData.getS()));
        }

        return result;
    }
}
