package org.web3j.protocol;

import java.util.concurrent.ScheduledExecutorService;

import org.web3j.protocol.core.JsonRpc2_0Web3j;

/**
 * web3j factory implementation.
 */
public class Web3jFactory {
    public static Web3j build(Web3jService web3jService) {
        return new JsonRpc2_0Web3j(web3jService);
    }

    public static Web3j build(
            Web3jService web3jService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Web3j(web3jService, pollingInterval, scheduledExecutorService);
    }
}
