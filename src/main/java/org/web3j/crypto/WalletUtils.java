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

    public static Credentials loadCredentials(String password, File source)
            throws IOException, CipherException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        WalletFile walletFile = objectMapper.readValue(source, WalletFile.class);
        return Credentials.create(Wallet.decrypt(password, walletFile));
    }

    private static String getWalletFileName(WalletFile walletFile) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(
                "'UTC--'yyyy-MM-dd'T'hh-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

        return now.format(format) + walletFile.getAddress() + ".json";
    }

    public static String getDefaultKeyDirectory() {
        return getDefaultKeyDirectory(System.getProperty("os.name"));
    }

    static String getDefaultKeyDirectory(String osName1) {
        String osName = osName1.toLowerCase();

        if (osName.startsWith("mac")) {
            return System.getProperty("user.home") + "/Library/Ethereum";
        } else if (osName.startsWith("win")) {
            return System.getenv("APPDATA") + "/Ethereum";
        } else {
            return System.getProperty("user.home") + "/.ethereum";
        }
    }

    public static String getTestnetKeyDirectory() {
        return getDefaultKeyDirectory() + "/testnet/keystore";
    }

    public static String getMainnetKeyDirectory() {
        return getDefaultKeyDirectory() + "/keystore";
    }
}
