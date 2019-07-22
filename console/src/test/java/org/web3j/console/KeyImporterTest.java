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
        prepareWalletCreation(
                KeyImporterTest.class
                        .getResource("/keyfiles/" + "sample-private-key.txt")
                        .getFile());
    }

    private void prepareWalletCreation(String input) {
        when(console.readLine(startsWith("Please enter the hex encoded private key")))
                .thenReturn(input);
        when(console.readPassword(contains("password")))
                .thenReturn(WALLET_PASSWORD, WALLET_PASSWORD);
        when(console.readLine(contains("Please enter a destination directory location")))
                .thenReturn(tempDirPath);

        KeyImporter.main(console);

        verify(console).printf(contains("successfully created in"));
    }
}
