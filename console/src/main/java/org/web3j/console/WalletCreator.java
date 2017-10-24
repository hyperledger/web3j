package org.web3j.console;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.web3j.codegen.Console;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;

/**
 * Simple class for creating a wallet file.
 */
public class WalletCreator extends WalletManager {

    public WalletCreator() {
    }

    public WalletCreator(IODevice console) {
        super(console);
    }

    public static void main(String[] args) {
        new WalletCreator().run();
    }

    static void main(IODevice console) {
        new WalletCreator(console).run();
    }

    private void run() {
        String password = getPassword("Please enter a wallet file password: ");
        String destinationDir = getDestinationDir();
        File destination = createDir(destinationDir);

        try {
            String walletFileName = WalletUtils.generateFullNewWalletFile(password, destination);
            console.printf("Wallet file " + walletFileName
                    + " successfully created in: " + destinationDir + "\n");
        } catch (CipherException e) {
            Console.exitError(e);
        } catch (IOException e) {
            Console.exitError(e);
        } catch (InvalidAlgorithmParameterException e) {
            Console.exitError(e);
        } catch (NoSuchAlgorithmException e) {
            Console.exitError(e);
        } catch (NoSuchProviderException e) {
            Console.exitError(e);
        }
    }
}
