package org.web3j.utils;


import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.web3j.utils.Collection.*;
import static org.web3j.utils.Collection.EMPTY_STRING_ARRAY;

public class CollectionTests {

    @Test
    public void testTail() {
        assertThat(tail(EMPTY_STRING_ARRAY), is(EMPTY_STRING_ARRAY));
        assertThat(tail(create("a", "b", "c" )), is(create("b", "c")));
        assertThat(tail(create("a")), is(EMPTY_STRING_ARRAY));
    }

    @Test
    public void testCreate() {
        assertThat(create("a"), is(new String[] { "a" }));
        assertThat(create(""), is(new String[] { "" }));
        assertThat(create("a", "b"), is(new String[] { "a", "b" }));
    }
}
