package org.web3j.protocol.eea.crypto;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class PrivateTransactionDecoderTest {

    @Test
    public void testDecoding() {
        final BigInteger nonce = BigInteger.ZERO;
        final BigInteger gasPrice = BigInteger.ONE;
        final BigInteger gasLimit = BigInteger.TEN;
        final String to = "0x0add5355";
        final String privateFrom = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
        final List<String> privateFor =
                Collections.singletonList("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
        final String restriction = "restricted";
        final RawPrivateTransaction rawTransaction = RawPrivateTransaction.createTransaction(
                nonce, gasPrice, gasLimit, to, "", privateFrom, privateFor, restriction);
        byte[] encodedMessage = PrivateTransactionEncoder.encode(rawTransaction);
        final String hexMessage = Numeric.toHexString(encodedMessage);

        final RawPrivateTransaction result = PrivateTransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals("", result.getData());
    }

    @Test
    public void testDecodingSigned() throws Exception {
        final BigInteger nonce = BigInteger.ZERO;
        final BigInteger gasPrice = BigInteger.ONE;
        final BigInteger gasLimit = BigInteger.TEN;
        final String to = "0x0add5355";
        final String privateFrom = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
        final List<String> privateFor =
                Collections.singletonList("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
        final String restriction = "restricted";
        final RawPrivateTransaction rawTransaction = RawPrivateTransaction.createTransaction(
                nonce, gasPrice, gasLimit, to, "", privateFrom, privateFor, restriction);
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
        assertEquals(privateFrom, result.getPrivateFrom());
        assertEquals(privateFor, result.getPrivateFor());
        assertEquals(restriction, result.getRestriction());
        assertTrue(result instanceof SignedRawPrivateTransaction);
        final SignedRawPrivateTransaction signedResult = (SignedRawPrivateTransaction) result;
        assertNotNull(signedResult.getSignatureData());
        Sign.SignatureData signatureData = signedResult.getSignatureData();
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
        final Integer chainId = 1;
        final String privateFrom = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=";
        final List<String> privateFor =
                Collections.singletonList("A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo=");
        final String restriction = "restricted";
        final RawPrivateTransaction rawTransaction = RawPrivateTransaction.createTransaction(
                nonce, gasPrice, gasLimit, to, "", privateFrom, privateFor, restriction);
        final String privateKey =
                "8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";
        final Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = PrivateTransactionEncoder.signMessage(
                rawTransaction, chainId.byteValue(), credentials);
        final String hexMessage = Numeric.toHexString(signedMessage);

        final RawPrivateTransaction result = PrivateTransactionDecoder.decode(hexMessage);
        assertNotNull(result);
        assertEquals(nonce, result.getNonce());
        assertEquals(gasPrice, result.getGasPrice());
        assertEquals(gasLimit, result.getGasLimit());
        assertEquals(to, result.getTo());
        assertEquals("", result.getData());
        assertEquals(privateFrom, result.getPrivateFrom());
        assertEquals(privateFor, result.getPrivateFor());
        assertEquals(restriction, result.getRestriction());
        assertTrue(result instanceof SignedRawPrivateTransaction);
        final SignedRawPrivateTransaction signedResult = (SignedRawPrivateTransaction) result;
        assertEquals(credentials.getAddress(), signedResult.getFrom());
        signedResult.verify(credentials.getAddress());
        assertEquals(chainId, signedResult.getChainId());
    }
}
