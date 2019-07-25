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
package org.web3j.console;

import java.io.File;
import java.io.IOException;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import static org.web3j.codegen.Console.exitError;

/** Simple class for creating a wallet file. */
public class WalletUpdater extends WalletManager {

    public WalletUpdater() {}

    public WalletUpdater(IODevice console) {
        super(console);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            exitError("You must provide an existing wallet file");
        } else {
            new WalletUpdater().run(args[0]);
        }
    }

    public static void main(IODevice console, String walletFileLocation) {
        new WalletUpdater(console).run(walletFileLocation);
    }

    private void run(String walletFileLocation) {
        File walletFile = new File(walletFileLocation);
        Credentials credentials = getCredentials(walletFile);

        console.printf("Wallet for address " + credentials.getAddress() + " loaded\n");

        String newPassword = getPassword("Please enter a new wallet file password: ");

        String destinationDir = getDestinationDir();
        File destination = createDir(destinationDir);

        try {
            String walletFileName =
                    WalletUtils.generateWalletFile(
                            newPassword, credentials.getEcKeyPair(), destination, true);
            console.printf(
                    "New wallet file "
                            + walletFileName
                            + " successfully created in: "
                            + destinationDir
                            + "\n");
        } catch (CipherException | IOException e) {
            exitError(e);
        }

        String delete =
                console.readLine("Would you like to delete your existing wallet file (Y/N)? [N]: ");
        if (delete.toUpperCase().equals("Y")) {
            if (!walletFile.delete()) {
                exitError("Unable to remove wallet file\n");
            } else {
                console.printf("Deleted previous wallet file: %s\n", walletFile.getName());
            }
        }
    }
}
