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
package org.web3j.service;

import org.web3j.crypto.HSMPass;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.transaction.type.TransactionType;
import org.web3j.tx.ChainId;

import static org.web3j.crypto.TransactionEncoder.createEip155SignatureData;
import static org.web3j.crypto.TransactionEncoder.encode;

public class TxHSMSignService implements TxSignService {

    private final HSMPass hsmPass;
    private final HSMRequestProcessor<HSMPass> hsmRequestProcessor;

    public TxHSMSignService(HSMRequestProcessor<HSMPass> hsmRequestProcessor, HSMPass hsmPass) {
        this.hsmPass = hsmPass;
        this.hsmRequestProcessor = hsmRequestProcessor;
    }

    @Override
    public byte[] sign(RawTransaction rawTransaction, long chainId) {
        byte[] finalBytes;
        byte[] encodedTransaction;
        Sign.SignatureData signatureData;
        boolean isNewTx =
                chainId > ChainId.NONE && rawTransaction.getType().equals(TransactionType.LEGACY);

        if (isNewTx) {
            encodedTransaction = encode(rawTransaction, chainId);
        } else {
            encodedTransaction = encode(rawTransaction);
        }

        byte[] messageHash = Hash.sha3(encodedTransaction);

        signatureData = hsmRequestProcessor.callHSM(messageHash, hsmPass);

        if (isNewTx) {
            signatureData = createEip155SignatureData(signatureData, chainId);
        }

        finalBytes = encode(rawTransaction, signatureData);

        return finalBytes;
    }

    @Override
    public String getAddress() {
        return hsmPass.getAddress();
    }
}
