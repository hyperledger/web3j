package org.web3j.crypto;

public class Bip39Wallet {

    private final String filename;
    private final String mnemonic;

    public Bip39Wallet(String filename, String mnemonic) {
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
        return "Bip39Wallet{" +
                "filename='" + filename + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                '}';
    }
}
