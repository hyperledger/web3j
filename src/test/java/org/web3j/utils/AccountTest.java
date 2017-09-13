package org.web3j.utils;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.web3j.utils.Account.isAddress;

public class AccountTest {

    final static String validLowerCaseAddress = "0x3de8c14c8e7a956f5cc4d82beff749ee65fdc358";
    final static String validChecksumAddress = "0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359";
    final static String invalidLengthAddress = "0x3de8c14c8e7a956f5cc4d82beff749ee65bac35";
    final static String invalidChecksumAddress = "0x3de8c14c8E7a956f5cc4d82beff749ee65fdc358";

    @Test
    public void verifyIsAddress() {
        assertTrue(isAddress(validLowerCaseAddress));
        assertTrue(isAddress(validChecksumAddress));
        assertFalse(isAddress(invalidLengthAddress));
        assertFalse(isAddress(invalidChecksumAddress));
    }
}