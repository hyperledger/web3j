package org.web3j.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.web3j.utils.Collection.create;
import static org.web3j.utils.Collection.join;
import static org.web3j.utils.Collection.tail;

public class CollectionTest {

    @Test
    public void testTail() {
        String[] emptyArray = {};
        assertThat(tail(emptyArray), is(emptyArray));
        assertThat(tail(create("a", "b", "c")), is(create("b", "c")));
        assertThat(tail(create("a")), is(emptyArray));
    }

    @Test
    public void testCreate() {
        assertThat(create("a"), is(new String[]{"a"}));
        assertThat(create(""), is(new String[]{""}));
        assertThat(create("a", "b"), is(new String[]{"a", "b"}));
    }

    @Test
    public void testJoinWithFunction() {
        final List<FakeSpec> specs1 = Arrays.asList(
                new FakeSpec("a"),
                new FakeSpec("b"),
                new FakeSpec("c"));
        assertThat(join(specs1, ",", FakeSpec::getName), is("a,b,c"));

        final List<FakeSpec> specs2 = Arrays.asList(
                new FakeSpec("a"),
                new FakeSpec("b"),
                new FakeSpec("c"));
        assertThat(join(specs2, ", ", FakeSpec::getName), is("a, b, c"));

        final List<FakeSpec> specs3 = Arrays.asList(
                new FakeSpec(" a"),
                new FakeSpec("b  "),
                new FakeSpec(" c "));
        assertThat(join(specs3, ",", FakeSpec::getName), is("a,b,c"));

        final List<FakeSpec> specs4 = Arrays.asList(
                new FakeSpec(" a"),
                new FakeSpec("b  "),
                new FakeSpec(" c "));
        assertThat(join(specs4, ", ", FakeSpec::getName), is("a, b, c"));
    }

    /**
     * Fake object to test {@link Collection#join(List, CharSequence, Function)}.
     */
    private static final class FakeSpec {
        private final String name;

        private FakeSpec(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
