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
package org.web3j.protocol.eea.crypto;

import java.math.BigInteger;
import java.util.List;

import org.web3j.crypto.Sign;
import org.web3j.crypto.SignatureDataOperations;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.utils.Base64String;
import org.web3j.utils.Restriction;

public class SignedRawPrivateTransaction extends RawPrivateTransaction
        implements SignatureDataOperations {

    private final Sign.SignatureData signatureData;

    public SignedRawPrivateTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final Sign.SignatureData signatureData,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Base64String privacyGroupId,
            final Restriction restriction) {
        super(
                nonce,
                gasPrice,
                gasLimit,
                to,
                data,
                privateFrom,
                privateFor,
                privacyGroupId,
                restriction);
        this.signatureData = signatureData;
    }

    public SignedRawPrivateTransaction(
            final SignedRawTransaction signedRawTransaction,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Restriction restriction) {
        this(signedRawTransaction, privateFrom, privateFor, null, restriction);
    }

    public SignedRawPrivateTransaction(
            final SignedRawTransaction signedRawTransaction,
            final Base64String privateFrom,
            final Base64String privacyGroupId,
            final Restriction restriction) {
        this(signedRawTransaction, privateFrom, null, privacyGroupId, restriction);
    }

    private SignedRawPrivateTransaction(
            final SignedRawTransaction signedRawTransaction,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Base64String privacyGroupId,
            final Restriction restriction) {
        this(
                signedRawTransaction.getNonce(),
                signedRawTransaction.getGasPrice(),
                signedRawTransaction.getGasLimit(),
                signedRawTransaction.getTo(),
                signedRawTransaction.getData(),
                signedRawTransaction.getSignatureData(),
                privateFrom,
                privateFor,
                privacyGroupId,
                restriction);
    }

    public Sign.SignatureData getSignatureData() {
        return signatureData;
    }

    @Override
    public byte[] getEncodedTransaction(final Long chainId) {
        if (null == chainId) {
            return PrivateTransactionEncoder.encode(this);
        } else {
            return PrivateTransactionEncoder.encode(this, chainId);
        }
    }
}
