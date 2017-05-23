package org.web3j.console;

import org.junit.Test;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WalletCreatorTest extends WalletTester {

    @Test
    public void testWalletCreation() {
        when(console.readPassword(contains("password")))
                .thenReturn(WALLET_PASSWORD, WALLET_PASSWORD);
        when(console.readLine(startsWith("Please enter a destination directory ")))
                .thenReturn(tempDirPath);

        WalletCreator.main(console);

        verify(console).printf(contains("successfully created in"));
    }
}
