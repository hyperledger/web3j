package org.web3j.abi.datatypes;

import org.junit.Test;

import org.web3j.abi.datatypes.generated.StaticArray3;
import org.web3j.abi.datatypes.generated.Uint8;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class StaticArrayTest {

    @Test
    public void canBeInstantiatedWithLessThan32Elements() {
        final StaticArray<Uint> array = new StaticArray<Uint>(arrayOfUints(32));

        assertThat(array.getValue().size(), equalTo(32));
    }

    @Test
    public void canBeInstantiatedWithSizeMatchingType() {
        final StaticArray<Uint> array = new StaticArray3<Uint>(arrayOfUints(3));

        assertThat(array.getValue().size(), equalTo(3));
    }

    @Test
    public void throwsIfSizeDoesntMatchType() {
        try {
            new StaticArray3<Uint>(arrayOfUints(4));
            fail();
        } catch (UnsupportedOperationException e) {
            assertThat(e.getMessage(), equalTo(
                    "Expected array of type [StaticArray3] to have [3] elements."));
        }
    }

    @Test
    public void throwsIfSizeIsAboveMaxOf32() {
        try {
            new StaticArray<Uint>(arrayOfUints(33));
            fail();
        } catch (UnsupportedOperationException e) {
            assertThat(e.getMessage(), equalTo(
                    "Static arrays with a length greater than 32 are not supported."));
        }
    }

    private Uint[] arrayOfUints(int length) {
        Uint[] uints = new Uint[length];
        for (int i = 0; i < length; i++) {
            uints[i] = new Uint8(i + 1);
        }
        return uints;
    }
}