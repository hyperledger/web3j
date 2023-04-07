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
package org.web3j.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;

import org.web3j.crypto.Keys;
import org.web3j.crypto.Secp256k1JWK;

public class EcJWK {

    private static final String kty = "EC";
    private static final String crv = "secp256k1";
    private Base64String x;
    private Base64String y;
    private Base64String d;

    public static void main(String[] args)
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException,
                    NoSuchProviderException, JsonProcessingException {
        KeyPair keyPair = Keys.createSecp256k1KeyPair();
        firstKeys(keyPair);
        secondKeys(keyPair);
    }

    private static void secondKeys(KeyPair keyPair) throws JsonProcessingException {
        ECPublicKey pub = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey priv = (ECPrivateKey) keyPair.getPrivate();
        Secp256k1JWK jwk = new Secp256k1JWK.Builder(pub).setPrivateKey(priv).build();
        ObjectMapper mapper = new JsonMapper();
        System.out.println(mapper.writeValueAsString(jwk));
    }

    private static void firstKeys(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        ECKey ecJWK =
                new ECKey.Builder(Curve.SECP256K1, (ECPublicKey) publicKey)
                        .privateKey((ECPrivateKey) privateKey)
                        .build();
        String jwkJson = ecJWK.toJSONString();

        System.out.println(jwkJson);
    }
}
