package org.web3j.protocol.eea.crypto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;

/**
 * Create signed RLP encoded private transaction.
 */
public class PrivateTransactionEncoder {

    public static byte[] signMessage(
            final RawPrivateTransaction rawTransaction, final Credentials credentials) {
        final byte[] encodedTransaction = encode(rawTransaction);
        final Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());

        return encode(rawTransaction, signatureData);
    }

    public static byte[] signMessage(
            final RawPrivateTransaction rawTransaction,
            final byte chainId, final Credentials credentials) {
        final byte[] encodedTransaction = encode(rawTransaction, chainId);
        final Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());

        final Sign.SignatureData eip155SignatureData =
                createEip155SignatureData(signatureData, chainId);
        return encode(rawTransaction, eip155SignatureData);
    }

    public static Sign.SignatureData createEip155SignatureData(
            final Sign.SignatureData signatureData, final byte chainId) {
        final byte v = (byte) (signatureData.getV() + (chainId << 1) + 8);

        return new Sign.SignatureData(
                v, signatureData.getR(), signatureData.getS());
    }

    public static byte[] encode(final RawPrivateTransaction rawTransaction) {
        return encode(rawTransaction, null);
    }

    public static byte[] encode(final RawPrivateTransaction rawTransaction,
                                final byte chainId) {
        final Sign.SignatureData signatureData = new Sign.SignatureData(
                chainId, new byte[] {}, new byte[] {});
        return encode(rawTransaction, signatureData);
    }

    private static byte[] encode(final RawPrivateTransaction rawTransaction,
                                 final Sign.SignatureData signatureData) {
        final List<RlpType> values = asRlpValues(rawTransaction, signatureData);
        final RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    public static List<RlpType> asRlpValues(
            final RawPrivateTransaction privateTransaction,
            final Sign.SignatureData signatureData) {

        final List<RlpType> result = new ArrayList<>(
                TransactionEncoder.asRlpValues(
                        privateTransaction.asRawTransaction(), signatureData));

        result.add(RlpString.create(privateTransaction.getPrivateFrom()));

        result.add(
                new RlpList(
                        privateTransaction.getPrivateFor().stream()
                                .map(RlpString::create)
                                .collect(Collectors.toList())
                )
        );

        result.add(RlpString.create(privateTransaction.getRestriction()));

        return result;
    }
}
