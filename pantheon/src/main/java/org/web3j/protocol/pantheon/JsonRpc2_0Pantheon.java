package org.web3j.protocol.pantheon;

import java.util.Collections;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.pantheon.methods.response.EthChainIdResponse;

/**
 * JSON-RPC 2.0 factory implementation for Pantheon.
 */
public class JsonRpc2_0Pantheon extends JsonRpc2_0Web3j implements Pantheon {

    public JsonRpc2_0Pantheon(Web3jService web3jService) {
        super(web3jService);
    }

    @Override
    public Request<?, EthChainIdResponse> ethChainId() {
        return new Request<>(
                "eth_chainId",
                Collections.<String>emptyList(),
                web3jService,
                EthChainIdResponse.class);
    }

}
