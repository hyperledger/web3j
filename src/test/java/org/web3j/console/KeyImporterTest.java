package org.web3j.console;


import org.junit.Test;

import org.web3j.crypto.SampleKeys;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeyImporterTest extends WalletTester {

    @Test
    public void testSpecifyPrivateKey() {
        prepareWalletCreation(SampleKeys.PRIVATE_KEY_STRING);
    }

    @Test
    public void testLoadPrivateKeyFromFile() {
        prepareWalletCreation(KeyImporterTest.class.getResource("/keyfiles/" +
                "sample-private-key.txt").getFile());
    }

    private void prepareWalletCreation(String input) {
        when(console.readLine(startsWith("Please enter the hex encoded private key")))
                .thenReturn(input);
        when(console.readPassword(contains("password"))).thenReturn(WALLET_PASSWORD, WALLET_PASSWORD);
        when(console.readLine(contains("Please enter a destination directory location"))).thenReturn(tempDirPath);

        KeyImporter.main(console);

        verify(console).printf(contains("successfully created in"));
    }
}
