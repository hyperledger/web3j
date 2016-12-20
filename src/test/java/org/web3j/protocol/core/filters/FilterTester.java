package org.web3j.protocol.core.filters;


import java.util.*;
import java.util.concurrent.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthUninstallFilter;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class FilterTester {

    private Web3jService web3jService;
    Web3j web3j;

    final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Before
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3jFactory.build(web3jService, 1000, executorService);
    }

    <T> void runTest(EthLog ethLog, Observable<T> observable) throws Exception {
        EthFilter ethFilter = objectMapper.readValue(
                "{\n" +
                        "  \"id\":1,\n" +
                        "  \"jsonrpc\": \"2.0\",\n" +
                        "  \"result\": \"0x1\"\n" +
                        "}", EthFilter.class);

        EthUninstallFilter ethUninstallFilter = objectMapper.readValue(
                "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":true}", EthUninstallFilter.class);

        final List<T> expected = createExpected(ethLog);
        final Set<T> results = Collections.synchronizedSet(new HashSet<T>());

        final CountDownLatch transactionLatch = new CountDownLatch(expected.size());

        final CountDownLatch completedLatch = new CountDownLatch(1);

        when(web3jService.sendAsync(any(Request.class), eq(EthFilter.class)))
                .thenReturn(future(ethFilter));
        when(web3jService.sendAsync(any(Request.class), eq(EthLog.class)))
                .thenReturn(future(ethLog));
        when(web3jService.sendAsync(any(Request.class), eq(EthUninstallFilter.class)))
                .thenReturn(future(ethUninstallFilter));

        Subscription subscription = observable.subscribe(
                new Action1<T>() {
                    @Override
                    public void call(T result) {
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
        assertThat(results, CoreMatchers.<Set<T>>equalTo(new HashSet<T>(expected)));

        subscription.unsubscribe();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }

    List createExpected(EthLog ethLog) {
        List<EthLog.LogResult> logResults = ethLog.getLogs();
        if (logResults.isEmpty()) {
            fail("Results cannot be empty");
        }

        List expected = new ArrayList();
        for (EthLog.LogResult logResult : ethLog.getLogs()) {
            expected.add(logResult.get());
        }
        return expected;
    }

    private <T extends Response> Future<T> future(final T value) {
        return executorService.submit(new Callable<T>() {
            @Override
            public T call() {
                return value;
            }
        });
    }
}
