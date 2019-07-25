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
import org.web3j.utils.Files;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.crypto.Keys.PRIVATE_KEY_LENGTH_IN_HEX;

/** Create Ethereum wallet file from a provided private key. */
public class KeyImporter extends WalletManager {

    public KeyImporter() {}

    public KeyImporter(IODevice console) {
        super(console);
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            new KeyImporter().run(args[0]);
        } else {
            new KeyImporter().run();
        }
    }

    static void main(IODevice console) {
        new KeyImporter(console).run();
    }

    private void run(String input) {
        File keyFile = new File(input);

        if (keyFile.isFile()) {
            String privateKey = null;
            try {
                privateKey = Files.readString(keyFile);
            } catch (IOException e) {
                exitError("Unable to read file " + input);
            }

            createWalletFile(privateKey.trim());
        } else {
            createWalletFile(input.trim());
        }
    }

    private void run() {
        String input =
                console.readLine("Please enter the hex encoded private key or key file location: ")
                        .trim();
        run(input);
    }

    private void createWalletFile(String privateKey) {
        if (!WalletUtils.isValidPrivateKey(privateKey)) {
            exitError(
                    "Invalid private key specified, must be "
                            + PRIVATE_KEY_LENGTH_IN_HEX
                            + " digit hex value");
        }

        Credentials credentials = Credentials.create(privateKey);
        String password = getPassword("Please enter a wallet file password: ");

        String destinationDir = getDestinationDir();
        File destination = createDir(destinationDir);

        try {
            String walletFileName =
                    WalletUtils.generateWalletFile(
                            password, credentials.getEcKeyPair(), destination, true);
            console.printf(
                    "Wallet file "
                            + walletFileName
                            + " successfully created in: "
                            + destinationDir
                            + "\n");
        } catch (CipherException | IOException e) {
            exitError(e);
        }
    }
}
