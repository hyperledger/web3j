package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.Utils;
import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * eth_hashrate
 */
public class EthHashrate extends Response<String> {
    public long getHashrate() {
        return Utils.decodeQuantity(getResult());
    }
}
