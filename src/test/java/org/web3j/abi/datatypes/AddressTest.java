package org.web3j.abi.datatypes;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AddressTest {

    @Test
    public void testToString() throws Exception {
        String expected = "1234567890abcdef";
        Address address = new Address(expected);

        assertThat(address.toString(), is(expected));
    }

}