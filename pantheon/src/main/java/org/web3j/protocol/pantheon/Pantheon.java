package org.web3j.protocol.pantheon;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;

public interface Pantheon extends Web3j {
    static Pantheon build(Web3jService web3jService) {
        return new JsonRpc2_0Pantheon(web3jService);
    }
}
