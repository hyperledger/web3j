package org.web3j.abi.datatypes;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BoolTest {

    @Test
    public void testToString(){
        Bool bool = new Bool(true);
        assertThat(bool.toString(), is(Boolean.toString(true)));
    }
}