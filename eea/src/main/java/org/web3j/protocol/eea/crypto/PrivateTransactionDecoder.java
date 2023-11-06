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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.crypto.TransactionDecoder;
import org.web3j.protocol.eea.crypto.transaction.type.LegacyPrivateTransaction;
import org.web3j.protocol.eea.crypto.transaction.type.PrivateTransaction1559;
import org.web3j.protocol.eea.crypto.transaction.type.PrivateTransactionType;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Base64String;
import org.web3j.utils.Numeric;
import org.web3j.utils.Restriction;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PrivateTransactionDecoder {

    public static LegacyPrivateTransaction decode(final String hexTransaction) {
        final byte[] transaction = Numeric.hexStringToByteArray(hexTransaction);
        final PrivateTransactionType transactionType = getPrivateTransactionType(transaction);

        if (transactionType == PrivateTransactionType.PRIVATE_1559) {
            return decodePrivateTransaction1559(transaction);
        }
        return decodeLegacyPrivateTransaction(transaction);
    }

    private static PrivateTransactionType getPrivateTransactionType(final byte[] transaction) {
        // Determine the type of the private transaction, similar to TransactionDecoder.
        byte firstByte = transaction[0];
        if (firstByte == PrivateTransactionType.PRIVATE_1559.getRlpType())
            return PrivateTransactionType.PRIVATE_1559;
        else return PrivateTransactionType.PRIVATE_LEGACY;
    }

    private static LegacyPrivateTransaction decodePrivateTransaction1559(final byte[] transaction) {
        final byte[] encodedTx = Arrays.copyOfRange(transaction, 1, transaction.length);
        final RlpList rlpList = RlpDecoder.decode(encodedTx);
        final RlpList temp = (RlpList) rlpList.getValues().get(0);
        final List<RlpType> values = temp.getValues();

        System.out.println(rlpList);
        System.out.println(temp);
        System.out.println(values);

        final long chainId =
                ((RlpString) temp.getValues().get(0)).asPositiveBigInteger().longValue();
        System.out.println("chainId " + chainId);
        final BigInteger nonce = ((RlpString) temp.getValues().get(1)).asPositiveBigInteger();
        System.out.println("nonce " + nonce);

        final BigInteger maxPriorityFeePerGas =
                ((RlpString) temp.getValues().get(2)).asPositiveBigInteger();
        System.out.println("maxPriorityFeePerGas " + maxPriorityFeePerGas);
        final BigInteger maxFeePerGas =
                ((RlpString) temp.getValues().get(3)).asPositiveBigInteger();
        System.out.println("maxFeePerGas " + maxFeePerGas);

        final BigInteger gasLimit = ((RlpString) temp.getValues().get(4)).asPositiveBigInteger();
        System.out.println("gasLimit " + gasLimit);
        final String to = ((RlpString) temp.getValues().get(5)).asString();
        System.out.println("to " + to);

        final String data = ((RlpString) temp.getValues().get(7)).asString();
        System.out.println("data " + data);

        final Base64String privateFrom = extractBase64(values.get(8));
        System.out.println("privateFrom " + privateFrom);
        final Restriction restriction = extractRestriction(values.get(10));
        System.out.println("restriction " + restriction);

        if (values.get(9) instanceof RlpList) {
            List<Base64String> privateForList = extractBase64List(values.get(9));
            return new PrivateTransaction1559(
                    chainId,
                    nonce,
                    gasLimit,
                    to,
                    data,
                    maxPriorityFeePerGas,
                    maxFeePerGas,
                    privateFrom,
                    privateForList,
                    null,
                    restriction);
        } else {
            Base64String privacyGroupId = extractBase64(values.get(9));
            return new PrivateTransaction1559(
                    chainId,
                    nonce,
                    gasLimit,
                    to,
                    data,
                    maxPriorityFeePerGas,
                    maxFeePerGas,
                    privateFrom,
                    null,
                    privacyGroupId,
                    restriction);
        }
    }

    private static LegacyPrivateTransaction decodeLegacyPrivateTransaction(
            final byte[] transaction) {
        final RlpList rlpList = RlpDecoder.decode(transaction);
        final RlpList temp = (RlpList) rlpList.getValues().get(0);
        final List<RlpType> values = temp.getValues();

        final RawTransaction rawTransaction =
                TransactionDecoder.decode(Numeric.toHexString(transaction));

        if (values.size() == 9) {
            final Base64String privateFrom = extractBase64(values.get(6));
            final Restriction restriction = extractRestriction(values.get(8));

            if (values.get(7) instanceof RlpList) {
                List<Base64String> privateForList = extractBase64List(values.get(7));
                return new LegacyPrivateTransaction(
                        rawTransaction, privateFrom, privateForList, null, restriction);
            } else {
                Base64String privacyGroupId = extractBase64(values.get(7));
                return new LegacyPrivateTransaction(
                        rawTransaction, privateFrom, null, privacyGroupId, restriction);
            }
        } else {
            final Base64String privateFrom = extractBase64(values.get(9));
            final Restriction restriction = extractRestriction(values.get(11));
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

    private static Restriction extractRestriction(final RlpType value) {
        return Restriction.fromString(new String(((RlpString) value).getBytes(), UTF_8));
    }

    private static Base64String extractBase64(final RlpType value) {
        return Base64String.wrap(((RlpString) value).getBytes());
    }

    private static List<Base64String> extractBase64List(final RlpType values) {
        return ((RlpList) values)
                .getValues().stream()
                        .map(PrivateTransactionDecoder::extractBase64)
                        .collect(Collectors.toList());
    }
}
