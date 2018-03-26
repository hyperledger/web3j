package org.web3j.protocol.core;

import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class JsonRpc2_0WebSocketClientJTest {

    @Test
    public void testStopExecutorOnShutdown() throws Exception {
        ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);
        Web3jService service = mock(Web3jService.class);

        Web3j web3j = Web3j.build(service, 10, scheduledExecutorService);

        web3j.shutdown();

        verify(scheduledExecutorService).shutdown();
        verify(service).close();
    }
}