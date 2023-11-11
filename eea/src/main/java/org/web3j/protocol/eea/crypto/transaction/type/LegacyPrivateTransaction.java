/*
 * Copyright 2023 Web3 Labs Ltd.
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
package org.web3j.protocol.eea.crypto.transaction.type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.web3j.crypto.Sign;
import org.web3j.crypto.transaction.type.TransactionType;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Base64String;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;
import org.web3j.utils.Restriction;

public class LegacyPrivateTransaction implements IPrivateTransaction {

    private final TransactionType transactionType;
    private final BigInteger nonce;
    private final BigInteger gasPrice;
    private final BigInteger gasLimit;
    private final String to;
    private final String data;
    private final Base64String privateFrom;
    private final List<Base64String> privateFor;
    private final Base64String privacyGroupId;
    private final Restriction restriction;

    public LegacyPrivateTransaction(
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            Base64String privateFrom,
            List<Base64String> privateFor,
            Base64String privacyGroupId,
            Restriction restriction) {
        this.transactionType = TransactionType.LEGACY;
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.to = to;
        this.data = data;
        this.privateFrom = privateFrom;
        this.privateFor = privateFor;
        this.privacyGroupId = privacyGroupId;
        this.restriction = restriction;
    }

    public static LegacyPrivateTransaction createContractTransaction(
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String data,
            Base64String privateFrom,
            List<Base64String> privateFor,
            Base64String privacyGroupId,
            Restriction restriction) {

        return new LegacyPrivateTransaction(
                nonce,
                gasPrice,
                gasLimit,
                null,
                data,
                privateFrom,
                privateFor,
                privacyGroupId,
                restriction);
    }

    public static LegacyPrivateTransaction createContractTransaction(
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String data,
            Base64String privateFrom,
            Base64String privacyGroupId,
            Restriction restriction) {

        return createContractTransaction(
                nonce, gasPrice, gasLimit, data, privateFrom, null, privacyGroupId, restriction);
    }

    public static LegacyPrivateTransaction createTransaction(
            final BigInteger nonce,
            final BigInteger gasPrice,
            final BigInteger gasLimit,
            final String to,
            final String data,
            final Base64String privateFrom,
            final List<Base64String> privateFor,
            final Base64String privacyGroupId,
            final Restriction restriction) {

        return new LegacyPrivateTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                data,
                privateFrom,
                privateFor,
                privacyGroupId,
                restriction);
    }

    public static LegacyPrivateTransaction createTransaction(
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            Base64String privateFrom,
            List<Base64String> privateFor,
            Base64String privacyGroupId,
            Restriction restriction) {

        return createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                "",
                privateFrom,
                privateFor,
                privacyGroupId,
                restriction);
    }

    public static LegacyPrivateTransaction createTransaction(
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            Base64String privateFrom,
            Base64String privacyGroupId,
            Restriction restriction) {

        return createTransaction(
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

    public static LegacyPrivateTransaction createTransaction(
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            Base64String privateFrom,
            final List<Base64String> privateFor,
            Restriction restriction) {

        return createTransaction(
                nonce, gasPrice, gasLimit, to, data, privateFrom, privateFor, null, restriction);
    }

    @Override
    public List<RlpType> asRlpValues(final Sign.SignatureData signatureData) {

        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(getNonce()));

        result.add(RlpString.create(getGasPrice()));
        result.add(RlpString.create(getGasLimit()));

        // an empty to address (contract creation) should not be encoded as a numeric 0 value
        String to = getTo();
        if (to != null && to.length() > 0) {
            // addresses that start with zeros should be encoded with the zeros included, not
            // as numeric values
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(getValue()));

        // value field will already be hex encoded, so we need to convert into binary first
        byte[] data = Numeric.hexStringToByteArray(getData());
        result.add(RlpString.create(data));

        if (signatureData != null) {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getV())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        result.add(getPrivateFrom().asRlp());

        getPrivateFor()
                .ifPresent(privateFor -> result.add(Base64String.unwrapListToRlp(privateFor)));

        getPrivacyGroupId().map(Base64String::asRlp).ifPresent(result::add);

        result.add(RlpString.create(getRestriction().getRestriction()));

        return result;
    }

    @Override
    public BigInteger getNonce() {
        return nonce;
    }

    @Override
    public BigInteger getGasPrice() {
        return gasPrice;
    }

    @Override
    public BigInteger getGasLimit() {
        return gasLimit;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public BigInteger getValue() {
        return BigInteger.ZERO;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public TransactionType getType() {
        return transactionType;
    }

    @Override
    public Base64String getPrivateFrom() {
        return privateFrom;
    }

    @Override
    public Optional<List<Base64String>> getPrivateFor() {
        return Optional.ofNullable(privateFor);
    }

    @Override
    public Optional<Base64String> getPrivacyGroupId() {
        return Optional.ofNullable(privacyGroupId);
    }

    @Override
    public Restriction getRestriction() {
        return restriction;
    }
}
