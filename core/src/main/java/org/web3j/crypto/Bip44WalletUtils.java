/*
 * Copyright 2019 Web3 Labs LTD.
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
        return generateBip44Wallet(password, destinationDirectory, false);
    }

    /**
     * Generates a BIP-44 compatible Ethereum wallet on top of BIP-39 generated seed.
     *
     * @param password Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param destinationDirectory The directory containing the wallet
     * @param testNet should use the testNet derive path
     * @return A BIP-39 compatible Ethereum wallet
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException if the destination cannot be written to
     */
    public static Bip39Wallet generateBip44Wallet(
            String password, File destinationDirectory, boolean testNet)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        SecureRandomUtils.secureRandom().nextBytes(initialEntropy);

        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);

        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = generateBip44KeyPair(masterKeypair, testNet);

        String walletFile = generateWalletFile(password, bip44Keypair, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }

    public static Bip32ECKeyPair generateBip44KeyPair(Bip32ECKeyPair master) {
        return generateBip44KeyPair(master, false);
    }

    public static Bip32ECKeyPair generateBip44KeyPair(Bip32ECKeyPair master, boolean testNet) {
        if (testNet) {
            // /m/44'/0'/0
            final int[] path = {44 | HARDENED_BIT, 0 | HARDENED_BIT, 0 | HARDENED_BIT, 0};
            return Bip32ECKeyPair.deriveKeyPair(master, path);
        } else {
            // m/44'/60'/0'/0
            final int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0};
            return Bip32ECKeyPair.deriveKeyPair(master, path);
        }
    }

    public static Credentials loadBip44Credentials(String password, String mnemonic) {
        return loadBip44Credentials(password, mnemonic, false);
    }

    public static Credentials loadBip44Credentials(
            String password, String mnemonic, boolean testNet) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = generateBip44KeyPair(masterKeypair, testNet);
        return Credentials.create(bip44Keypair);
    }
}
