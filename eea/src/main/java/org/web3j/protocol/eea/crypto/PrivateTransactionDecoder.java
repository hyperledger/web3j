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

import java.util.List;
import java.util.stream.Collectors;

import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.crypto.TransactionDecoder;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

public class PrivateTransactionDecoder {

    public static RawPrivateTransaction decode(final String hexTransaction) {
        final byte[] transaction = Numeric.hexStringToByteArray(hexTransaction);
        final RlpList rlpList = RlpDecoder.decode(transaction);
        final RlpList temp = (RlpList) rlpList.getValues().get(0);
        final List<RlpType> values = temp.getValues();

        final RawTransaction rawTransaction = TransactionDecoder.decode(hexTransaction);

        if (values.size() == 9) {
            final String privateFrom = extractBase64(values.get(6));
            final String restriction = extractString(values.get(8));
            if (values.get(7) instanceof RlpList) {
                return new RawPrivateTransaction(
                        rawTransaction,
                        privateFrom,
                        extractBase64List((RlpList) values.get(7)),
                        restriction);
            } else {
                return new RawPrivateTransaction(
                        rawTransaction, privateFrom, extractBase64(values.get(7)), restriction);
            }

        } else {
            final String privateFrom = extractBase64(values.get(9));
            final String restriction = extractString(values.get(11));
            if (values.get(10) instanceof RlpList) {
                return new SignedRawPrivateTransaction(
                        (SignedRawTransaction) rawTransaction,
                        privateFrom,
                        extractBase64List(values.get(10)),
                        restriction);
            } else {
                return new SignedRawPrivateTransaction(
                        (SignedRawTransaction) rawTransaction,
                        privateFrom,
                        extractBase64(values.get(10)),
                        restriction);
            }
        }
    }

    private static String extractString(final RlpType value) {
        return new String(((RlpString) value).getBytes());
    }

    private static String extractBase64(final RlpType value) {
        return Numeric.byteArrayToBase64(((RlpString) value).getBytes());
    }

    private static List<String> extractBase64List(final RlpType values) {
        return ((RlpList) values)
                .getValues().stream()
                        .map(PrivateTransactionDecoder::extractBase64)
                        .collect(Collectors.toList());
    }
}
