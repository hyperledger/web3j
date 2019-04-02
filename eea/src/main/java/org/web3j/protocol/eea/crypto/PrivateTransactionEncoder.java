package org.web3j.protocol.eea.crypto;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.eea.request.PrivateTransaction;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create signed RLP encoded private transaction.
 */
public class PrivateTransactionEncoder {

    public static byte[] signMessage(PrivateTransaction rawTransaction, Credentials credentials) {
        byte[] encodedTransaction = encode(rawTransaction);
        Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());

        return encode(rawTransaction, signatureData);
    }

    public static byte[] signMessage(
            PrivateTransaction rawTransaction, byte chainId, Credentials credentials) {
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

    public static byte[] encode(PrivateTransaction rawTransaction) {
        return encode(rawTransaction, null);
    }

    public static byte[] encode(PrivateTransaction rawTransaction, byte chainId) {
        Sign.SignatureData signatureData = new Sign.SignatureData(
                chainId, new byte[] {}, new byte[] {});
        return encode(rawTransaction, signatureData);
    }

    private static byte[] encode(PrivateTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> values = asRlpValues(rawTransaction, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    public static List<RlpType> asRlpValues(
            PrivateTransaction privateTransaction, Sign.SignatureData signatureData) {

        List<RlpType> result = new ArrayList<>(TransactionEncoder.asRlpValues(buildRawTransaction(privateTransaction), signatureData));

        result.add(RlpString.create(privateTransaction.getPrivateFrom()));

        result.add(
                new RlpList(
                        privateTransaction.getPrivateFor().stream()
                                .map(RlpString::create).collect(Collectors.toList()
                        )
                )
        );
        result.add(RlpString.create(privateTransaction.getRestriction()));

        return result;
    }

    private static RawTransaction buildRawTransaction(PrivateTransaction privateTransaction) {
        return RawTransaction.createTransaction(
                Numeric.decodeQuantity(privateTransaction.getNonce()),
                Numeric.decodeQuantity(privateTransaction.getGasPrice()),
                Numeric.decodeQuantity(privateTransaction.getGas()),
                privateTransaction.getTo(),
                Numeric.decodeQuantity(privateTransaction.getValue()),
                privateTransaction.getData());
    }
}
