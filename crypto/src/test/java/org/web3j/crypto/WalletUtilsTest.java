package org.web3j.crypto;

import java.io.File;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.web3j.utils.Numeric;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.web3j.crypto.Hash.sha256;
import static org.web3j.crypto.SampleKeys.CREDENTIALS;
import static org.web3j.crypto.SampleKeys.KEY_PAIR;
import static org.web3j.crypto.SampleKeys.PASSWORD;
import static org.web3j.crypto.WalletUtils.isValidAddress;
import static org.web3j.crypto.WalletUtils.isValidPrivateKey;

public class WalletUtilsTest {

    private File tempDir;

    @Before
    public void setUp() throws Exception {
        tempDir = createTempDir();
    }

    @After
    public void tearDown() throws Exception {
        for (File file:tempDir.listFiles()) {
            file.delete();
        }
        tempDir.delete();
    }

    @Test
    public void testGenerateBip39Wallets() throws Exception {
        Bip39Wallet wallet = WalletUtils.generateBip39Wallet(PASSWORD, tempDir);
        byte[] seed = MnemonicUtils.generateSeed(wallet.getMnemonic(), PASSWORD);
        Credentials credentials = Credentials.create(ECKeyPair.create(sha256(seed)));

        assertEquals(credentials, WalletUtils.loadBip39Credentials(PASSWORD, wallet.getMnemonic()));
    }

    @Test
    public void testGenerateFullNewWalletFile() throws Exception {
        String fileName = WalletUtils.generateFullNewWalletFile(PASSWORD, tempDir);
        testGeneratedNewWalletFile(fileName);
    }

    @Test
    public void testGenerateNewWalletFile() throws Exception {
        String fileName = WalletUtils.generateLightNewWalletFile(PASSWORD, tempDir);
        testGeneratedNewWalletFile(fileName);
    }


    @Test
    public void testGenerateLightNewWalletFile() throws Exception {
        String fileName = WalletUtils.generateLightNewWalletFile(PASSWORD, tempDir);
        testGeneratedNewWalletFile(fileName);
    }

    private void testGeneratedNewWalletFile(String fileName) throws Exception {
        WalletUtils.loadCredentials(PASSWORD, new File(tempDir, fileName));
    }

    @Test
    public void testGenerateFullWalletFile() throws Exception {
        String fileName = WalletUtils.generateWalletFile(PASSWORD, KEY_PAIR, tempDir, true);
        testGenerateWalletFile(fileName);
    }

    @Test
    public void testGenerateLightWalletFile() throws Exception {
        String fileName = WalletUtils.generateWalletFile(PASSWORD, KEY_PAIR, tempDir, false);
        testGenerateWalletFile(fileName);
    }

    private void testGenerateWalletFile(String fileName) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(
                PASSWORD, new File(tempDir, fileName));

        assertThat(credentials, equalTo(CREDENTIALS));
    }

    @Test
    public void testLoadCredentialsFromFile() throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(
                PASSWORD,
                new File(WalletUtilsTest.class.getResource(
                        "/keyfiles/"
                                + "UTC--2016-11-03T05-55-06."
                                + "340672473Z--ef678007d18427e6022059dbc264f27507cd1ffc")
                        .getFile()));

        assertThat(credentials, equalTo(CREDENTIALS));
    }

    @Test
    public void testLoadCredentialsFromString() throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(
                PASSWORD,
                WalletUtilsTest.class.getResource(
                        "/keyfiles/"
                        + "UTC--2016-11-03T05-55-06."
                        + "340672473Z--ef678007d18427e6022059dbc264f27507cd1ffc").getFile());

        assertThat(credentials, equalTo(CREDENTIALS));
    }

    @Ignore  // enable if users need to work with MyEtherWallet
    @Test
    public void testLoadCredentialsMyEtherWallet() throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(
                PASSWORD,
                new File(WalletUtilsTest.class.getResource(
                        "/keyfiles/"
                        + "UTC--2016-11-03T07-47-45."
                        + "988Z--4f9c1a1efaa7d81ba1cabf07f2c3a5ac5cf4f818").getFile()));

        assertThat(credentials, equalTo(
                Credentials.create(
                        "6ca4203d715e693279d6cd9742ad2fb7a3f6f4abe27a64da92e0a70ae5d859c9")));
    }

    @Test
    public void testGetDefaultKeyDirectory() {
        assertTrue(WalletUtils.getDefaultKeyDirectory("Mac OS X")
                .endsWith(String.format("%sLibrary%sEthereum", File.separator, File.separator)));
        assertTrue(WalletUtils.getDefaultKeyDirectory("Windows")
                .endsWith(String.format("%sEthereum", File.separator)));
        assertTrue(WalletUtils.getDefaultKeyDirectory("Linux")
                .endsWith(String.format("%s.ethereum", File.separator)));
    }

    @Test
    public void testGetTestnetKeyDirectory() {
        assertTrue(WalletUtils.getMainnetKeyDirectory()
                .endsWith(String.format("%skeystore", File.separator)));
        assertTrue(WalletUtils.getTestnetKeyDirectory()
                .endsWith(String.format("%stestnet%skeystore", File.separator, File.separator)));
    }

    private static File createTempDir() throws Exception {
        return Files.createTempDirectory(
                WalletUtilsTest.class.getSimpleName() + "-testkeys").toFile();
    }

    @Test
    public void testIsValidPrivateKey() {
        assertTrue(isValidPrivateKey(SampleKeys.PRIVATE_KEY_STRING));
        assertTrue(isValidPrivateKey(Numeric.prependHexPrefix(SampleKeys.PRIVATE_KEY_STRING)));

        assertFalse(isValidPrivateKey(""));
        assertFalse(isValidPrivateKey(SampleKeys.PRIVATE_KEY_STRING + "a"));
        assertFalse(isValidPrivateKey(SampleKeys.PRIVATE_KEY_STRING.substring(1)));
    }

    @Test
    public void testIsValidAddress() {
        assertTrue(isValidAddress(SampleKeys.ADDRESS));
        assertTrue(isValidAddress(SampleKeys.ADDRESS_NO_PREFIX));

        assertFalse(isValidAddress(""));
        assertFalse(isValidAddress(SampleKeys.ADDRESS + 'a'));
        assertFalse(isValidAddress(SampleKeys.ADDRESS.substring(1)));
    }
}
