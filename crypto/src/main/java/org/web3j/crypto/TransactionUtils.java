/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.crypto;

import org.web3j.utils.Numeric;

/** Transaction utility functions. */
public class TransactionUtils {

    /**
     * Utility method to provide the transaction hash for a given transaction.
     *
     * @param rawTransaction we wish to send
     * @param credentials of the sender
     * @return encoded transaction hash
     */
    public static byte[] generateTransactionHash(
            final RawTransaction rawTransaction, final Credentials credentials) {
        final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
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
            final RawTransaction rawTransaction,
            final byte chainId,
            final Credentials credentials) {
        final byte[] signedMessage =
                TransactionEncoder.signMessage(rawTransaction, (long) chainId, credentials);
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
            final RawTransaction rawTransaction, final Credentials credentials) {
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
            final RawTransaction rawTransaction,
            final byte chainId,
            final Credentials credentials) {
        return Numeric.toHexString(generateTransactionHash(rawTransaction, chainId, credentials));
    }
}
