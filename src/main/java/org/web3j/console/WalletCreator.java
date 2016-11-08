package org.web3j.console;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;

/**
 * Simple class for creating a wallet file.
 */
public class WalletCreator extends WalletManager {

    public static void main(String[] args) {
        new WalletCreator().run();
    }

    private void run() {
        String password = getPassword("Please enter a wallet file password: ");
        String destinationDir = getDestinationDir();
        File destination = createDir(destinationDir);

        try {
            String walletFileName = WalletUtils.generateNewWalletFile(password, destination);
            console.printf("Wallet file " + walletFileName +
                    " successfully created in: " + destinationDir + "\n");
        } catch (CipherException|IOException|InvalidAlgorithmParameterException|
                NoSuchAlgorithmException|NoSuchProviderException e) {
            exitError(e);
        }
    }
}
