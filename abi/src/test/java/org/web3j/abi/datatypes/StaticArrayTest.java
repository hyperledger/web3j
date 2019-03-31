package org.web3j.abi.datatypes;

import java.util.stream.IntStream;

import org.junit.Test;

import org.web3j.abi.datatypes.generated.StaticArray1024;
import org.web3j.abi.datatypes.generated.StaticArray3;
import org.web3j.abi.datatypes.generated.StaticArray32;
import org.web3j.abi.datatypes.generated.Uint16;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class StaticArrayTest {

    @Test
    public void canBeInstantiatedWithLessThan32Elements() {
        final StaticArray<Uint> array = new StaticArray32<>(arrayOfUints(32));

        assertThat(array.getValue().size(), equalTo(32));
    }

    @Test
    public void canBeInstantiatedWithSizeMatchingType() {
        final StaticArray<Uint> array = new StaticArray3<>(arrayOfUints(3));

        assertThat(array.getValue().size(), equalTo(3));
    }

    @Test
    public void throwsIfSizeDoesntMatchType() {
        try {
            new StaticArray3<>(arrayOfUints(4));
            fail();
        } catch (UnsupportedOperationException e) {
            assertThat(e.getMessage(), equalTo(
                    "Expected array of type [StaticArray3] to have [3] elements."));
        }
    }

    @Test
    public void throwsIfSizeIsAboveMax() {
        final int maxArraySize = StaticArray.MAX_SIZE_OF_STATIC_ARRAY;
        try {
            new StaticArray1024<>(arrayOfUints(maxArraySize + 1));
            fail();
        } catch (UnsupportedOperationException e) {
            assertThat(e.getMessage(), equalTo("Static arrays with a length greater than "
                    + maxArraySize + " are not supported."));
        }
    }

    private Uint[] arrayOfUints(int length) {
        return IntStream.rangeClosed(1, length).mapToObj(Uint16::new).toArray(Uint[]::new);
    }
}