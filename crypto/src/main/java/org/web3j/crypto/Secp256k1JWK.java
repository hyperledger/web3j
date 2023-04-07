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

import java.math.BigInteger;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

import net.jcip.annotations.Immutable;

@Immutable
public final class Secp256k1JWK {

    private static final String kty = "EC";
    private static final String crv = "secp256k1";

    private String x;
    private String y;
    private String d;

    public Secp256k1JWK(String xCoordinate, String yCoordinate) {
        x = xCoordinate;
        y = yCoordinate;
    }

    public Secp256k1JWK(String xCoordinate, String yCoordinate, String dCoordinate) {
        this(xCoordinate, yCoordinate);
        d = dCoordinate;
    }

    public String getKty() {
        return kty;
    }

    public String getCrv() {
        return crv;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getD() {
        return d;
    }

    public static class Builder {
        private String x;
        private String y;
        private String d;

        public Builder(ECPublicKey key) {
            setPublicKey(key);
        }

        public Builder d(final String d) {
            if (!isEcCoordBase64Valid(d)) {
                throw new IllegalArgumentException("Specified d jwk is not a valid base 64 value");
            }
            this.d = d;
            return this;
        }

        public Builder x(final String x) {
            if (!isEcCoordBase64Valid(x)) {
                throw new IllegalArgumentException("Specified x jwk is not a valid base 64 value");
            }
            this.x = x;
            return this;
        }

        public Builder y(final String y) {
            if (!isEcCoordBase64Valid(y)) {
                throw new IllegalArgumentException("Specified y jwk is not a valid base 64 value");
            }
            this.y = y;
            return this;
        }

        public Builder setPrivateKey(final ECPrivateKey pk) {
            this.d = encodeCoordinate(pk.getS());
            return this;
        }

        public Builder setPublicKey(final ECPublicKey pk) {
            this.x = encodeCoordinate(pk.getW().getAffineX());
            this.y = encodeCoordinate(pk.getW().getAffineY());
            return this;
        }

        private String encodeCoordinate(BigInteger coordinate) {
            final byte[] bytes = coordinate.toByteArray();

            // check if it is unsigned
            if (bytes[0] == 0) {
                // remove leading zero
                final byte[] unsignedBytes = new byte[bytes.length - 1];
                System.arraycopy(bytes, 1, unsignedBytes, 0, unsignedBytes.length);
                return encodeEcToBase64(unsignedBytes);
            }
            return encodeEcToBase64(bytes);
        }

        private String encodeEcToBase64(byte[] bytes) {
            String paddedEncoding = Base64.getEncoder().encodeToString(bytes);

            // Format to standard
            paddedEncoding = paddedEncoding.replaceAll("[+]", "-").replaceAll("[/]", "_");

            // By default, secp256k1 curve will give 32 bytes long coordinates, % 3 on its length =
            // 2, so encoding them to base 64 will append one '=' as padded value. This padding
            // needs to be removed before adding it to JWK.
            return paddedEncoding.substring(0, paddedEncoding.length() - 1);
        }

        public Secp256k1JWK build() {
            if (d == null) {
                // public JWK
                return new Secp256k1JWK(x, y);
            }
            // private JWK
            return new Secp256k1JWK(x, y, d);
        }
    }

    /**
     * Returns true if the string is empty or contains only white space codepoints, otherwise false.
     * This is specific only for secp256k1 curve encoded coordinates.
     *
     * @param s String value
     * @return is given string is Blank or not
     */
    public static boolean isEcCoordBase64Valid(String s) {
        return s.matches("(?:[A-Za-z0-9-_]{4})*(?:[A-Za-z0-9-_]{2}|[A-Za-z0-9-_]{3})");
    }
}
