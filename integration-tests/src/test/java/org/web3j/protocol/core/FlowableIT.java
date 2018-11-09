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
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.io.File;
import java.math.BigInteger;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.web3j.protocol.core.TestParameters.isInfuraTestRinkebyUrl;

/**
 * Flowable callback tests.
 */
public class FlowableIT {
    private static Logger log = LoggerFactory.getLogger(FlowableIT.class);

    @Rule
    public Timeout globalTimeout = new Timeout(10 * 60_000);

    private static final int EVENT_COUNT = 1;
    private static final int TIMEOUT_MINUTES = 5;

    private Web3j web3j;

    @Before
    public void setUp() throws Exception {
        // TODO: upgrade to JUnit5, and use a parammetrized test to load the config perhaps?
        // Or better still, run the config for all files and exclude based on env variable?
        final String configFileName = "/local-config.yaml";
        final File configFile = new File(FlowableIT.class.getResource(configFileName).getFile());

        if (!configFile.exists()) {
            throw new IllegalStateException("Input file does not exist at: " + configFile.getAbsolutePath());
        }

//        HttpService httpService = new HttpService(TestParameters.TEST_RINKEBY_URL);
//        if (TestParameters.hasRinkebyCredentials()) {
//            httpService.addHeader("Authorization", TestParameters.getRinkebyAuthorization());
//        }

//        Web3j web3 = Web3j.build(new HttpService("https://morden.infura.io/your-token"));

        final String apiKey = "a1504fcad53249f5a7b8df6bc2e3c3fb";
        final String apiSecret = "6d95da5f296e41ff8c587c171b5cf176";
        final String ethUrl = "https://rinkeby.infura.io/v3/a1504fcad53249f5a7b8df6bc2e3c3fb";
        final String wsUrl = "wss://rinkeby.infura.io/ws";
        boolean useWebSockets = true;

        if (useWebSockets) {
            final WebSocketClient webSocketClient = new WebSocketClient(new URI(wsUrl));
            final WebSocketService webSocketService = new WebSocketService(webSocketClient,
                    true);
            webSocketService.connect();
            this.web3j = Web3j.build(webSocketService);
            log.info("Web service socket has been set up");
        } else {
            // THis works for etherum API.
            final HttpService httpService = new HttpService(ethUrl);
            httpService.addHeader("Authorization", apiSecret);
            this.web3j = Web3j.build(httpService);
        }
    }


    @Test
    public void testBlockFlowable() throws Throwable {
        assumeFalse("Infura does NOT support eth_newBlockFilter - "
                        + "https://github.com/INFURA/infura/blob/master/docs/source/index.html.md"
                        + "#supported-json-rpc-methods",
                isInfuraTestRinkebyUrl());

        // ethNewBlockFilter: WS API only!
        run(web3j.blockFlowable(false));


        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

    }

    @Test
    public void testPendingTransactionFlowable() throws Throwable {
        // eth_newPendingTransactionFilter: WS API only!
        run(web3j.pendingTransactionFlowable());
    }

    @Test
    public void testTransactionFlowable() throws Throwable {
        run(web3j.transactionFlowable()); // eth_newBlockFilter -> WS API only
    }

    @Test
    public void testLogFlowable() throws Throwable {
        // eth_newFilter: WS API only
        run(web3j.ethLogFlowable(new EthFilter()));
    }

    @Test
    public void testReplayFlowable() throws Throwable {
        // etherum API
        run(web3j.replayPastBlocksFlowable(
                new DefaultBlockParameterNumber(0),
                new DefaultBlockParameterNumber(EVENT_COUNT), true));
    }

    @Test
    public void testReplayPastAndFutureBlocksFlowable() throws Throwable {
        // etherum API: eth_getBlockByNumber
        EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send();
        BigInteger latestBlockNumber = ethBlock.getBlock().getNumber();
        run(web3j.replayPastAndFutureBlocksFlowable(
                new DefaultBlockParameterNumber(latestBlockNumber.subtract(BigInteger.ONE)),
                false));
    }

    private <T> void run(Flowable<T> flowable) throws Throwable {
        final CountDownLatch countDownLatch = new CountDownLatch(EVENT_COUNT);
        final CountDownLatch completedLatch = new CountDownLatch(EVENT_COUNT);

        final AtomicReference<Throwable> exceptionThrown = new AtomicReference<>();

        final Disposable subscription = flowable.subscribe(
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
