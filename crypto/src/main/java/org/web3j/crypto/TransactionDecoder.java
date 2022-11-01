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
package org.web3j.crypto;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.web3j.crypto.transaction.type.*;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

public class TransactionDecoder {
    private static final int UNSIGNED_ACCESS_LIST_TX_RLP_LIST_SIZE = 8;
    private static final int UNSIGNED_EIP1559TX_RLP_LIST_SIZE = 9;

    public static RawTransaction decode(final String hexTransaction) {
        final byte[] transaction = Numeric.hexStringToByteArray(hexTransaction);
        if (getTransactionType(transaction) == TransactionType.ACCESS_LIST) {
            return decodeAccessListTransaction(transaction);
        }
        if (getTransactionType(transaction) == TransactionType.EIP1559) {
            return decodeEIP1559Transaction(transaction);
        }
        return decodeLegacyTransaction(transaction);
    }

    private static TransactionType getTransactionType(final byte[] transaction) {
        // The first byte indicates a transaction type.
        byte firstByte = transaction[0];
        if (firstByte == TransactionType.ACCESS_LIST.getRlpType()) {
            return TransactionType.ACCESS_LIST;
        }
        if (firstByte == TransactionType.EIP1559.getRlpType()) {
            return TransactionType.EIP1559;
        }
        return TransactionType.LEGACY;
    }

    private static RawTransaction decodeAccessListTransaction(final byte[] transaction) {
        final byte[] encodedTx = Arrays.copyOfRange(transaction, 1, transaction.length);
        final RlpList rlpList = RlpDecoder.decode(encodedTx);
        final RlpList values = (RlpList) rlpList.getValue(0);

        final long chainId = ((RlpString) values.getValue(0)).asPositiveBigInteger().longValue();
        final BigInteger nonce = ((RlpString) values.getValue(1)).asPositiveBigInteger();
        final BigInteger gasPrice = ((RlpString) values.getValue(2)).asPositiveBigInteger();
        final BigInteger gasLimit = ((RlpString) values.getValue(3)).asPositiveBigInteger();
        final String to = ((RlpString) values.getValue(4)).asString();
        final BigInteger value = ((RlpString) values.getValue(5)).asPositiveBigInteger();
        final String data = ((RlpString) values.getValue(6)).asString();
        final List<AccessListObject> accessList =
                AccessListObject.getRlpDecodedList((RlpList) values.getValue(7));

        AccessListTransaction rawTx =
                AccessListTransaction.createTransaction(
                        chainId, nonce, gasPrice, gasLimit, to, value, data, accessList);

        if (values.size() == UNSIGNED_ACCESS_LIST_TX_RLP_LIST_SIZE) {
            return new RawTransaction(rawTx);
        }

        final byte[] v = ((RlpString) values.getValue(8)).getBytes();
        final byte[] r =
                Numeric.toBytesPadded(
                        Numeric.toBigInt(((RlpString) values.getValue(9)).getBytes()), 32);
        final byte[] s =
                Numeric.toBytesPadded(
                        Numeric.toBigInt(((RlpString) values.getValue(10)).getBytes()), 32);
        final Sign.SignatureData signature = new Sign.SignatureData(v, r, s);

        return new SignedRawTransaction(rawTx, signature);
    }

    private static RawTransaction decodeEIP1559Transaction(final byte[] transaction) {
        final byte[] encodedTx = Arrays.copyOfRange(transaction, 1, transaction.length);
        final RlpList rlpList = RlpDecoder.decode(encodedTx);
        final RlpList values = (RlpList) rlpList.getValues().get(0);

        final long chainId =
                ((RlpString) values.getValues().get(0)).asPositiveBigInteger().longValue();
        final BigInteger nonce = ((RlpString) values.getValues().get(1)).asPositiveBigInteger();
        final BigInteger maxPriorityFeePerGas =
                ((RlpString) values.getValues().get(2)).asPositiveBigInteger();
        final BigInteger maxFeePerGas =
                ((RlpString) values.getValues().get(3)).asPositiveBigInteger();
        final BigInteger gasLimit = ((RlpString) values.getValues().get(4)).asPositiveBigInteger();
        final String to = ((RlpString) values.getValues().get(5)).asString();

        final BigInteger value = ((RlpString) values.getValues().get(6)).asPositiveBigInteger();
        final String data = ((RlpString) values.getValues().get(7)).asString();
        final List<AccessListObject> accessList =
                AccessListObject.getRlpDecodedList((RlpList) values.getValues().get(8));

        final Transaction1559 rawTx =
                Transaction1559.createTransaction(
                        chainId,
                        nonce,
                        gasLimit,
                        to,
                        value,
                        data,
                        maxPriorityFeePerGas,
                        maxFeePerGas,
                        accessList);

        if (values.getValues().size() == UNSIGNED_EIP1559TX_RLP_LIST_SIZE) {
            return new RawTransaction(rawTx);
        } else {
            final byte[] v =
                    Sign.getVFromRecId(
                            Numeric.toBigInt(((RlpString) values.getValues().get(9)).getBytes())
                                    .intValue());
            final byte[] r =
                    Numeric.toBytesPadded(
                            Numeric.toBigInt(((RlpString) values.getValues().get(10)).getBytes()),
                            32);
            final byte[] s =
                    Numeric.toBytesPadded(
                            Numeric.toBigInt(((RlpString) values.getValues().get(11)).getBytes()),
                            32);
            final Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
            return new SignedRawTransaction(rawTx, signatureData);
        }
    }

    private static RawTransaction decodeLegacyTransaction(final byte[] transaction) {
        final RlpList rlpList = RlpDecoder.decode(transaction);
        final RlpList values = (RlpList) rlpList.getValues().get(0);
        final BigInteger nonce = ((RlpString) values.getValues().get(0)).asPositiveBigInteger();
        final BigInteger gasPrice = ((RlpString) values.getValues().get(1)).asPositiveBigInteger();
        final BigInteger gasLimit = ((RlpString) values.getValues().get(2)).asPositiveBigInteger();
        final String to = ((RlpString) values.getValues().get(3)).asString();
        final BigInteger value = ((RlpString) values.getValues().get(4)).asPositiveBigInteger();
        final String data = ((RlpString) values.getValues().get(5)).asString();
        if (values.getValues().size() == 6
                || (values.getValues().size() == 8
                        && ((RlpString) values.getValues().get(7)).getBytes().length == 10)
                || (values.getValues().size() == 9
                        && ((RlpString) values.getValues().get(8)).getBytes().length == 10)) {
            // the 8th or 9nth element is the hex
            // representation of "restricted" for private transactions
            return RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);
        } else {
            final byte[] v = ((RlpString) values.getValues().get(6)).getBytes();
            final byte[] r =
                    Numeric.toBytesPadded(
                            Numeric.toBigInt(((RlpString) values.getValues().get(7)).getBytes()),
                            32);
            final byte[] s =
                    Numeric.toBytesPadded(
                            Numeric.toBigInt(((RlpString) values.getValues().get(8)).getBytes()),
                            32);
            final Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
            return new SignedRawTransaction(
                    nonce, gasPrice, gasLimit, to, value, data, signatureData);
        }
    }
}
