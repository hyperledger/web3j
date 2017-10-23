package org.web3j.console;

import org.junit.Test;

import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WalletUpdaterTest extends WalletTester {

    @Test
    public void testWalletUpdate() {
        when(console.readPassword(startsWith("Please enter your existing wallet file password")))
                .thenReturn(WALLET_PASSWORD);

        when(console.readPassword(contains("password")))
                .thenReturn(WALLET_PASSWORD, WALLET_PASSWORD);
        when(console.readLine(startsWith("Please enter a destination directory ")))
                .thenReturn(tempDirPath);
        when(console.readLine(startsWith("Would you like to delete")))
                .thenReturn("N");

        WalletUpdater.main(console, KeyImporterTest.class.getResource("/keyfiles/"
                + "UTC--2016-11-03T05-55-06.340672473Z--ef678007d18427e6022059dbc264f27507cd1ffc")
                .getFile());

        verify(console).printf(contains("successfully created in"));
    }
}
