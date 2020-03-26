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
import java.security.SignatureException;

import org.web3j.utils.Numeric;

public interface SignatureDataOperations {

    int CHAIN_ID_INC = 35;
    int LOWER_REAL_V = 27;

    Sign.SignatureData getSignatureData();

    byte[] getEncodedTransaction(Long chainId);

    default String getFrom() throws SignatureException {
        final byte[] encodedTransaction = getEncodedTransaction(getChainId());
        final BigInteger v = Numeric.toBigInt(getSignatureData().getV());
        final byte[] r = getSignatureData().getR();
        final byte[] s = getSignatureData().getS();
        final Sign.SignatureData signatureDataV = new Sign.SignatureData(getRealV(v), r, s);
        final BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureDataV);
        return "0x" + Keys.getAddress(key);
    }

    default void verify(final String from) throws SignatureException {
        final String actualFrom = getFrom();
        if (!actualFrom.equals(from)) {
            throw new SignatureException("from mismatch");
        }
    }

    default byte getRealV(final BigInteger bv) {
        final long v = bv.longValue();
        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return (byte) v;
        }
        final byte realV = LOWER_REAL_V;
        int inc = 0;
        if ((int) v % 2 == 0) {
            inc = 1;
        }
        return (byte) (realV + inc);
    }

    default Long getChainId() {
        final BigInteger bv = Numeric.toBigInt(getSignatureData().getV());
        final long v = bv.longValue();
        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return null;
        }
        return (v - CHAIN_ID_INC) / 2;
    }
}
