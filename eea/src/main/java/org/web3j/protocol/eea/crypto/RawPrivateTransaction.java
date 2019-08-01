/*
 * Copyright 2019 Web3 Labs LTD.
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

/**
 * Transaction class used for signing transactions locally.<br>
 * For the specification, refer to p4 of the <a href="http://gavwood.com/paper.pdf">yellow
 * paper</a>.
 */
public class RawPrivateTransaction extends RawTransaction {

    private final String privateFrom;
    private final List<String> privateFor;
    private final String privacyGroupId;
    private final String restriction;

    protected RawPrivateTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final String privateFrom,
            final List<String> privateFor,
            final String privacyGroupId,
            final String restriction) {
        super(nonce, gasPrice, gasLimit, to, BigInteger.ZERO, data);
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.privacyGroupId = privacyGroupId;
        this.restriction = restriction;
    }

    protected RawPrivateTransaction(
            final RawTransaction rawTransaction,
            final String privateFrom,
            final String privacyGroupId,
            final String restriction) {
        this(rawTransaction, privateFrom, null, privacyGroupId, restriction);
    }

    protected RawPrivateTransaction(
            final RawTransaction rawTransaction,
            final String privateFrom,
            final List<String> privateFor,
            final String restriction) {
        this(rawTransaction, privateFrom, privateFor, null, restriction);
    }

    private RawPrivateTransaction(
            final RawTransaction rawTransaction,
            final String privateFrom,
            final List<String> privateFor,
            final String privacyGroupId,
            final String restriction) {
        this(
                rawTransaction.getNonce(),
                rawTransaction.getGasPrice(),
                rawTransaction.getGasLimit(),
                rawTransaction.getTo(),
                rawTransaction.getData(),
                privateFrom,
                privateFor,
                privacyGroupId,
                restriction);
    }

    public static RawPrivateTransaction createContractTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String init,
            final String privateFrom,
            final List<String> privateFor,
            final String restriction) {

        return new RawPrivateTransaction(
                nonce, gasPrice, gasLimit, "", init, privateFrom, privateFor, null, restriction);
    }

    public static RawPrivateTransaction createContractTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String init,
            final String privateFrom,
            final String privacyGroupId,
            final String restriction) {

        return new RawPrivateTransaction(
                nonce,
                gasPrice,
                gasLimit,
                "",
                init,
                privateFrom,
                null,
                privacyGroupId,
                restriction);
    }

    public static RawPrivateTransaction createTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final String privateFrom,
            final List<String> privateFor,
            final String restriction) {

        return new RawPrivateTransaction(
                nonce, gasPrice, gasLimit, to, data, privateFrom, privateFor, null, restriction);
    }

    public static RawPrivateTransaction createTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final String privateFrom,
            final String privacyGroupId,
            final String restriction) {

        return new RawPrivateTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                data,
                privateFrom,
                null,
                privacyGroupId,
                restriction);
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public Optional<List<String>> getPrivateFor() {
        return Optional.ofNullable(privateFor);
    }

    public Optional<String> getPrivacyGroupId() {
        return Optional.ofNullable(privacyGroupId);
    }

    public String getRestriction() {
        return restriction;
    }

    RawTransaction asRawTransaction() {
        return RawTransaction.createTransaction(
                getNonce(), getGasPrice(), getGasLimit(), getTo(), BigInteger.ZERO, getData());
    }
}
