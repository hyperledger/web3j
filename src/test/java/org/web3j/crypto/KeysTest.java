package org.web3j.crypto;


import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import org.junit.Test;

import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class KeysTest {

    private static final byte[] ENCODED;

    static {
        byte[] privateKey = Numeric.hexStringToByteArray(SampleKeys.PRIVATE_KEY_STRING);
        byte[] publicKey = Numeric.hexStringToByteArray(SampleKeys.PUBLIC_KEY_STRING);
        ENCODED = Arrays.copyOf(privateKey, privateKey.length + publicKey.length);
        System.arraycopy(publicKey, 0, ENCODED, privateKey.length, publicKey.length);
    }

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
        String publicKey = SampleKeys.PUBLIC_KEY_STRING;
        assertThat(Keys.getAddress(publicKey), is(SampleKeys.ADDRESS));
    }

    @Test
    public void testGetAddressBigInteger() {
        assertThat(Keys.getAddress(SampleKeys.PUBLIC_KEY), is(SampleKeys.ADDRESS));
    }

    @Test
    public void testSerializeECKey() {
        assertThat(Keys.serialize(SampleKeys.KEY_PAIR), is(ENCODED));
    }

    @Test
    public void testDeserializeECKey() {
        assertThat(Keys.deserialize(ENCODED), is(SampleKeys.KEY_PAIR));
    }

    @Test(expected = RuntimeException.class)
    public void testDeserializeInvalidKey() {
        Keys.deserialize(new byte[0]);
    }
}
