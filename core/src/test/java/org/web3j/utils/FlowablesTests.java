package org.web3j.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FlowablesTests {

    @Test
    public void testRangeFlowable() throws InterruptedException {
        int count = 10;

        Flowable<BigInteger> flowable = Flowables.range(
                BigInteger.ZERO, BigInteger.valueOf(count - 1));

        List<BigInteger> expected = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            expected.add(BigInteger.valueOf(i));
        }

        runRangeTest(flowable, expected);
    }

    @Test
    public void testRangeDescendingFlowable() throws InterruptedException {
        int count = 10;

        Flowable<BigInteger> flowable = Flowables.range(
                BigInteger.ZERO, BigInteger.valueOf(count - 1), false);

        List<BigInteger> expected = new ArrayList<>(count);
        for (int i = count - 1; i >= 0; i--) {
            expected.add(BigInteger.valueOf(i));
        }

        runRangeTest(flowable, expected);
    }

    private void runRangeTest(
            Flowable<BigInteger> flowable, List<BigInteger> expected)
            throws InterruptedException {

        CountDownLatch transactionLatch = new CountDownLatch(expected.size());
        CountDownLatch completedLatch = new CountDownLatch(1);

        List<BigInteger> results = new ArrayList<>(expected.size());

        Disposable subscription = flowable.subscribe(
                result -> {
                    results.add(result);
                    transactionLatch.countDown();
                },
                throwable -> fail(throwable.getMessage()),
                () -> completedLatch.countDown());

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertThat(results, equalTo(expected));

        subscription.dispose();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isDisposed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRangeFlowableIllegalLowerBound() throws InterruptedException {
        Flowables.range(BigInteger.valueOf(-1), BigInteger.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRangeFlowableIllegalBounds() throws InterruptedException {
        Flowables.range(BigInteger.TEN, BigInteger.ONE);
    }
}
