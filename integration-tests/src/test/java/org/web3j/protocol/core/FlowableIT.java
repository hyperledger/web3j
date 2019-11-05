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
package org.web3j.protocol.core;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import static org.junit.Assert.assertTrue;

/** Flowable callback tests. */
public class FlowableIT {
    private static Logger log = LoggerFactory.getLogger(FlowableIT.class);

    private static final int EVENT_COUNT = 5;
    private static final int TIMEOUT_MINUTES = 5;

    private Web3j web3j;

    @Before
    public void setUp() {
        this.web3j = Web3j.build(new HttpService());
    }

    @Test
    public void testBlockFlowable() throws Exception {
        run(web3j.blockFlowable(false));
    }

    @Test
    public void testPendingTransactionFlowable() throws Exception {
        run(web3j.pendingTransactionFlowable());
    }

    @Test
    public void testTransactionFlowable() throws Exception {
        run(web3j.transactionFlowable());
    }

    @Test
    public void testLogFlowable() throws Exception {
        run(web3j.ethLogFlowable(new EthFilter()));
    }

    @Test
    public void testReplayFlowable() throws Exception {
        run(
                web3j.replayPastBlocksFlowable(
                        new DefaultBlockParameterNumber(0),
                        new DefaultBlockParameterNumber(EVENT_COUNT),
                        true));
    }

    @Test
    public void testReplayPastAndFutureBlocksFlowable() throws Exception {
        EthBlock ethBlock =
                web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
        BigInteger latestBlockNumber = ethBlock.getBlock().getNumber();
        run(
                web3j.replayPastAndFutureBlocksFlowable(
                        new DefaultBlockParameterNumber(latestBlockNumber.subtract(BigInteger.ONE)),
                        false));
    }

    private <T> void run(Flowable<T> flowable) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(EVENT_COUNT);
        CountDownLatch completedLatch = new CountDownLatch(EVENT_COUNT);

        Disposable subscription =
                flowable.subscribe(
                        x -> countDownLatch.countDown(),
                        Throwable::printStackTrace,
                        completedLatch::countDown);

        countDownLatch.await(TIMEOUT_MINUTES, TimeUnit.MINUTES);
        subscription.dispose();
        completedLatch.await(1, TimeUnit.SECONDS);

        log.info(
                "CountDownLatch={}, CompletedLatch={}",
                countDownLatch.getCount(),
                completedLatch.getCount());
        assertTrue(subscription.isDisposed());
    }
}
