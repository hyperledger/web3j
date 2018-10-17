package org.web3j.protocol.parity;

import org.web3j.protocol.Web3jService;

public class ParityFactory {

    static Parity build(Web3jService web3jService) {
        return new JsonRpc2_0Parity(web3jService);
    }

}
