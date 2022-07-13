/*
 * Copyright 2022 Web3 Labs Ltd.
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
package org.web3j.protocol.eea.crypto;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.service.TxSignService;
import org.web3j.tx.ChainId;

/** Service to base sign transaction. */
public class PrivateTxSignServiceImpl implements TxSignService {

    private final Credentials credentials;

    public PrivateTxSignServiceImpl(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public byte[] sign(RawTransaction rawTransaction, long chainId) {
        if (!(rawTransaction instanceof RawPrivateTransaction)) {
            throw new RuntimeException("Can only sign RawPrivateTransaction");
        }

        final byte[] signedMessage;

        if (chainId > ChainId.NONE) {
            signedMessage =
                    PrivateTransactionEncoder.signMessage(
                            (RawPrivateTransaction) rawTransaction, chainId, credentials);
        } else {
            signedMessage =
                    PrivateTransactionEncoder.signMessage(
                            (RawPrivateTransaction) rawTransaction, credentials);
        }
        return signedMessage;
    }

    @Override
    public String getAddress() {
        return credentials.getAddress();
    }
}
