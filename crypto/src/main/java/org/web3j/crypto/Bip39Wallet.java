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

/** Data class encapsulating a BIP-39 compatible Ethereum wallet. */
public class Bip39Wallet {

    /** Path to wallet file. */
    private final String filename;

    /** Generated BIP-39 mnemonic for the wallet. */
    private final String mnemonic;

    public Bip39Wallet(final String filename, final String mnemonic) {
        this.filename = filename;
        this.mnemonic = mnemonic;
    }

    public String getFilename() {
        return filename;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    @Override
    public String toString() {
        return "Bip39Wallet{"
                + "filename='"
                + filename
                + '\''
                + ", mnemonic='"
                + mnemonic
                + '\''
                + '}';
    }
}
