package org.web3j.crypto;

import java.io.IOException;

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
