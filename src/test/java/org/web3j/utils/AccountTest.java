package org.web3j.utils;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.web3j.utils.Account.isAddress;

public class AccountTest {
    @Test
    public void verifyIsAddress() {
        String validLowerCaseAddress = "0x3de8c14c8e7a956f5cc4d82beff749ee65fdc358";
        assertTrue(isAddress(validLowerCaseAddress));
        String validChecksumAddress = "0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359";
        assertTrue(isAddress(validChecksumAddress));
        String invalidLengthAddress = "0x3de8c14c8e7a956f5cc4d82beff749ee65bac35";
        assertFalse(isAddress(invalidLengthAddress));
        String invalidChecksumAddress = "0x3de8c14c8E7a956f5cc4d82beff749ee65fdc358";
        assertFalse(isAddress(invalidChecksumAddress));
    }
}