package org.web3j.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ObservablesTests {

    @Test
    public void testRangeObservable() throws InterruptedException {
        int count = 10;

        Observable<BigInteger> observable = Observables.range(
                BigInteger.ZERO, BigInteger.valueOf(count - 1));

        List<BigInteger> expected = new ArrayList<BigInteger>(count);
        for (int i = 0; i < count; i++) {
            expected.add(BigInteger.valueOf(i));
        }

        runRangeTest(observable, expected);
    }

    @Test
    public void testRangeDescendingObservable() throws InterruptedException {
        int count = 10;

        Observable<BigInteger> observable = Observables.range(
                BigInteger.ZERO, BigInteger.valueOf(count - 1), false);

        List<BigInteger> expected = new ArrayList<BigInteger>(count);
        for (int i = count - 1; i >= 0; i--) {
            expected.add(BigInteger.valueOf(i));
        }

        runRangeTest(observable, expected);
    }

    private void runRangeTest(
            Observable<BigInteger> observable, List<BigInteger> expected)
            throws InterruptedException {

        final CountDownLatch transactionLatch = new CountDownLatch(expected.size());
        final CountDownLatch completedLatch = new CountDownLatch(1);

        final List<BigInteger> results = new ArrayList<BigInteger>(expected.size());

        Subscription subscription = observable.subscribe(
                new Action1<BigInteger>() {
                    @Override
                    public void call(BigInteger result) {
                        results.add(result);
                        transactionLatch.countDown();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        fail(throwable.getMessage());
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        completedLatch.countDown();
                    }
                });

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertThat(results, equalTo(expected));

        subscription.unsubscribe();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRangeObservableIllegalLowerBound() throws InterruptedException {
        Observables.range(BigInteger.valueOf(-1), BigInteger.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRangeObservableIllegalBounds() throws InterruptedException {
        Observables.range(BigInteger.TEN, BigInteger.ONE);
    }
}
