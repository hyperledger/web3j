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
import java.util.Collections;
import java.util.List;

import org.web3j.crypto.AccessListObject;
import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

import static org.web3j.crypto.transaction.type.TransactionType.EIP2930;

public class Transaction2930 extends LegacyTransaction {
    private long chainId;
    private List<AccessListObject> accessList;

    public Transaction2930(
            long chainId,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value,
            String data,
            List<AccessListObject> accessList) {
        super(EIP2930, nonce, gasPrice, gasLimit, to, value, data);
        this.chainId = chainId;
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

        // access list
        result.add(new RlpList(rlpAccessListRlp()));

        if (signatureData != null) {
            result.add(RlpString.create(Sign.getRecId(signatureData, getChainId())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        return result;
    }

    protected List<RlpType> rlpAccessListRlp() {

        List<AccessListObject> accessList = getAccessList();
        List<RlpType> rlpAccessList = new ArrayList<>();
        accessList.forEach(
                entry -> {
                    List<RlpType> rlpAccessListObject = new ArrayList<>();
                    rlpAccessListObject.add(
                            RlpString.create(Numeric.hexStringToByteArray(entry.getAddress())));
                    List<RlpType> keyList = new ArrayList<>();
                    entry.getStorageKeys()
                            .forEach(
                                    key -> {
                                        keyList.add(
                                                RlpString.create(
                                                        Numeric.hexStringToByteArray(key)));
                                    });
                    rlpAccessListObject.add(new RlpList(keyList));
                    rlpAccessList.add(new RlpList(rlpAccessListObject));
                });
        return rlpAccessList;
    }

    public static Transaction2930 createEtherTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value) {
        return new Transaction2930(
                chainId, nonce, gasPrice, gasLimit, to, value, "", Collections.emptyList());
    }

    public static Transaction2930 createTransaction(
            long chainId,
            BigInteger nonce,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            BigInteger value,
            String data,
            List<AccessListObject> accessList) {
        return new Transaction2930(chainId, nonce, gasPrice, gasLimit, to, value, data, accessList);
    }

    public long getChainId() {
        return chainId;
    }

    public List<AccessListObject> getAccessList() {
        return accessList;
    }
}
