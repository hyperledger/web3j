package org.web3j.ens;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.web3j.ens.NameHash.nameHash;
import static org.web3j.ens.NameHash.normalise;

public class NameHashTest {

    @Test
    public void testNameHash() {
        assertThat(nameHash(""),
                is("0x0000000000000000000000000000000000000000000000000000000000000000"));

        assertThat(nameHash("eth"),
                is("0x93cdeb708b7545dc668eb9280176169d1c33cfd8ed6f04690a0bcc88a93fc4ae"));

        assertThat(nameHash("foo.eth"),
                is("0xde9b09fd7c5f901e23a3f19fecc54828e9c848539801e86591bd9801b019f84f"));
    }

    @Test
    public void testNormalise() {
        assertThat(normalise("foo"), is("foo"));
        assertThat(normalise("foo.bar.baz.eth"), is("foo.bar.baz.eth"));
        assertThat(normalise("fOo.eth"), is("foo.eth"));
        assertThat(normalise("foo-bar.eth"), is("foo-bar.eth"));
    }

    @Test
    public void testNormaliseInvalid() {
        testInvalidName("foo..bar");
        testInvalidName("ba\\u007Fr.eth");
        testInvalidName("-baz.eth-");
        testInvalidName("foo_bar.eth");
    }

    private void testInvalidName(String ensName) {
        try {
            normalise(ensName);
            fail("Name should not be valid");
        } catch (EnsResolutionException e) {
            // expected
        }
    }
}
