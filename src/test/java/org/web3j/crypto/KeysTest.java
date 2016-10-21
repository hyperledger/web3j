package org.web3j.crypto;


import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Test;

import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class KeysTest {

    @Test
    public void testCreateSecp256k1KeyPair() throws Exception {
        KeyPair keyPair = Keys.createSecp256k1KeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertNotNull(privateKey);
        assertNotNull(publicKey);

        assertThat(privateKey.getEncoded().length, is(144));
        assertThat(publicKey.getEncoded().length, is(88));
    }

    @Test
    public void testCreateEcKeyPair() throws Exception {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        assertThat(ecKeyPair.getPublicKey().signum(), is(1));
        assertThat(ecKeyPair.getPrivateKey().signum(), is(1));
    }

    @Test
    public void testGetAddressString() {
        String publicKey = "0x54c8cda130d3bfda86bd698cee738e5e502abc1fcb9e45709ee1fe38e855cda3" +
                "34ca6f9288ab6d867f6baa2b2afeced0478e6a7225a5b1bb263ab21611817507";
        assertThat(Keys.getAddress(publicKey), is("9c98e381edc5fe1ac514935f3cc3edaa764cf004"));
    }

    @Test
    public void testGetAddressBigInteger() {
        BigInteger publicKey = Numeric.toBigInt("0x54c8cda130d3bfda86bd698cee738e5e502abc1fcb9e45709ee1fe38e855cda3" +
                "34ca6f9288ab6d867f6baa2b2afeced0478e6a7225a5b1bb263ab21611817507");
        assertThat(Keys.getAddress(publicKey), is("9c98e381edc5fe1ac514935f3cc3edaa764cf004"));
    }
}
