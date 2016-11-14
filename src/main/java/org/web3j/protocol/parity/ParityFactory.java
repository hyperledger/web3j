package org.web3j.protocol.parity;

import org.web3j.protocol.Web3jService;

/**
 * web3j Parity client factory.
 */
public class ParityFactory {

    public static Parity build(Web3jService web3jService) {
        return new JsonRpc2_0Parity(web3jService);
    }
}
