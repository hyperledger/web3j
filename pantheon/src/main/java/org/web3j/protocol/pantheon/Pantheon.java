package org.web3j.protocol.pantheon;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.pantheon.methods.response.EthChainIdResponse;

/**
 * JSON-RPC Request object building factory for Pantheon.
 */
public interface Pantheon {
    static Pantheon build(Web3jService web3jService) {
        return new JsonRpc2_0Pantheon(web3jService);
    }

    Request<?, EthChainIdResponse> ethChainId();
}
