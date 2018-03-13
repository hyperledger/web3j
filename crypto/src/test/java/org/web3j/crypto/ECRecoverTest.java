package org.web3j.crypto;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ECRecoverTest {

    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    @Test
    public void testRecoverAddressFromSignature() {
        //CHECKSTYLE:OFF
        String signature = "0x2c6401216c9031b9a6fb8cbfccab4fcec6c951cdf40e2320108d1856eb532250576865fbcd452bcdc4c57321b619ed7a9cfd38bd973c3e1e0243ac2777fe9d5b1b";
        //CHECKSTYLE:ON
        String address = "0x31b26e43651e9371c88af3d36c14cfd938baf4fd";
        String message = "v0G9u7huK4mJb2K1";
                
                
        // Message
        String prefix = PERSONAL_MESSAGE_PREFIX + message.length();
        byte[] msgHash = Hash.sha3((prefix + message).getBytes());


        // Signature
        byte[] array = Numeric.hexStringToByteArray(signature);
        byte v = array[64];
        if (v < 27) { 
            v += 27; 
        }
           
        SignatureData sd = new SignatureData(
                v, 
                (byte[]) Arrays.copyOfRange(array, 0, 32), 
                (byte[])  Arrays.copyOfRange(array, 32, 64));

        String addressRecovered = null;
        boolean match = false;
        
        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey = Sign.recoverFromSignature(
                    (byte)i, 
                    new ECDSASignature(new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())), 
                    msgHash);
               
            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey); 
                
                if (addressRecovered.equals(address)) {
                    match = true;
                    break;
                }
            }
        }
        
        assertThat(addressRecovered, is(address));
        assertThat(match, is(true));
    }
}
