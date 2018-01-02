package org.web3j.crypto;

import java.math.BigInteger;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.web3j.crypto.ContractUtils.generateContractAddress;

public class ContractUtilsTest {

    @Test
    public void testCreateContractAddress() {
        String address = "0x19e03255f667bdfd50a32722df860b1eeaf4d635";

        assertThat(generateContractAddress(address, BigInteger.valueOf(209)),
                is("0xe41e694d8fa4337b7bffc7483d3609ae1ea068d5"));

        assertThat(generateContractAddress(address, BigInteger.valueOf(257)),
                is("0x59c21d36fbe415218e834683cb6616f2bc971ca9"));
    }
}
