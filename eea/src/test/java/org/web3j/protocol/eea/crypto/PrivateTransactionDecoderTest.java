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
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Base64String;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.web3j.utils.Restriction.RESTRICTED;

public class PrivateTransactionDecoderTest {

    private static final Base64String MOCK_ENCLAVE_KEY =
            Base64String.wrap("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
    private static final List<Base64String> MOCK_PRIVATE_FOR =
            Collections.singletonList(MOCK_ENCLAVE_KEY);

    @Test
    public void testDecoding() {
        final BigInteger nonce = BigInteger.ZERO;
        final BigInteger gasPrice = BigInteger.ONE;
        final BigInteger gasLimit = BigInteger.TEN;
        final String to = "0x0add5355";
        final RawPrivateTransaction rawTransaction =
                RawPrivateTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        "",
                        MOCK_ENCLAVE_KEY,
                        MOCK_PRIVATE_FOR,
                        RESTRICTED);
        final byte[] encodedMessage = PrivateTransactionEncoder.encode(rawTransaction);
        final String hexMessage = Numeric.toHexString(encodedMessage);

        final RawPrivateTransaction result = PrivateTransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals("", result.getData());
        assertEquals(MOCK_ENCLAVE_KEY, result.getPrivateFrom());
        assertEquals(MOCK_PRIVATE_FOR, result.getPrivateFor().get());
        assertEquals(RESTRICTED, result.getRestriction());
    }

    @Test
    public void testDecodingPrivacyGroup() {
        final BigInteger nonce = BigInteger.ZERO;
        final BigInteger gasPrice = BigInteger.ONE;
        final BigInteger gasLimit = BigInteger.TEN;
        final String to = "0x0add5355";
        final RawPrivateTransaction rawTransaction =
                RawPrivateTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        "",
                        MOCK_ENCLAVE_KEY,
                        MOCK_ENCLAVE_KEY,
                        RESTRICTED);
        final byte[] encodedMessage = PrivateTransactionEncoder.encode(rawTransaction);
        final String hexMessage = Numeric.toHexString(encodedMessage);

        final RawPrivateTransaction result = PrivateTransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals("", result.getData());
        assertEquals(MOCK_ENCLAVE_KEY, result.getPrivateFrom());
        assertEquals(MOCK_ENCLAVE_KEY, result.getPrivacyGroupId().get());
        assertEquals(RESTRICTED, result.getRestriction());
    }

    @Test
    public void testDecodingSigned() throws Exception {
        final BigInteger nonce = BigInteger.ZERO;
        final BigInteger gasPrice = BigInteger.ONE;
        final BigInteger gasLimit = BigInteger.TEN;
        final String to = "0x0add5355";
        final RawPrivateTransaction rawTransaction =
                RawPrivateTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        "",
                        MOCK_ENCLAVE_KEY,
                        MOCK_PRIVATE_FOR,
                        RESTRICTED);
        final String privateKey =
                "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
        final Credentials credentials = Credentials.create(privateKey);
        final byte[] encodedMessage =
                PrivateTransactionEncoder.signMessage(rawTransaction, credentials);
        final String hexMessage = Numeric.toHexString(encodedMessage);

        final RawPrivateTransaction result = PrivateTransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals("", result.getData());
        assertEquals(MOCK_ENCLAVE_KEY, result.getPrivateFrom());
        assertEquals(MOCK_PRIVATE_FOR, result.getPrivateFor().get());
        assertEquals(RESTRICTED, result.getRestriction());
        assertTrue(result instanceof SignedRawPrivateTransaction);
        final SignedRawPrivateTransaction signedResult = (SignedRawPrivateTransaction) result;
        assertNotNull(signedResult.getSignatureData());
        final Sign.SignatureData signatureData = signedResult.getSignatureData();
        final byte[] encodedTransaction = PrivateTransactionEncoder.encode(rawTransaction);
        final BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureData);
        assertEquals(key, credentials.getEcKeyPair().getPublicKey());
        assertEquals(credentials.getAddress(), signedResult.getFrom());
        signedResult.verify(credentials.getAddress());
        assertNull(signedResult.getChainId());
    }

    @Test
    public void testDecodingSignedPrivacyGroup() throws Exception {
        final BigInteger nonce = BigInteger.ZERO;
        final BigInteger gasPrice = BigInteger.ONE;
        final BigInteger gasLimit = BigInteger.TEN;
        final String to = "0x0add5355";
        final RawPrivateTransaction rawTransaction =
                RawPrivateTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        "",
                        MOCK_ENCLAVE_KEY,
                        MOCK_ENCLAVE_KEY,
                        RESTRICTED);
        final String privateKey =
                "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
        final Credentials credentials = Credentials.create(privateKey);
        final byte[] encodedMessage =
                PrivateTransactionEncoder.signMessage(rawTransaction, credentials);
        final String hexMessage = Numeric.toHexString(encodedMessage);

        final RawPrivateTransaction result = PrivateTransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals("", result.getData());
        assertEquals(MOCK_ENCLAVE_KEY, result.getPrivateFrom());
        assertEquals(MOCK_ENCLAVE_KEY, result.getPrivacyGroupId().get());
        assertEquals(RESTRICTED, result.getRestriction());
        assertTrue(result instanceof SignedRawPrivateTransaction);
        final SignedRawPrivateTransaction signedResult = (SignedRawPrivateTransaction) result;
        assertNotNull(signedResult.getSignatureData());
        final Sign.SignatureData signatureData = signedResult.getSignatureData();
        final byte[] encodedTransaction = PrivateTransactionEncoder.encode(rawTransaction);
        final BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureData);
        assertEquals(key, credentials.getEcKeyPair().getPublicKey());
        assertEquals(credentials.getAddress(), signedResult.getFrom());
        signedResult.verify(credentials.getAddress());
        assertNull(signedResult.getChainId());
    }

    @Test
    public void testDecodingSignedChainId() throws Exception {
        final BigInteger nonce = BigInteger.ZERO;
        final BigInteger gasPrice = BigInteger.ONE;
        final BigInteger gasLimit = BigInteger.TEN;
        final String to = "0x0add5355";
        final long chainId = 2018L;
        final RawPrivateTransaction rawTransaction =
                RawPrivateTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        to,
                        "",
                        MOCK_ENCLAVE_KEY,
                        MOCK_PRIVATE_FOR,
                        RESTRICTED);
        final String privateKey =
                "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
        final Credentials credentials = Credentials.create(privateKey);
        final byte[] signedMessage =
                PrivateTransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        final String hexMessage = Numeric.toHexString(signedMessage);

        final RawPrivateTransaction result = PrivateTransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals("", result.getData());
        assertEquals(MOCK_ENCLAVE_KEY, result.getPrivateFrom());
        assertEquals(MOCK_PRIVATE_FOR, result.getPrivateFor().get());
        assertEquals(RESTRICTED, result.getRestriction());
        assertTrue(result instanceof SignedRawPrivateTransaction);
        final SignedRawPrivateTransaction signedResult = (SignedRawPrivateTransaction) result;
        assertEquals(credentials.getAddress(), signedResult.getFrom());
        signedResult.verify(credentials.getAddress());
        assertEquals(chainId, signedResult.getChainId().longValue());
    }
}
