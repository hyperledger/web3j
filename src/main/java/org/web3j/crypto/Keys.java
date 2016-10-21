package org.web3j.crypto;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;

import org.web3j.utils.Numeric;


/**
 * Crypto key utilities.
 */
public class Keys {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private Keys() { }

    /**
     * Create a keypair using SECP-256k1 curve.
     *
     * Private keypairs are encoded using PKCS8
     * Private keys are encoded using X.509
     */
    static KeyPair createSecp256k1KeyPair() throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public static ECKeyPair createEcKeyPair() throws InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = createSecp256k1KeyPair();
        return ECKeyPair.create(keyPair);
    }

    public static String getAddress(ECKeyPair ecKeyPair) {
        BigInteger publicKey = ecKeyPair.getPublicKey();
        return getAddress(Numeric.toHexStringWithPrefix(publicKey));
    }

    public static String getAddress(String publicKey) {
        String hash = Hash.sha3(publicKey);
        return hash.substring(hash.length() - 40);  // right most 160 bits
    }

    public static String getAddress(BigInteger publicKey) {
        return getAddress(Numeric.toHexStringWithPrefix(publicKey));
    }

    public static byte[] getAddress(byte[] publicKey) {
        byte[] hash = Hash.sha3(publicKey);
        return Arrays.copyOfRange(hash, hash.length - 20, hash.length);  // right most 160 bits
    }
}
