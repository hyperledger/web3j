/*
 * Copyright 2021 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.abi.datatypes;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import org.web3j.abi.datatypes.generated.Uint8;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicArrayTest {

    @Test
    public void testEmptyDynamicArray() {
        DynamicArray<Address> empty = new DynamicArray<>(Address.class, Collections.emptyList());
        String type = empty.getTypeAsString();
        assertEquals(Address.TYPE_NAME + "[]", type);
    }

    @Test
    public void testDynamicArrayWithDynamicStruct() {
        List<DynamicStruct> list = Collections.singletonList(new DynamicStruct());
        DynamicArray<DynamicStruct> empty = new DynamicArray<>(DynamicStruct.class, list);
        String type = empty.getTypeAsString();

        assertEquals("()[]", type);
    }

    @Test
    public void testDynamicArrayWithAbiType() {
        DynamicArray<Uint> empty = new DynamicArray<>(Uint.class, arrayOfUints(1));
        String type = empty.getTypeAsString();

        assertEquals(Uint.TYPE_NAME + "[]", type);
    }

    private Uint[] arrayOfUints(int length) {
        return IntStream.rangeClosed(1, length).mapToObj(Uint8::new).toArray(Uint[]::new);
    }
}
