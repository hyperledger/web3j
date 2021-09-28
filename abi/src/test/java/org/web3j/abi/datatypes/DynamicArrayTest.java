package org.web3j.abi.datatypes;

import org.junit.jupiter.api.Test;
import org.web3j.abi.datatypes.generated.Uint8;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicArrayTest {

    @Test
    public void testEmptyDynamicArray()  {
        DynamicArray<Address> empty = new DynamicArray<>(Address.class, Collections.emptyList());
        String type = empty.getTypeAsString();
        assertEquals(Address.TYPE_NAME+"[]", type);
    }

    @Test
    public void testDynamicArrayWithDynamicStruct()  {
        List<DynamicStruct> list = Collections.singletonList(new DynamicStruct());
        DynamicArray<DynamicStruct> empty = new DynamicArray<>(DynamicStruct.class, list);
        String type = empty.getTypeAsString();

        assertEquals("()[]", type);
    }

    @Test
    public void testDynamicArrayWithAbiType()  {
        DynamicArray<Uint> empty = new DynamicArray<>(Uint.class, arrayOfUints(1));
        String type = empty.getTypeAsString();

        assertEquals(Uint.TYPE_NAME+"[]", type);
    }

    private Uint[] arrayOfUints(int length) {
        return IntStream.rangeClosed(1, length).mapToObj(Uint8::new).toArray(Uint[]::new);
    }
}
