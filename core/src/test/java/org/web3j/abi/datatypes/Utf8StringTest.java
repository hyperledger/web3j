package org.web3j.abi.datatypes;

import org.junit.Test;

import org.web3j.abi.datatypes.Utf8String;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Utf8StringTest {

    @Test
    public void testToString() {
        assertThat(new Utf8String("").toString(), is(""));
        assertThat(new Utf8String("string").toString(), is("string"));
    }
}
