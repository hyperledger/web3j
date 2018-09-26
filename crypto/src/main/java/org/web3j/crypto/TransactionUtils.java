package org.web3j.crypto;

import org.web3j.utils.Numeric;

/**
 * Transaction utility functions.
 */
public class TransactionUtils {

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param credentials of the sender
     * @return encoded transaction hash
     */
    public static byte[] generateTransactionHash(
            RawTransaction rawTransaction, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Hash.sha3(signedMessage);
    }

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param chainId of the intended chain
     * @param credentials of the sender
     * @return encoded transaction hash
     */
    public static byte[] generateTransactionHash(
            RawTransaction rawTransaction, byte chainId, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        return Hash.sha3(signedMessage);
    }

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param credentials of the sender
     * @return transaction hash as a hex encoded string
     */
    public static String generateTransactionHashHexEncoded(
            RawTransaction rawTransaction, Credentials credentials) {
        return Numeric.toHexString(generateTransactionHash(rawTransaction, credentials));
    }

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param chainId of the intended chain
     * @param credentials of the sender
     * @return transaction hash as a hex encoded string
     */
    public static String generateTransactionHashHexEncoded(
            RawTransaction rawTransaction, byte chainId, Credentials credentials) {
        return Numeric.toHexString(generateTransactionHash(rawTransaction, chainId, credentials));
    }
}
