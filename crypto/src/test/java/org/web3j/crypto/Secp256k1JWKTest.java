package org.web3j.crypto;

import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.web3j.crypto.Secp256k1JWK.isEcCoordBase64Valid;

public class Secp256k1JWKTest {

    private final static String CRV = "secp256k1";
    private final static String KTY = "EC";

    @Test
    public void testPublicJWKCreation() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = Keys.createSecp256k1KeyPair();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        Secp256k1JWK jwk = new Secp256k1JWK.Builder(publicKey).build();
        assertPublicJWK(jwk);
        assertNull(jwk.getD());
    }

    @Test
    public void testPrivateJWKCreation() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = Keys.createSecp256k1KeyPair();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        Secp256k1JWK jwk = new Secp256k1JWK.Builder(publicKey).setPrivateKey(privateKey).build();
        assertPublicJWK(jwk);
        assertTrue(isEcCoordBase64Valid(jwk.getD()));
    }

    private void assertPublicJWK(Secp256k1JWK jwk) {
        assertEquals(CRV, jwk.getCrv());
        assertEquals(KTY, jwk.getKty());
        assertTrue(isEcCoordBase64Valid(jwk.getX()));
        assertTrue(isEcCoordBase64Valid(jwk.getY()));
    }
}
