package org.web3j.protocol.eea.crypto;

import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.eea.request.PrivateTransaction;

import static org.junit.Assert.assertThat;
import static org.web3j.utils.Numeric.toHexString;

public class PrivateTransactionEncoderTest {

    @Test
    public void testSignMessage() {
        String expected = "";
        String privateKey = "";
        Credentials credentials = Credentials.create(privateKey);
        PrivateTransaction privateTransaction = new PrivateTransaction();
        String privateRawTransaction = toHexString(PrivateTransactionEncoder.signMessage(privateTransaction, chainId, credentials));

        assertThat(privateRawTransaction, expected);
    }
}
