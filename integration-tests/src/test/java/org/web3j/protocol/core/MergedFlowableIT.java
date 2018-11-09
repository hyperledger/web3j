package org.web3j.protocol.core;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.web3j.protocol.core.TestParameters.isInfuraTestRinkebyUrl;

/**
 * Flowable callback tests.
 */
public class MergedFlowableIT {
    private static Logger log = LoggerFactory.getLogger(MergedFlowableIT.class);

    @Rule
    public Timeout globalTimeout = new Timeout(10 * 60_000);

    private static final int EVENT_COUNT = 1;
    private static final int TIMEOUT_MINUTES = 5;

    private Web3j web3j;

    @Before
    public void setUp() {
        HttpService httpService = new HttpService(TestParameters.TEST_RINKEBY_URL);
        if (TestParameters.hasRinkebyCredentials()) {
            httpService.addHeader("Authorization", TestParameters.getRinkebyAuthorization());
        }
        this.web3j = Web3j.build(httpService);
    }

    @Test
    public void testBlockFlowable() throws Throwable {
        assumeFalse("Infura does NOT support eth_newBlockFilter - "
                        + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                        + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        run(web3j.blockFlowable(false));
    }

    @Test
    public void testPendingTransactionFlowable() throws Throwable {
        assumeFalse("Infura does NOT support eth_newPendingTransactionFilter - "
                        + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                        + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        run(web3j.pendingTransactionFlowable());
    }

    @Test
    public void testTransactionFlowable() throws Throwable {
        assumeFalse("Infura does NOT support eth_newBlockFilter - "
                        + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                        + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        run(web3j.transactionFlowable());
    }

    @Test
    public void testLogFlowable() throws Throwable {
        assumeFalse("Infura does NOT support eth_newFilter - "
                        + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                        + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        run(web3j.ethLogFlowable(new EthFilter()));
    }

    @Test
    public void testReplayFlowable() throws Throwable {
        run(web3j.replayPastBlocksFlowable(
                new DefaultBlockParameterNumber(0),
                new DefaultBlockParameterNumber(EVENT_COUNT), true));
    }

    @Test
    public void testReplayPastAndFutureBlocksFlowable() throws Throwable {
        assumeFalse("Infura does NOT support eth_newBlockFilter - "
                        + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                        + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send();
        BigInteger latestBlockNumber = ethBlock.getBlock().getNumber();
        run(web3j.replayPastAndFutureBlocksFlowable(
                new DefaultBlockParameterNumber(latestBlockNumber.subtract(BigInteger.ONE)),
                false));
    }

    private <T> void run(Flowable<T> flowable) throws Throwable {
        CountDownLatch countDownLatch = new CountDownLatch(EVENT_COUNT);
        CountDownLatch completedLatch = new CountDownLatch(EVENT_COUNT);

        AtomicReference<Throwable> exceptionThrown = new AtomicReference<>();

        Disposable subscription = flowable.subscribe(
                x -> countDownLatch.countDown(),
                e -> {
                    exceptionThrown.set(e);
                    countDownLatch.countDown();
                },
                completedLatch::countDown
        );
        countDownLatch.await(TIMEOUT_MINUTES, TimeUnit.MINUTES);
        subscription.dispose();

        if (exceptionThrown.get() != null) {
            throw exceptionThrown.get();
        }

        subscription.dispose();
        completedLatch.await(1, TimeUnit.SECONDS);

        log.info("CountDownLatch={}, CompletedLatch={}", countDownLatch.getCount(),
                completedLatch.getCount());
        assertTrue(subscription.isDisposed());
    }
}
