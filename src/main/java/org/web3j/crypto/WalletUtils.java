package org.web3j.crypto;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import static org.web3j.crypto.Keys.ADDRESS_LENGTH_IN_HEX;
import static org.web3j.crypto.Keys.PRIVATE_KEY_LENGTH_IN_HEX;

/**
 * Utility functions for working with Wallet files.
 */
public class WalletUtils {

    public static String generateNewWalletFile(String password, File destinationDirectory)
            throws CipherException, IOException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException {

        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        return generateWalletFile(password, ecKeyPair, destinationDirectory);
    }

    public static String generateWalletFile(
            String password, ECKeyPair ecKeyPair, File destinationDirectory)
            throws CipherException, IOException {
        WalletFile walletFile = Wallet.create(password, ecKeyPair);

        String fileName = getWalletFileName(walletFile);
        File destination = new File(destinationDirectory, fileName);

        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        objectMapper.writeValue(destination, walletFile);

        return fileName;
    }

    public static Credentials loadCredentials(String password, String source)
            throws IOException, CipherException {
        return loadCredentials(password, new File(source));
    }

    public static Credentials loadCredentials(String password, File source)
            throws IOException, CipherException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        WalletFile walletFile = objectMapper.readValue(source, WalletFile.class);
        return Credentials.create(Wallet.decrypt(password, walletFile));
    }

    private static String getWalletFileName(WalletFile walletFile) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(
                "'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        return now.format(format) + walletFile.getAddress() + ".json";
    }

    public static String getDefaultKeyDirectory() {
        return getDefaultKeyDirectory(System.getProperty("os.name"));
    }

    static String getDefaultKeyDirectory(String osName1) {
        String osName = osName1.toLowerCase();

        if (osName.startsWith("mac")) {
            return String.format("%s%sLibrary%sEthereum", 
            		System.getProperty("user.home"), File.separator, File.separator);
        } else if (osName.startsWith("win")) {
            return String.format("%s%sEthereum", System.getenv("APPDATA"), File.separator);
        } else {
            return String.format("%s%s.ethereum", System.getProperty("user.home"), File.separator);
        }
    }

    public static String getTestnetKeyDirectory() {
        return String.format("%s%stestnet%skeystore", getDefaultKeyDirectory(), File.separator, File.separator);
    }

    public static String getMainnetKeyDirectory() {
        return String.format("%s%skeystore", getDefaultKeyDirectory(), File.separator);
    }

    public static boolean isValidPrivateKey(String privateKey) {
        String cleanPrivateKey = Numeric.cleanHexPrefix(privateKey);
        return cleanPrivateKey.length() == PRIVATE_KEY_LENGTH_IN_HEX;
    }

    public static boolean isValidAddress(String address) {
        String addressNoPrefix = Numeric.cleanHexPrefix(address);
        return addressNoPrefix.length() == ADDRESS_LENGTH_IN_HEX;
    }
}
