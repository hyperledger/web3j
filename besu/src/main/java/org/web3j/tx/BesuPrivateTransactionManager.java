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
package org.web3j.tx;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.besu.Besu;
import org.web3j.tx.gas.BesuPrivacyGasProvider;
import org.web3j.utils.Base64String;

/** PrivateTransactionManager implementation for using a Besu node to transact. */
public class BesuPrivateTransactionManager extends PrivateTransactionManager {

    private final Base64String privacyGroupId;

    public BesuPrivateTransactionManager(
            final Besu besu,
            final BesuPrivacyGasProvider gasProvider,
            final Credentials credentials,
            final long chainId,
            final Base64String privateFrom,
            final Base64String privacyGroupId,
            final int attempts,
            final int sleepDuration) {
        super(besu, gasProvider, credentials, chainId, privateFrom, attempts, sleepDuration);
        this.privacyGroupId = privacyGroupId;
    }

    public BesuPrivateTransactionManager(
            final Besu besu,
            final BesuPrivacyGasProvider gasProvider,
            final Credentials credentials,
            final long chainId,
            final Base64String privateFrom,
            final Base64String privacyGroupId) {
        this(
                besu,
                gasProvider,
                credentials,
                chainId,
                privateFrom,
                privacyGroupId,
                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH,
                15 * 1000);
    }

    @Override
    public Base64String getPrivacyGroupId() {
        return privacyGroupId;
    }

    @Override
    protected Object privacyGroupIdOrPrivateFor() {
        return privacyGroupId;
    }
}
