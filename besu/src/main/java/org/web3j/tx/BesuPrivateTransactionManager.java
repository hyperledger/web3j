/*
 * Copyright 2021 Web3 Labs Ltd.
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
package org.web3j.tx;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Base64String;
import org.web3j.utils.Restriction;

@Deprecated
public class BesuPrivateTransactionManager extends PrivateTransactionManager {

    /**
     * Creates a LegacyPrivateTransactionManager
     *
     * @deprecated
     *     <p>Use {@link PrivateTransactionManager} instead.
     * @param besu
     * @param credentials
     * @param transactionReceiptProcessor
     * @param chainId
     * @param privateFrom
     * @param privacyGroupId
     * @param restriction
     */
    public BesuPrivateTransactionManager(
            Besu besu,
            Credentials credentials,
            TransactionReceiptProcessor transactionReceiptProcessor,
            long chainId,
            Base64String privateFrom,
            Base64String privacyGroupId,
            Restriction restriction) {
        super(
                besu,
                credentials,
                transactionReceiptProcessor,
                chainId,
                privateFrom,
                privacyGroupId,
                restriction);
    }
}
