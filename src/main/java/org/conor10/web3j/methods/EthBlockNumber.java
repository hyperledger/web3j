package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.Utils;
import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * eth_blockNumber
 */
public class EthBlockNumber extends Response<String> {
    public long getBlockNumber() {
        return Utils.decodeQuantity(getResult());
    }
}
