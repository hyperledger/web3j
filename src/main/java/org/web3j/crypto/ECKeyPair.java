package org.web3j.crypto;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Arrays;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import org.web3j.utils.Numeric;

/**
 * Elliptic Curve SECP-256k1 generated key pair.
 */
public class ECKeyPair {
    private final BigInteger privateKey;
    private final BigInteger publicKey;

    public ECKeyPair(BigInteger privateKey, BigInteger publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }


    public static ECKeyPair create(KeyPair keyPair) {
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        BigInteger privateKeyValue = privateKey.getD();

        // Ethereum does not use encoded public keys like bitcoin - see
        // https://en.bitcoin.it/wiki/Elliptic_Curve_Digital_Signature_Algorithm for details
        // Additionally, as the first bit is a constant prefix (0x04) we ignore this value
        byte[] publicKeyBytes = publicKey.getQ().getEncoded(false);
        BigInteger publicKeyValue =
                new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.length));

        return new ECKeyPair(privateKeyValue, publicKeyValue);
    }

    public static ECKeyPair create(BigInteger privateKey) {
        return new ECKeyPair(privateKey, Sign.publicKeyFromPrivate(privateKey));
    }

    public static ECKeyPair create(byte[] privateKey) {
        return create(Numeric.toBigInt(privateKey));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ECKeyPair ecKeyPair = (ECKeyPair) o;

        if (privateKey != null ? !privateKey.equals(ecKeyPair.privateKey) : ecKeyPair.privateKey != null)
            return false;
        return publicKey != null ? publicKey.equals(ecKeyPair.publicKey) : ecKeyPair.publicKey == null;

    }

    @Override
    public int hashCode() {
        int result = privateKey != null ? privateKey.hashCode() : 0;
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        return result;
    }
}
