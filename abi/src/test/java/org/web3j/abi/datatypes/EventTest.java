package org.web3j.abi.datatypes;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.generated.Uint256;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    @Test
    public void testCreation() {
        Event event =
                new Event(
                        "testName",
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));

        assertEquals(event.getName(), "testName");
        assertEquals(event.getParameters().size(), 2);
        assertEquals(event.getIndexedParameters().size(), 0);
        assertEquals(event.getNonIndexedParameters().size(), 2);
    }
}

