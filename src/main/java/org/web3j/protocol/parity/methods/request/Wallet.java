package org.web3j.protocol.parity.methods.request;

import java.util.Map;

/**
 * Ethereum Wallet
 */
public class Wallet {
    private String address;
    private Crypto crypto;
    private String id;

    public Wallet() {
    }

    public Wallet(String address, Crypto crypto, String id) {
        this.address = address;
        this.crypto = crypto;
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        if (address != null ? !address.equals(wallet.address) : wallet.address != null)
            return false;
        if (crypto != null ? !crypto.equals(wallet.crypto) : wallet.crypto != null) return false;
        return id != null ? id.equals(wallet.id) : wallet.id == null;

    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (crypto != null ? crypto.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public static class Crypto {
        private String cipher;
        private String ciphertext;
        private Map<String, String> cipherparams;
        private String kdf;
        private Map<String, String> kdfparams;
        private String mac;

        public Crypto() { }

        public Crypto(String cipher, String ciphertext, Map<String, String> cipherparams,
                      String kdf, Map<String, String> kdfparams, String mac) {
            this.cipher = cipher;
            this.ciphertext = ciphertext;
            this.cipherparams = cipherparams;
            this.kdf = kdf;
            this.kdfparams = kdfparams;
            this.mac = mac;
        }

        public String getCipher() {
            return cipher;
        }

        public void setCipher(String cipher) {
            this.cipher = cipher;
        }

        public String getCiphertext() {
            return ciphertext;
        }

        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        public Map<String, String> getCipherparams() {
            return cipherparams;
        }

        public void setCipherparams(Map<String, String> cipherparams) {
            this.cipherparams = cipherparams;
        }

        public String getKdf() {
            return kdf;
        }

        public void setKdf(String kdf) {
            this.kdf = kdf;
        }

        public Map<String, String> getKdfparams() {
            return kdfparams;
        }

        public void setKdfparams(Map<String, String> kdfparams) {
            this.kdfparams = kdfparams;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Crypto crypto = (Crypto) o;

            if (cipher != null ? !cipher.equals(crypto.cipher) : crypto.cipher != null)
                return false;
            if (ciphertext != null ? !ciphertext.equals(crypto.ciphertext) : crypto.ciphertext != null)
                return false;
            if (cipherparams != null ? !cipherparams.equals(crypto.cipherparams) : crypto.cipherparams != null)
                return false;
            if (kdf != null ? !kdf.equals(crypto.kdf) : crypto.kdf != null) return false;
            if (kdfparams != null ? !kdfparams.equals(crypto.kdfparams) : crypto.kdfparams != null)
                return false;
            return mac != null ? mac.equals(crypto.mac) : crypto.mac == null;

        }

        @Override
        public int hashCode() {
            int result = cipher != null ? cipher.hashCode() : 0;
            result = 31 * result + (ciphertext != null ? ciphertext.hashCode() : 0);
            result = 31 * result + (cipherparams != null ? cipherparams.hashCode() : 0);
            result = 31 * result + (kdf != null ? kdf.hashCode() : 0);
            result = 31 * result + (kdfparams != null ? kdfparams.hashCode() : 0);
            result = 31 * result + (mac != null ? mac.hashCode() : 0);
            return result;
        }
    }
}
