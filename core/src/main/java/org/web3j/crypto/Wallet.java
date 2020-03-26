/*
 * Copyright 2019 Web3 Labs Ltd.
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.params.KeyParameter;

import org.web3j.utils.Numeric;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.web3j.crypto.SecureRandomUtils.secureRandom;

/**
 * Ethereum wallet file management. For reference, refer to <a
 * href="https://github.com/ethereum/wiki/wiki/Web3-Secret-Storage-Definition">Web3 Secret Storage
 * Definition</a> or the <a
 * href="https://github.com/ethereum/go-ethereum/blob/master/accounts/key_store_passphrase.go">Go
 * Ethereum client implementation</a>.
 *
 * <p><strong>Note:</strong> the Bouncy Castle Scrypt implementation {@link SCrypt}, fails to comply
 * with the following Ethereum reference <a
 * href="https://github.com/ethereum/wiki/wiki/Web3-Secret-Storage-Definition#scrypt">Scrypt test
 * vector</a>:
 *
 * <pre>{@code
 * // Only value of r that cost (as an int) could be exceeded for is 1
 * if (r == 1 && N_STANDARD > 65536)
 * {
 *     throw new IllegalArgumentException("Cost parameter N_STANDARD must be > 1 and < 65536.");
 * }
 * }</pre>
 */
public class Wallet {

    private static final int N_LIGHT = 1 << 12;
    private static final int P_LIGHT = 6;

    private static final int N_STANDARD = 1 << 18;
    private static final int P_STANDARD = 1;

    private static final int R = 8;
    private static final int DKLEN = 32;

    private static final int CURRENT_VERSION = 3;

    private static final String CIPHER = "aes-128-ctr";
    static final String AES_128_CTR = "pbkdf2";
    static final String SCRYPT = "scrypt";

    public static WalletFile create(
            final String password, final ECKeyPair ecKeyPair, final int n, final int p)
            throws CipherException {

        final byte[] salt = generateRandomBytes(32);

        final byte[] derivedKey =
                generateDerivedScryptKey(password.getBytes(UTF_8), salt, n, R, p, DKLEN);

        final byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
        final byte[] iv = generateRandomBytes(16);

        final byte[] privateKeyBytes =
                Numeric.toBytesPadded(ecKeyPair.getPrivateKey(), Keys.PRIVATE_KEY_SIZE);

        final byte[] cipherText =
                performCipherOperation(Cipher.ENCRYPT_MODE, iv, encryptKey, privateKeyBytes);

        final byte[] mac = generateMac(derivedKey, cipherText);

        return createWalletFile(ecKeyPair, cipherText, iv, salt, mac, n, p);
    }

    public static WalletFile createStandard(final String password, final ECKeyPair ecKeyPair)
            throws CipherException {
        return create(password, ecKeyPair, N_STANDARD, P_STANDARD);
    }

    public static WalletFile createLight(final String password, final ECKeyPair ecKeyPair)
            throws CipherException {
        return create(password, ecKeyPair, N_LIGHT, P_LIGHT);
    }

    private static WalletFile createWalletFile(
            final ECKeyPair ecKeyPair,
            final byte[] cipherText,
            final byte[] iv,
            final byte[] salt,
            final byte[] mac,
            final int n,
            final int p) {

        final WalletFile walletFile = new WalletFile();
        walletFile.setAddress(Keys.getAddress(ecKeyPair));

        final WalletFile.Crypto crypto = new WalletFile.Crypto();
        crypto.setCipher(CIPHER);
        crypto.setCiphertext(Numeric.toHexStringNoPrefix(cipherText));

        final WalletFile.CipherParams cipherParams = new WalletFile.CipherParams();
        cipherParams.setIv(Numeric.toHexStringNoPrefix(iv));
        crypto.setCipherparams(cipherParams);

        crypto.setKdf(SCRYPT);
        final WalletFile.ScryptKdfParams kdfParams = new WalletFile.ScryptKdfParams();
        kdfParams.setDklen(DKLEN);
        kdfParams.setN(n);
        kdfParams.setP(p);
        kdfParams.setR(R);
        kdfParams.setSalt(Numeric.toHexStringNoPrefix(salt));
        crypto.setKdfparams(kdfParams);

        crypto.setMac(Numeric.toHexStringNoPrefix(mac));
        walletFile.setCrypto(crypto);
        walletFile.setId(UUID.randomUUID().toString());
        walletFile.setVersion(CURRENT_VERSION);

        return walletFile;
    }

    private static byte[] generateDerivedScryptKey(
            final byte[] password,
            final byte[] salt,
            final int n,
            final int r,
            final int p,
            final int dkLen)
            throws CipherException {
        return SCrypt.generate(password, salt, n, r, p, dkLen);
    }

    private static byte[] generateAes128CtrDerivedKey(
            final byte[] password, final byte[] salt, final int c, final String prf)
            throws CipherException {

        if (!prf.equals("hmac-sha256")) {
            throw new CipherException("Unsupported prf:" + prf);
        }

        // Java 8 supports this, but you have to convert the password to a character array, see
        // http://stackoverflow.com/a/27928435/3211687

        final PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
        gen.init(password, salt, c);
        return ((KeyParameter) gen.generateDerivedParameters(256)).getKey();
    }

    private static byte[] performCipherOperation(
            final int mode, final byte[] iv, final byte[] encryptKey, final byte[] text)
            throws CipherException {

        try {
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

            final SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey, "AES");
            cipher.init(mode, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(text);
        } catch (final NoSuchPaddingException
                | NoSuchAlgorithmException
                | InvalidAlgorithmParameterException
                | InvalidKeyException
                | BadPaddingException
                | IllegalBlockSizeException e) {
            throw new CipherException("Error performing cipher operation", e);
        }
    }

    private static byte[] generateMac(final byte[] derivedKey, final byte[] cipherText) {
        final byte[] result = new byte[16 + cipherText.length];

        System.arraycopy(derivedKey, 16, result, 0, 16);
        System.arraycopy(cipherText, 0, result, 16, cipherText.length);

        return Hash.sha3(result);
    }

    public static ECKeyPair decrypt(final String password, final WalletFile walletFile)
            throws CipherException {

        validate(walletFile);

        final WalletFile.Crypto crypto = walletFile.getCrypto();

        final byte[] mac = Numeric.hexStringToByteArray(crypto.getMac());
        final byte[] iv = Numeric.hexStringToByteArray(crypto.getCipherparams().getIv());
        final byte[] cipherText = Numeric.hexStringToByteArray(crypto.getCiphertext());

        final byte[] derivedKey;

        final WalletFile.KdfParams kdfParams = crypto.getKdfparams();
        if (kdfParams instanceof WalletFile.ScryptKdfParams) {
            final WalletFile.ScryptKdfParams scryptKdfParams =
                    (WalletFile.ScryptKdfParams) crypto.getKdfparams();
            final int dklen = scryptKdfParams.getDklen();
            final int n = scryptKdfParams.getN();
            final int p = scryptKdfParams.getP();
            final int r = scryptKdfParams.getR();
            final byte[] salt = Numeric.hexStringToByteArray(scryptKdfParams.getSalt());
            derivedKey = generateDerivedScryptKey(password.getBytes(UTF_8), salt, n, r, p, dklen);
        } else if (kdfParams instanceof WalletFile.Aes128CtrKdfParams) {
            final WalletFile.Aes128CtrKdfParams aes128CtrKdfParams =
                    (WalletFile.Aes128CtrKdfParams) crypto.getKdfparams();
            final int c = aes128CtrKdfParams.getC();
            final String prf = aes128CtrKdfParams.getPrf();
            final byte[] salt = Numeric.hexStringToByteArray(aes128CtrKdfParams.getSalt());

            derivedKey = generateAes128CtrDerivedKey(password.getBytes(UTF_8), salt, c, prf);
        } else {
            throw new CipherException("Unable to deserialize params: " + crypto.getKdf());
        }

        final byte[] derivedMac = generateMac(derivedKey, cipherText);

        if (!Arrays.equals(derivedMac, mac)) {
            throw new CipherException("Invalid password provided");
        }

        final byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
        final byte[] privateKey =
                performCipherOperation(Cipher.DECRYPT_MODE, iv, encryptKey, cipherText);
        return ECKeyPair.create(privateKey);
    }

    static void validate(final WalletFile walletFile) throws CipherException {
        final WalletFile.Crypto crypto = walletFile.getCrypto();

        if (walletFile.getVersion() != CURRENT_VERSION) {
            throw new CipherException("Wallet version is not supported");
        }

        if (!crypto.getCipher().equals(CIPHER)) {
            throw new CipherException("Wallet cipher is not supported");
        }

        if (!crypto.getKdf().equals(AES_128_CTR) && !crypto.getKdf().equals(SCRYPT)) {
            throw new CipherException("KDF type is not supported");
        }
    }

    static byte[] generateRandomBytes(final int size) {
        final byte[] bytes = new byte[size];
        secureRandom().nextBytes(bytes);
        return bytes;
    }
}
