package org.web3j.crypto;


import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class WalletTest {

    @Test
    public void testCreateStandard() throws Exception {
        testCreate(Wallet.createStandard(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR));
    }

    @Test
    public void testCreateLight() throws Exception {
        testCreate(Wallet.createLight(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR));
    }

    private void testCreate(WalletFile walletFile) throws Exception {
        assertThat(walletFile.getAddress(), is(SampleKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testEncryptDecryptStandard() throws Exception {
        testEncryptDecrypt(Wallet.createStandard(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR));
    }

    @Test
    public void testEncryptDecryptLight() throws Exception {
        testEncryptDecrypt(Wallet.createLight(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR));
    }

    private void testEncryptDecrypt(WalletFile walletFile) throws Exception {
        assertThat(Wallet.decrypt(SampleKeys.PASSWORD, walletFile), equalTo(SampleKeys.KEY_PAIR));
    }

    @Test
    public void testDecryptAes128Ctr() throws Exception {
        WalletFile walletFile = load(AES_128_CTR);
        ECKeyPair ecKeyPair = Wallet.decrypt(PASSWORD, walletFile);
        assertThat(Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey()), is(SECRET));
    }

    @Test
    public void testDecryptScrypt() throws Exception {
        WalletFile walletFile = load(SCRYPT);
        ECKeyPair ecKeyPair = Wallet.decrypt(PASSWORD, walletFile);
        assertThat(Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey()), is(SECRET));
    }

    @Test
    public void testGenerateRandomBytes() {
        assertThat(Wallet.generateRandomBytes(0), is(new byte[]{}));
        assertThat(Wallet.generateRandomBytes(10).length, is(10));
    }

    private WalletFile load(String source) throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        return objectMapper.readValue(source, WalletFile.class);
    }

    // Test vectors taken from https://github.com/ethereum/wiki/wiki/Web3-Secret-Storage-Definition
    private static final String PASSWORD = "testpassword";
    private static final String SECRET = "7a28b5ba57c53603b0b07b56bba752f7784bf506fa95edc395f5cf6c7514fe9d";

    private static final String AES_128_CTR = "{\n" +
            "    \"crypto\" : {\n" +
            "        \"cipher\" : \"aes-128-ctr\",\n" +
            "        \"cipherparams\" : {\n" +
            "            \"iv\" : \"6087dab2f9fdbbfaddc31a909735c1e6\"\n" +
            "        },\n" +
            "        \"ciphertext\" : \"5318b4d5bcd28de64ee5559e671353e16f075ecae9f99c7a79a38af5f869aa46\",\n" +
            "        \"kdf\" : \"pbkdf2\",\n" +
            "        \"kdfparams\" : {\n" +
            "            \"c\" : 262144,\n" +
            "            \"dklen\" : 32,\n" +
            "            \"prf\" : \"hmac-sha256\",\n" +
            "            \"salt\" : \"ae3cd4e7013836a3df6bd7241b12db061dbe2c6785853cce422d148a624ce0bd\"\n" +
            "        },\n" +
            "        \"mac\" : \"517ead924a9d0dc3124507e3393d175ce3ff7c1e96529c6c555ce9e51205e9b2\"\n" +
            "    },\n" +
            "    \"id\" : \"3198bc9c-6672-5ab3-d995-4942343ae5b6\",\n" +
            "    \"version\" : 3\n" +
            "}";

    private static final String SCRYPT = "{\n" +
            "    \"crypto\" : {\n" +
            "        \"cipher\" : \"aes-128-ctr\",\n" +
            "        \"cipherparams\" : {\n" +
            "            \"iv\" : \"83dbcc02d8ccb40e466191a123791e0e\"\n" +
            "        },\n" +
            "        \"ciphertext\" : \"d172bf743a674da9cdad04534d56926ef8358534d458fffccd4e6ad2fbde479c\",\n" +
            "        \"kdf\" : \"scrypt\",\n" +
            "        \"kdfparams\" : {\n" +
            "            \"dklen\" : 32,\n" +
            "            \"n\" : 262144,\n" +
            "            \"r\" : 1,\n" +
            "            \"p\" : 8,\n" +
            "            \"salt\" : \"ab0c7876052600dd703518d6fc3fe8984592145b591fc8fb5c6d43190334ba19\"\n" +
            "        },\n" +
            "        \"mac\" : \"2103ac29920d71da29f15d75b4a16dbe95cfd7ff8faea1056c33131d846e3097\"\n" +
            "    },\n" +
            "    \"id\" : \"3198bc9c-6672-5ab3-d995-4942343ae5b6\",\n" +
            "    \"version\" : 3\n" +
            "}";
}
