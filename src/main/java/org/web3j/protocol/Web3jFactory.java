package org.web3j.protocol;

import java.util.concurrent.ExecutorService;

import org.web3j.protocol.core.JsonRpc2_0Web3j;

/**
 * web3j factory implementation.
 */
public class Web3jFactory {
    public static Web3j build(Web3jService web3jService) {
        return new JsonRpc2_0Web3j(web3jService);
    }

    public static Web3j build(
            Web3jService web3jService, long pollingInterval, ExecutorService executorService) {
        return new JsonRpc2_0Web3j(web3jService, pollingInterval, executorService);
    }
}
