package org.web3j.crypto;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Ethereum wallet file.
 */
public class WalletFile {
    private String address;
    private Crypto crypto;
    private String id;
    private int version;

    public WalletFile() {
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

    @JsonSetter("crypto")
    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    @JsonSetter("Crypto")  // older wallet files may have this attribute name
    public void setCryptoV1(Crypto crypto) {
        setCrypto(crypto);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.address);
        hash = 37 * hash + Objects.hashCode(this.crypto);
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + this.version;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WalletFile other = (WalletFile) obj;
        if (this.version != other.version) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.crypto, other.crypto)) {
            return false;
        }
        return true;
    }   

    public static class Crypto {
        private String cipher;
        private String ciphertext;
        private CipherParams cipherparams;

        private String kdf;
        private KdfParams kdfparams;

        private String mac;

        public Crypto() {
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

        public CipherParams getCipherparams() {
            return cipherparams;
        }

        public void setCipherparams(CipherParams cipherparams) {
            this.cipherparams = cipherparams;
        }

        public String getKdf() {
            return kdf;
        }

        public void setKdf(String kdf) {
            this.kdf = kdf;
        }

        public KdfParams getKdfparams() {
            return kdfparams;
        }

        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "kdf")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = Aes128CtrKdfParams.class, name = Wallet.AES_128_CTR),
                @JsonSubTypes.Type(value = ScryptKdfParams.class, name = Wallet.SCRYPT)
        })
        // To support my Ether Wallet keys uncomment this annotation & comment out the above
        //  @JsonDeserialize(using = KdfParamsDeserialiser.class)
        // Also add the following to the ObjectMapperFactory
        // objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        public void setKdfparams(KdfParams kdfparams) {
            this.kdfparams = kdfparams;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.cipher);
            hash = 17 * hash + Objects.hashCode(this.ciphertext);
            hash = 17 * hash + Objects.hashCode(this.cipherparams);
            hash = 17 * hash + Objects.hashCode(this.kdf);
            hash = 17 * hash + Objects.hashCode(this.kdfparams);
            hash = 17 * hash + Objects.hashCode(this.mac);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Crypto other = (Crypto) obj;
            if (!Objects.equals(this.cipher, other.cipher)) {
                return false;
            }
            if (!Objects.equals(this.ciphertext, other.ciphertext)) {
                return false;
            }
            if (!Objects.equals(this.kdf, other.kdf)) {
                return false;
            }
            if (!Objects.equals(this.mac, other.mac)) {
                return false;
            }
            if (!Objects.equals(this.cipherparams, other.cipherparams)) {
                return false;
            }
            if (!Objects.equals(this.kdfparams, other.kdfparams)) {
                return false;
            }
            return true;
        }       
        
    }

    public static class CipherParams {
        private String iv;

        public CipherParams() {
        }

        public String getIv() {
            return iv;
        }

        public void setIv(String iv) {
            this.iv = iv;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 89 * hash + Objects.hashCode(this.iv);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CipherParams other = (CipherParams) obj;
            if (!Objects.equals(this.iv, other.iv)) {
                return false;
            }
            return true;
        }        
        
    }

    interface KdfParams {
        int getDklen();

        String getSalt();
    }

    public static class Aes128CtrKdfParams implements KdfParams {
        private int dklen;
        private int c;
        private String prf;
        private String salt;

        public Aes128CtrKdfParams() {
        }

        public int getDklen() {
            return dklen;
        }

        public void setDklen(int dklen) {
            this.dklen = dklen;
        }

        public int getC() {
            return c;
        }

        public void setC(int c) {
            this.c = c;
        }

        public String getPrf() {
            return prf;
        }

        public void setPrf(String prf) {
            this.prf = prf;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + this.dklen;
            hash = 53 * hash + this.c;
            hash = 53 * hash + Objects.hashCode(this.prf);
            hash = 53 * hash + Objects.hashCode(this.salt);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Aes128CtrKdfParams other = (Aes128CtrKdfParams) obj;
            if (this.dklen != other.dklen) {
                return false;
            }
            if (this.c != other.c) {
                return false;
            }
            if (!Objects.equals(this.prf, other.prf)) {
                return false;
            }
            if (!Objects.equals(this.salt, other.salt)) {
                return false;
            }
            return true;
        }        
    }

    public static class ScryptKdfParams implements KdfParams {
        private int dklen;
        private int n;
        private int p;
        private int r;
        private String salt;

        public ScryptKdfParams() {
        }

        public int getDklen() {
            return dklen;
        }

        public void setDklen(int dklen) {
            this.dklen = dklen;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + this.dklen;
            hash = 97 * hash + this.n;
            hash = 97 * hash + this.p;
            hash = 97 * hash + this.r;
            hash = 97 * hash + Objects.hashCode(this.salt);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ScryptKdfParams other = (ScryptKdfParams) obj;
            if (this.dklen != other.dklen) {
                return false;
            }
            if (this.n != other.n) {
                return false;
            }
            if (this.p != other.p) {
                return false;
            }
            if (this.r != other.r) {
                return false;
            }
            if (!Objects.equals(this.salt, other.salt)) {
                return false;
            }
            return true;
        }       
    }

    // If we need to work with MyEtherWallet we'll need to use this deserializer, see the
    // following issue https://github.com/kvhnuke/etherwallet/issues/269
    static class KdfParamsDeserialiser extends JsonDeserializer<KdfParams> {

        @Override
        public KdfParams deserialize(
                JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {

            ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
            ObjectNode root = objectMapper.readTree(jsonParser);
            KdfParams kdfParams;

            // it would be preferable to detect the class to use based on the kdf parameter in the
            // container object instance
            JsonNode n = root.get("n");
            if (n == null) {
                kdfParams = objectMapper.convertValue(root, Aes128CtrKdfParams.class);
            } else {
                kdfParams = objectMapper.convertValue(root, ScryptKdfParams.class);
            }

            return kdfParams;
        }
    }
}
