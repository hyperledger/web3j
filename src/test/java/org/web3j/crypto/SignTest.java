package org.web3j.crypto;


import java.math.BigInteger;
import java.security.SignatureException;

import org.junit.Test;

import org.web3j.utils.Hex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class SignTest {

    private static final byte[] TEST_MESSAGE = "A test message".getBytes();

    @Test
    public void testSignMessage() {
        Sign.SignatureData signatureData = Sign.signMessage(TEST_MESSAGE, SampleKeys.KEY_PAIR);

        Sign.SignatureData expected = new Sign.SignatureData(
                (byte) 28,
                Hex.hexStringToByteArray(
                        "0xde7a0b9fa2b476d6fa0e6c67f291473ea9dc09a5abaf8d163742bac01c637d16"),
                Hex.hexStringToByteArray(
                        "0x744797b826dfd4a6f5549a4f8f693fda3a1c10421220471119574dd2f31441f")
        );

        assertThat(signatureData, is(expected));
    }

    @Test
    public void testSignedMessageToKey() throws SignatureException {
        Sign.SignatureData signatureData = Sign.signMessage(TEST_MESSAGE, SampleKeys.KEY_PAIR);
        BigInteger key = Sign.signedMessageToKey(TEST_MESSAGE, signatureData);
        assertThat(key, equalTo(SampleKeys.PUBLIC_KEY));
    }

    @Test
    public void testPublicKeyFromPrivateKey() {
        assertThat(Sign.publicKeyFromPrivate(SampleKeys.PRIVATE_KEY),
                equalTo(SampleKeys.PUBLIC_KEY));
    }
}
