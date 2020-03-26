/*
 * Copyright 2019 Web3 Labs Ltd.
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
package org.web3j.utils;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.web3j.utils.Collection.EMPTY_STRING_ARRAY;
import static org.web3j.utils.Collection.create;
import static org.web3j.utils.Collection.join;
import static org.web3j.utils.Collection.tail;

public class CollectionTest {

    @Test
    public void testTail() {
        assertArrayEquals(tail(EMPTY_STRING_ARRAY), (EMPTY_STRING_ARRAY));
        assertArrayEquals(tail(create("a", "b", "c")), (create("b", "c")));
        assertArrayEquals(tail(create("a")), (EMPTY_STRING_ARRAY));
    }

    @Test
    public void testCreate() {
        assertArrayEquals(create("a"), (new String[] {"a"}));
        assertArrayEquals(create(""), (new String[] {""}));
        assertArrayEquals(create("a", "b"), (new String[] {"a", "b"}));
    }

    @Test
    public void testJoinWithFunction() {
        final List<FakeSpec> specs1 =
                Arrays.asList(new FakeSpec("a"), new FakeSpec("b"), new FakeSpec("c"));
        assertEquals(join(specs1, ",", FakeSpec::getName), ("a,b,c"));

        final List<FakeSpec> specs2 =
                Arrays.asList(new FakeSpec("a"), new FakeSpec("b"), new FakeSpec("c"));
        assertEquals(join(specs2, ", ", FakeSpec::getName), ("a, b, c"));

        final List<FakeSpec> specs3 =
                Arrays.asList(new FakeSpec(" a"), new FakeSpec("b  "), new FakeSpec(" c "));
        assertEquals(join(specs3, ",", FakeSpec::getName), ("a,b,c"));

        final List<FakeSpec> specs4 =
                Arrays.asList(new FakeSpec(" a"), new FakeSpec("b  "), new FakeSpec(" c "));
        assertEquals(join(specs4, ", ", FakeSpec::getName), ("a, b, c"));
    }

    private final class FakeSpec {
        private final String name;

        private FakeSpec(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
