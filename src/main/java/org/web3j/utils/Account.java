package org.web3j.utils;

import java.util.regex.Pattern;

import org.web3j.crypto.Hash;

/**
 * Account utility functions.
 */
public class Account {

    private static final Pattern ignoreCaseAddrPattern = Pattern.compile("(?i)^(0x)?[0-9a-f]{40}$");
    private static final Pattern lowerCaseAddrPattern = Pattern.compile("^(0x)?[0-9a-f]{40}$");
    private static final Pattern upperCaseAddrPattern = Pattern.compile("^(0x)?[0-9A-F]{40}$");

    /**
     * Verify that a hex account string is a valid Ethereum address.
     *
     * @param address given address in HEX
     * @return is this a valid address
     */
    public static Boolean isAddress(String address) {
        /*
         * check basic address requirements, i.e. is not empty and contains
         * the valid number and type of characters
         */
        if (address.isEmpty() || !ignoreCaseAddrPattern.matcher(address).find()) {
            return false;
        } else if (lowerCaseAddrPattern.matcher(address).find()
                || upperCaseAddrPattern.matcher(address).find()) {
            // if it's all small caps or caps return true
            return true;
        } else {
            // if it is mixed caps it is a checksum address and needs to be validated
            return validateChecksumAddress(address);
        }
    }

    private static Boolean validateChecksumAddress(String address) {
        address = address.replace("0x", "");
        String hash = Numeric.toHexStringNoPrefix(Hash.sha3(address.toLowerCase().getBytes()));
        for (int i = 0; i < 40; i++) {
            if (Character.isLetter(address.charAt(i))) {
                // each uppercase letter should correlate with a first bit of 1 in the hash
                // char with the same index, and each lowercase letter with a 0 bit
                int charInt = Integer.parseInt(Character.toString(hash.charAt(i)), 16);
                if ((Character.isUpperCase(address.charAt(i)) && charInt <= 7)
                        || (Character.isLowerCase(address.charAt(i)) && charInt > 7)) {
                    return false;
                }
            }
        }
        return true;
    }
}
