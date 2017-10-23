package org.web3j.abi.datatypes;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AddressTest {

    @Test
    public void testToString() {
        assertThat(new Address("00052b08330e05d731e38c856c1043288f7d9744").toString(),
                is("0x00052b08330e05d731e38c856c1043288f7d9744"));
        assertThat(new Address("0x00052b08330e05d731e38c856c1043288f7d9744").toString(),
                is("0x00052b08330e05d731e38c856c1043288f7d9744"));
    }
}
