/*
 * Copyright 2019 Web3 Labs LTD.
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

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.web3j.utils.Collection.EMPTY_STRING_ARRAY;
import static org.web3j.utils.Collection.Function;
import static org.web3j.utils.Collection.create;
import static org.web3j.utils.Collection.join;
import static org.web3j.utils.Collection.tail;

public class CollectionTest {

    @Test
    public void testTail() {
        assertThat(tail(EMPTY_STRING_ARRAY), is(EMPTY_STRING_ARRAY));
        assertThat(tail(create("a", "b", "c")), is(create("b", "c")));
        assertThat(tail(create("a")), is(EMPTY_STRING_ARRAY));
    }

    @Test
    public void testCreate() {
        assertThat(create("a"), is(new String[] {"a"}));
        assertThat(create(""), is(new String[] {""}));
        assertThat(create("a", "b"), is(new String[] {"a", "b"}));
    }

    @Test
    public void testJoin() {
        assertThat(join(Arrays.asList("a  ", "b ", " c "), ","), is("a,b,c"));
        assertThat(join(Arrays.asList("a", "b", "c", "d"), ","), is("a,b,c,d"));
        assertThat(join(Arrays.asList("a  ", "b ", " c "), ", "), is("a, b, c"));
        assertThat(join(Arrays.asList("a", "b", "c", "d"), ", "), is("a, b, c, d"));
    }

    @Test
    public void testJoinWithFunction() {
        final List<FakeSpec> specs1 =
                Arrays.asList(new FakeSpec("a"), new FakeSpec("b"), new FakeSpec("c"));
        assertThat(join(specs1, ",", FakeSpec::getName), is("a,b,c"));

        final List<FakeSpec> specs2 =
                Arrays.asList(new FakeSpec("a"), new FakeSpec("b"), new FakeSpec("c"));
        assertThat(join(specs2, ", ", FakeSpec::getName), is("a, b, c"));

        final List<FakeSpec> specs3 =
                Arrays.asList(new FakeSpec(" a"), new FakeSpec("b  "), new FakeSpec(" c "));
        assertThat(join(specs3, ",", FakeSpec::getName), is("a,b,c"));

        final List<FakeSpec> specs4 =
                Arrays.asList(new FakeSpec(" a"), new FakeSpec("b  "), new FakeSpec(" c "));
        assertThat(join(specs4, ", ", FakeSpec::getName), is("a, b, c"));
    }

    /** Fake object to test {@link Collection#join(List, String, Function)}. */
    private final class FakeSpec {
        private final String name;

        private FakeSpec(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
