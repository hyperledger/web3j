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

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.web3j.crypto.Bip32Test.addChecksum;
import static org.web3j.crypto.Bip32Test.serializePrivate;
import static org.web3j.crypto.Bip32Test.serializePublic;
import static org.web3j.crypto.SampleKeys.PASSWORD;
import static org.web3j.crypto.WalletUtilsTest.createTempDir;

public class Bip44WalletUtilsTest {

    private File tempDir;

    @BeforeEach
    public void setUp() throws Exception {
        tempDir = createTempDir();
    }

    @AfterEach
    public void tearDown() throws Exception {
        for (final File file : tempDir.listFiles()) {
            file.delete();
        }
        tempDir.delete();
    }

    @Test
    public void generateBip44KeyPair() {
        final String mnemonic =
                "spider elbow fossil truck deal circle divert sleep safe report laundry above";
        final byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        final String seedStr = bytesToHex(seed);
        assertEquals(
                "f0d2ab78b96acd147119abad1cd70eb4fec4f0e0a95744cf532e6a09347b08101213b4cbf50eada0eb89cba444525fe28e69707e52aa301c6b47ce1c5ef82eb5",
                seedStr);

        final Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        assertEquals(
                "xprv9s21ZrQH143K2yA9Cdad5gjqHRC7apVUgEyYq5jXeXigDZ3PfEnps44tJprtMXr7PZivEsin6Qrbad7PuiEy4tn5jAEK6A3U46f9KvfRCmD",
                Base58.encode(addChecksum(serializePrivate(masterKeypair))));

        final Bip32ECKeyPair bip44Keypair = Bip44WalletUtils.generateBip44KeyPair(masterKeypair);

        assertEquals(
                "xprvA3p5nTrBJcdEvUQAK64rZ4oJTwsTiMg7JQrqNh6JNWe3VUW2tcLb7GW1wj1fNDAoymUTSFERZ2LxPxJNmqoMZPs9y3TMNMuBN8MS9eigoWq",
                Base58.encode(addChecksum(serializePrivate(bip44Keypair))));
        assertEquals(
                "xpub6GoSByP58zBY8xUdR7brvCk31yhx7pPxfdnSB5VuvrB2NGqBS9eqf4pVo1xev4GEmip5Wuky9KUtJVxq4fvYfFchS6SA6C4cCRyQkLqNNjq",
                Base58.encode(addChecksum(serializePublic(bip44Keypair))));

        Credentials credentials =
                Bip44WalletUtils.loadBip44Credentials(
                        "", mnemonic); // Verify address according to https://iancoleman.io/bip39/
        assertEquals(
                "0xece62451ca8fba33746d6dafd0d0ebdef84778b7",
                credentials.getAddress().toLowerCase());
    }

    @Test
    public void generateBip44KeyPairTestNet() {
        final String mnemonic =
                "spider elbow fossil truck deal circle divert sleep safe report laundry above";
        final byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        final String seedStr = bytesToHex(seed);
        assertEquals(
                "f0d2ab78b96acd147119abad1cd70eb4fec4f0e0a95744cf532e6a09347b08101213b4cbf50eada0eb89cba444525fe28e69707e52aa301c6b47ce1c5ef82eb5",
                seedStr);

        final Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        assertEquals(
                "xprv9s21ZrQH143K2yA9Cdad5gjqHRC7apVUgEyYq5jXeXigDZ3PfEnps44tJprtMXr7PZivEsin6Qrbad7PuiEy4tn5jAEK6A3U46f9KvfRCmD",
                Base58.encode(addChecksum(serializePrivate(masterKeypair))));

        final Bip32ECKeyPair bip44Keypair =
                Bip44WalletUtils.generateBip44KeyPair(masterKeypair, true);

        assertEquals(
                "xprv9zhLxq63By3SX5hAMKnxjGy7L18bnn7GzDQv53eYYqeRX9M82riC1dqovamttwFpk2ZkDQxgcikBQzs1DTu2KShJJqnqgx83EftUB3k39uc",
                Base58.encode(addChecksum(serializePrivate(bip44Keypair))));
        assertEquals(
                "xpub6DghNLcw2LbjjZmdTMKy6Quqt2y6CEq8MSLWsS4A7BBQPwgGaQ2SZSAHmsrqBVxLegjW2mBfcvDBhpeEqCmucTTPJiNLHQkiDuKwHs9gEtk",
                Base58.encode(addChecksum(serializePublic(bip44Keypair))));
    }

    @Test
    public void testGenerateBip44Wallets() throws Exception {
        final Bip39Wallet wallet = Bip44WalletUtils.generateBip44Wallet(PASSWORD, tempDir);
        final byte[] seed = MnemonicUtils.generateSeed(wallet.getMnemonic(), PASSWORD);
        final Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        final Bip32ECKeyPair bip44Keypair = Bip44WalletUtils.generateBip44KeyPair(masterKeypair);
        final Credentials credentials = Credentials.create(bip44Keypair);

        assertEquals(
                credentials, Bip44WalletUtils.loadBip44Credentials(PASSWORD, wallet.getMnemonic()));
    }

    private String bytesToHex(final byte[] bytes) {
        final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

        final char[] chars = new char[2 * bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            chars[2 * i] = HEX_CHARS[(bytes[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[bytes[i] & 0x0F];
        }
        return new String(chars);
    }
}
