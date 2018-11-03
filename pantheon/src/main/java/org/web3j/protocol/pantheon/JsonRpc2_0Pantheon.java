package org.web3j.protocol.pantheon;

import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.JsonRpc2_0Web3j;

public class JsonRpc2_0Pantheon extends JsonRpc2_0Web3j implements Pantheon {
    public JsonRpc2_0Pantheon(Web3jService web3jService) {
        super(web3jService);
    }
}
