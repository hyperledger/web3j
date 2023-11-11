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

import org.web3j.crypto.Sign;
import org.web3j.crypto.transaction.type.TransactionType;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Base64String;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;
import org.web3j.utils.Restriction;

public class PrivateTransaction1559 extends LegacyPrivateTransaction
        implements IPrivateTransaction {

    private final TransactionType transactionType;
    private final long chainId;
    private final BigInteger maxPriorityFeePerGas;
    private final BigInteger maxFeePerGas;

    public PrivateTransaction1559(
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
        super(
                nonce,
                null,
                gasLimit,
                to,
                data,
                privateFrom,
                privateFor,
                privacyGroupId,
                restriction);
        this.transactionType = TransactionType.EIP1559;
        this.chainId = chainId;
        this.maxPriorityFeePerGas = maxPriorityFeePerGas;
        this.maxFeePerGas = maxFeePerGas;
    }

    public static PrivateTransaction1559 createContractTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            BigInteger gasLimit,
            String data,
            Base64String privateFrom,
            List<Base64String> privateFor,
            Base64String privacyGroupId,
            Restriction restriction) {

        return new PrivateTransaction1559(
                chainId,
                nonce,
                maxPriorityFeePerGas,
                maxFeePerGas,
                gasLimit,
                null,
                data,
                privateFrom,
                privateFor,
                privacyGroupId,
                restriction);
    }

    public static PrivateTransaction1559 createContractTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            BigInteger gasLimit,
            String data,
            Base64String privateFrom,
            Base64String privacyGroupId,
            Restriction restriction) {

        return createContractTransaction(
                chainId,
                nonce,
                maxPriorityFeePerGas,
                maxFeePerGas,
                gasLimit,
                data,
                privateFrom,
                null,
                privacyGroupId,
                restriction);
    }

    public static PrivateTransaction1559 createTransaction(
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

        return new PrivateTransaction1559(
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
                restriction);
    }

    public static PrivateTransaction1559 createTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            Base64String privateFrom,
            Base64String privacyGroupId,
            Restriction restriction) {

        return createTransaction(
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
                restriction);
    }

    public static PrivateTransaction1559 createTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger maxPriorityFeePerGas,
            BigInteger maxFeePerGas,
            Base64String privateFrom,
            List<Base64String> privateFor,
            Restriction restriction) {

        return new PrivateTransaction1559(
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
                restriction);
    }

    @Override
    public List<RlpType> asRlpValues(final Sign.SignatureData signatureData) {

        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(getChainId()));

        result.add(RlpString.create(getNonce()));

        // add maxPriorityFeePerGas and maxFeePerGas if this is an EIP-1559 transaction
        result.add(RlpString.create(getMaxPriorityFeePerGas()));
        result.add(RlpString.create(getMaxFeePerGas()));

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
            result.add(RlpString.create(Sign.getRecId(signatureData, getChainId())));
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
    public BigInteger getGasPrice() {
        throw new UnsupportedOperationException("not available for 1559 transaction");
    }

    public long getChainId() {
        return chainId;
    }

    public BigInteger getMaxPriorityFeePerGas() {
        return maxPriorityFeePerGas;
    }

    public BigInteger getMaxFeePerGas() {
        return maxFeePerGas;
    }

    @Override
    public TransactionType getType() {
        return transactionType;
    }
}
