/*
 * Copyright 2023 Web3 Labs Ltd.
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

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.web3j.crypto.Secp256k1JWK.isEcCoordBase64Valid;

public class Secp256k1JWKTest {

    private static final String CRV = "secp256k1";
    private static final String KTY = "EC";

    @Test
    public void testPublicJWKCreation()
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException,
                    NoSuchProviderException {
        KeyPair keyPair = Keys.createSecp256k1KeyPair();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        Secp256k1JWK jwk = new Secp256k1JWK.Builder(publicKey).build();
        assertPublicJWK(jwk);
        assertNull(jwk.getD());
    }

    @Test
    public void testPrivateJWKCreation()
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException,
                    NoSuchProviderException {
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
