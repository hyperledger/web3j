package org.web3j.crypto;

import java.io.File;
import java.io.IOException;

import static org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT;

public class Bip44WalletUtils extends WalletUtils {

    /**
     * Generates a BIP-44 compatible Ethereum wallet on top of BIP-39 generated seed.
     *
     * @param password Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param destinationDirectory The directory containing the wallet
     * @return A BIP-39 compatible Ethereum wallet
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException if the destination cannot be written to
     */
    public static Bip39Wallet generateBip44Wallet(String password, File destinationDirectory)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        SecureRandomUtils.secureRandom().nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = generateBip44KeyPair(masterKeypair);

        String walletFile = generateWalletFile(password, bip44Keypair, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }

    public static Bip32ECKeyPair generateBip44KeyPair(Bip32ECKeyPair master) {
        // m/44'/60'/0'/0
        final int[] path = { 44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0 };
        return Bip32ECKeyPair.deriveKeyPair(master, path);
    }

    public static Credentials loadBip44Credentials(String password, String mnemonic) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = generateBip44KeyPair(masterKeypair);
        return Credentials.create(bip44Keypair);
    }
}
