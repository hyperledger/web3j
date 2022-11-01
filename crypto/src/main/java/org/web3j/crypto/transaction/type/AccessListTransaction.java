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
package org.web3j.crypto.transaction.type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

/**
 * Transaction class used for signing AccessList (EIP-2930) transactions. Specification: <a
 * href="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-2930.md#specification">EIP-2930</a>
 */
public class AccessListTransaction implements ITransaction {
    private final long chainId;
    private final BigInteger nonce;
    private final BigInteger gasPrice;
    private final BigInteger gasLimit;
    private final String to;
    private final BigInteger value;
    private final String data;
    private final List<AccessListObject> accessList;

    public AccessListTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value,
            String data,
            List<AccessListObject> accessList) {
        this.chainId = chainId;
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.to = to;
        this.value = value;
        this.data = data;
        this.accessList = accessList;
    }

    @Override
    public List<RlpType> asRlpValues(Sign.SignatureData signatureData) {
        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(getChainId()));
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

        if (accessList == null) {
            result.add(new RlpList());
        } else {
            result.add(AccessListObject.getRlpEncodedList(accessList));
        }

        if (signatureData != null) {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getV())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        return result;
    }

    public static AccessListTransaction createTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value,
            String data,
            List<AccessListObject> accessList) {

        return new AccessListTransaction(
                chainId, nonce, gasPrice, gasLimit, to, value, data, accessList);
    }

    public long getChainId() {
        return chainId;
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
        return value;
    }

    @Override
    public String getData() {
        return data;
    }

    public List<AccessListObject> getAccessList() {
        return accessList;
    }

    @Override
    public TransactionType getType() {
        return TransactionType.ACCESS_LIST;
    }
}
