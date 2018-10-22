package org.web3j.protocol.core;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.Before;
import org.junit.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import static org.junit.Assert.assertTrue;

/**
 * Observable callback tests.
 */
public class ObservableIT {

    private static final int EVENT_COUNT = 5;
    private static final int TIMEOUT_MINUTES = 5;

    private Web3j web3j;

    @Before
    public void setUp() {
        this.web3j = Web3j.build(new HttpService());
    }

    @Test
    public void testBlockObservable() throws Exception {
        run(web3j.blockObservable(false));
    }

    @Test
    public void testPendingTransactionObservable() throws Exception {
        run(web3j.pendingTransactionObservable());
    }

    @Test
    public void testTransactionObservable() throws Exception {
        run(web3j.transactionObservable());
    }

    @Test
    public void testLogObservable() throws Exception {
        run(web3j.ethLogObservable(new EthFilter()));
    }

    @Test
    public void testReplayObservable() throws Exception {
        run(web3j.replayBlocksObservable(
                new DefaultBlockParameterNumber(0),
                new DefaultBlockParameterNumber(EVENT_COUNT), true));
    }

    @Test
    public void testCatchUpToLatestAndSubscribeToNewBlocksObservable() throws Exception {
        EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send();
        BigInteger latestBlockNumber = ethBlock.getBlock().getNumber();
        run(web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(
                new DefaultBlockParameterNumber(latestBlockNumber.subtract(BigInteger.ONE)),
                false));
    }

    private <T> void run(Flowable<T> observable) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(EVENT_COUNT);
        CountDownLatch completedLatch = new CountDownLatch(EVENT_COUNT);

        Disposable subscription = observable.subscribe(
                x -> countDownLatch.countDown(),
                Throwable::printStackTrace,
                completedLatch::countDown
        );

        countDownLatch.await(TIMEOUT_MINUTES, TimeUnit.MINUTES);
        subscription.dispose();
        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isDisposed());
    }
}
