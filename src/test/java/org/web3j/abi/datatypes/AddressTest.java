package org.web3j.abi.datatypes;

import org.junit.Test;

import org.web3j.crypto.SampleKeys;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressTest {

    @Test
    public void testToString() {
        assertThat(new Address(SampleKeys.ADDRESS).toString(), is(SampleKeys.ADDRESS));
        assertThat(new Address(SampleKeys.ADDRESS_NO_PREFIX).toString(), is(SampleKeys.ADDRESS));
        assertThat(new Address("0x00052b08330e05d731e38c856c1043288f7d9744").toString(),
                is("0x00052b08330e05d731e38c856c1043288f7d9744"));
    }
}
