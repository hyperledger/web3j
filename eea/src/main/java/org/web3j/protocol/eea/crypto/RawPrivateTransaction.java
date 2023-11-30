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
import java.util.Optional;

import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.eea.crypto.transaction.type.IPrivateTransaction;
import org.web3j.protocol.eea.crypto.transaction.type.LegacyPrivateTransaction;
import org.web3j.protocol.eea.crypto.transaction.type.PrivateTransaction1559;
import org.web3j.utils.Base64String;
import org.web3j.utils.Restriction;

/**
 * Transaction class used for signing transactions locally.<br>
 * For the specification, refer to p4 of the <a href="http://gavwood.com/paper.pdf">yellow
 * paper</a>.
 */
public class RawPrivateTransaction extends RawTransaction {

    private final IPrivateTransaction privateTransaction;

    protected RawPrivateTransaction(final IPrivateTransaction privateTransaction) {
        super(privateTransaction);
        this.privateTransaction = privateTransaction;
    }

    protected RawPrivateTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Base64String privacyGroupId,
            final Restriction restriction) {
        this(
                new LegacyPrivateTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        data,
                        privateFrom,
                        privateFor,
                        privacyGroupId,
                        restriction));
    }

    protected RawPrivateTransaction(
            final long chainId,
            final BigInteger nonce,
            final BigInteger maxPriorityFeePerGas,
            final BigInteger maxFeePerGas,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Base64String privacyGroupId,
            final Restriction restriction) {
        this(
                new PrivateTransaction1559(
                        chainId,
                        nonce,
                        maxPriorityFeePerGas,
                        maxFeePerGas,
                        gasLimit,
                        to,
                        data,
                        privateFrom,
                        privateFor,
                        privacyGroupId,
                        restriction));
    }

    public static RawPrivateTransaction createContractTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String data,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Base64String privacyGroupId,
            final Restriction restriction) {

        return new RawPrivateTransaction(
                LegacyPrivateTransaction.createContractTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        data,
                        privateFrom,
                        privateFor,
                        privacyGroupId,
                        restriction));
    }

    public static RawPrivateTransaction createContractTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String data,
            final Base64String privateFrom,
            final Base64String privacyGroupId,
            final Restriction restriction) {

        return new RawPrivateTransaction(
                LegacyPrivateTransaction.createContractTransaction(
                        nonce, gasPrice, gasLimit, data, privateFrom, privacyGroupId, restriction));
    }

    public static RawPrivateTransaction createContractTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            final BigInteger gasLimit,
            final String data,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Base64String privacyGroupId,
            final Restriction restriction) {

        return new RawPrivateTransaction(
                PrivateTransaction1559.createContractTransaction(
                        chainId,
                        nonce,
                        maxPriorityFeePerGas,
                        maxFeePerGas,
                        gasLimit,
                        data,
                        privateFrom,
                        privateFor,
                        privacyGroupId,
                        restriction));
    }

    public static RawPrivateTransaction createTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Base64String privacyGroupId,
            final Restriction restriction) {

        return new RawPrivateTransaction(
                LegacyPrivateTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        data,
                        privateFrom,
                        privateFor,
                        privacyGroupId,
                        restriction));
    }

    public static RawPrivateTransaction createTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final Base64String privateFrom,
            final Base64String privacyGroupId,
            final Restriction restriction) {

        return new RawPrivateTransaction(
                LegacyPrivateTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        data,
                        privateFrom,
                        privacyGroupId,
                        restriction));
    }

    public static RawPrivateTransaction createTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Restriction restriction) {

        return new RawPrivateTransaction(
                LegacyPrivateTransaction.createTransaction(
                        nonce, gasPrice, gasLimit, to, data, privateFrom, privateFor, restriction));
    }

    public static RawPrivateTransaction createTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            BigInteger gasLimit,
            String to,
            String data,
            Base64String privateFrom,
            List<Base64String> privateFor,
            Base64String privacyGroupId,
            Restriction restriction) {

        return new RawPrivateTransaction(
                PrivateTransaction1559.createTransaction(
                        chainId,
                        nonce,
                        maxPriorityFeePerGas,
                        maxFeePerGas,
                        gasLimit,
                        to,
                        data,
                        privateFrom,
                        privateFor,
                        privacyGroupId,
                        restriction));
    }

    public static RawPrivateTransaction createTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            BigInteger gasLimit,
            String to,
            String data,
            Base64String privateFrom,
            Base64String privacyGroupId,
            Restriction restriction) {

        return new RawPrivateTransaction(
                PrivateTransaction1559.createTransaction(
                        chainId,
                        nonce,
                        maxPriorityFeePerGas,
                        maxFeePerGas,
                        gasLimit,
                        to,
                        data,
                        privateFrom,
                        null,
                        privacyGroupId,
                        restriction));
    }

    public static RawPrivateTransaction createTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            BigInteger gasLimit,
            String to,
            String data,
            Base64String privateFrom,
            List<Base64String> privateFor,
            Restriction restriction) {

        return new RawPrivateTransaction(
                PrivateTransaction1559.createTransaction(
                        chainId,
                        nonce,
                        maxPriorityFeePerGas,
                        maxFeePerGas,
                        gasLimit,
                        to,
                        data,
                        privateFrom,
                        privateFor,
                        null,
                        restriction));
    }

    public Base64String getPrivateFrom() {
        return privateTransaction.getPrivateFrom();
    }

    public Optional<List<Base64String>> getPrivateFor() {
        return privateTransaction.getPrivateFor();
    }

    public Optional<Base64String> getPrivacyGroupId() {
        return privateTransaction.getPrivacyGroupId();
    }

    public Restriction getRestriction() {
        return privateTransaction.getRestriction();
    }

    public IPrivateTransaction getPrivateTransaction() {
        return privateTransaction;
    }
}
