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

import org.junit.jupiter.api.Test;

import org.web3j.crypto.transaction.type.Transaction1559;
import org.web3j.utils.Numeric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionDecoderTest {

    @Test
    public void testDecoding() throws Exception {
        BigInteger nonce = BigInteger.ZERO;
        BigInteger gasPrice = BigInteger.ONE;
        BigInteger gasLimit = BigInteger.TEN;
        String to = "0x0add5355";
        BigInteger value = BigInteger.valueOf(Long.MAX_VALUE);
        RawTransaction rawTransaction =
                RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);
        byte[] encodedMessage = TransactionEncoder.encode(rawTransaction);
        String hexMessage = Numeric.toHexString(encodedMessage);

        RawTransaction result = TransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals(value, result.getValue());
        assertEquals("", result.getData());
    }

    @Test
    public void testDecodingSigned() throws Exception {
        BigInteger nonce = BigInteger.ZERO;
        BigInteger gasPrice = BigInteger.ONE;
        BigInteger gasLimit = BigInteger.TEN;
        String to = "0x0add5355";
        BigInteger value = BigInteger.valueOf(Long.MAX_VALUE);
        RawTransaction rawTransaction =
                RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);
        byte[] signedMessage =
                TransactionEncoder.signMessage(rawTransaction, SampleKeys.CREDENTIALS);
        String hexMessage = Numeric.toHexString(signedMessage);

        RawTransaction result = TransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals(value, result.getValue());
        assertEquals("", result.getData());
        assertTrue(result instanceof SignedRawTransaction);
        SignedRawTransaction signedResult = (SignedRawTransaction) result;
        assertNotNull(signedResult.getSignatureData());
        Sign.SignatureData signatureData = signedResult.getSignatureData();
        byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureData);
        assertEquals(key, SampleKeys.PUBLIC_KEY);
        assertEquals(SampleKeys.ADDRESS, signedResult.getFrom());
        signedResult.verify(SampleKeys.ADDRESS);
        assertNull(signedResult.getChainId());
    }

    @Test
    public void testDecodingSignedChainId() throws Exception {
        BigInteger nonce = BigInteger.ZERO;
        BigInteger gasPrice = BigInteger.ONE;
        BigInteger gasLimit = BigInteger.TEN;
        String to = "0x0add5355";
        BigInteger value = BigInteger.valueOf(Long.MAX_VALUE);
        long chainId = 46;
        RawTransaction rawTransaction =
                RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, value);
        byte[] signedMessage =
                TransactionEncoder.signMessage(rawTransaction, chainId, SampleKeys.CREDENTIALS);
        String hexMessage = Numeric.toHexString(signedMessage);

        RawTransaction result = TransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals(value, result.getValue());
        assertEquals("", result.getData());
        assertTrue(result instanceof SignedRawTransaction);
        SignedRawTransaction signedResult = (SignedRawTransaction) result;
        assertEquals(SampleKeys.ADDRESS, signedResult.getFrom());
        signedResult.verify(SampleKeys.ADDRESS);
        assertEquals(chainId, signedResult.getChainId().longValue());
    }

    @Test
    public void testRSize31() throws Exception {

        String hexTransaction =
                "0xf883370183419ce09433c98f20dd73d7bb1d533c4aa3371f2b30c6ebde80a45093dc7d00000000000000000000000000000000000000000000000000000000000000351c9fb90996c836fb34b782ee3d6efa9e2c79a75b277c014e353b51b23b00524d2da07435ebebca627a51a863bf590aff911c4746ab8386a0477c8221bb89671a5d58";

        RawTransaction result = TransactionDecoder.decode(hexTransaction);
        SignedRawTransaction signedResult = (SignedRawTransaction) result;
        assertEquals("0x1b609b03e2e9b0275a61fa5c69a8f32550285536", signedResult.getFrom());
    }

    @Test
    public void testDecoding1559() {
        final RawTransaction rawTransaction = createEip1559RawTransaction();
        final Transaction1559 transaction1559 = (Transaction1559) rawTransaction.getTransaction();

        final byte[] encodedMessage = TransactionEncoder.encode(rawTransaction);
        final String hexMessage = Numeric.toHexString(encodedMessage);

        final RawTransaction result = TransactionDecoder.decode(hexMessage);
        assertTrue(result.getTransaction() instanceof Transaction1559);
        final Transaction1559 resultTransaction1559 = (Transaction1559) result.getTransaction();

        assertNotNull(result);
        assertEquals(transaction1559.getChainId(), resultTransaction1559.getChainId());
        assertEquals(transaction1559.getNonce(), resultTransaction1559.getNonce());
        assertEquals(transaction1559.getMaxFeePerGas(), resultTransaction1559.getMaxFeePerGas());
        assertEquals(
                transaction1559.getMaxPriorityFeePerGas(),
                resultTransaction1559.getMaxPriorityFeePerGas());
        assertEquals(transaction1559.getGasLimit(), resultTransaction1559.getGasLimit());
        assertEquals(transaction1559.getTo(), resultTransaction1559.getTo());
        assertEquals(transaction1559.getValue(), resultTransaction1559.getValue());
        assertEquals(transaction1559.getData(), resultTransaction1559.getData());
    }

    @Test
    public void testDecodingSigned1559() throws SignatureException {
        final RawTransaction rawTransaction = createEip1559RawTransaction();
        final Transaction1559 transaction1559 = (Transaction1559) rawTransaction.getTransaction();

        final byte[] signedMessage =
                TransactionEncoder.signMessage(rawTransaction, SampleKeys.CREDENTIALS);
        final String signedHexMessage = Numeric.toHexString(signedMessage);

        final RawTransaction result = TransactionDecoder.decode(signedHexMessage);
        assertTrue(result.getTransaction() instanceof Transaction1559);
        final Transaction1559 resultTransaction1559 = (Transaction1559) result.getTransaction();

        assertNotNull(result);
        assertEquals(transaction1559.getChainId(), resultTransaction1559.getChainId());
        assertEquals(transaction1559.getNonce(), resultTransaction1559.getNonce());
        assertEquals(transaction1559.getMaxFeePerGas(), resultTransaction1559.getMaxFeePerGas());
        assertEquals(
                transaction1559.getMaxPriorityFeePerGas(),
                resultTransaction1559.getMaxPriorityFeePerGas());
        assertEquals(transaction1559.getGasLimit(), resultTransaction1559.getGasLimit());
        assertEquals(transaction1559.getTo(), resultTransaction1559.getTo());
        assertEquals(transaction1559.getValue(), resultTransaction1559.getValue());
        assertEquals(transaction1559.getData(), resultTransaction1559.getData());

        assertTrue(result instanceof SignedRawTransaction);
        final SignedRawTransaction signedResult = (SignedRawTransaction) result;
        assertNotNull(signedResult.getSignatureData());

        final Sign.SignatureData signatureData = signedResult.getSignatureData();
        final byte[] encodedTransaction = TransactionEncoder.encode(rawTransaction);
        final BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureData);
        assertEquals(key, SampleKeys.PUBLIC_KEY);
        assertEquals(SampleKeys.ADDRESS, signedResult.getFrom());
        signedResult.verify(SampleKeys.ADDRESS);
        assertNull(signedResult.getChainId());
    }

    private static RawTransaction createEip1559RawTransaction() {
        return RawTransaction.createEtherTransaction(
                3L,
                BigInteger.valueOf(0),
                BigInteger.valueOf(30000),
                "0x627306090abab3a6e1400e9345bc60c78a8bef57",
                BigInteger.valueOf(123),
                BigInteger.valueOf(5678),
                BigInteger.valueOf(1100000));
    }
}
