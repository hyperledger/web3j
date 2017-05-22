package org.web3j.utils;

import java.util.concurrent.ExecutionException;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AsyncTest {

    @Test
    public void testRun() throws Exception {
        assertThat(Async.run(() -> "").get(), is(""));
    }

    @Test(expected = ExecutionException.class)
    public void testRunException() throws Exception {
        Async.run(() -> {
            throw new RuntimeException("");
        }).get();
    }
}
