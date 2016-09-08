package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.Utils;
import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getBalance
 */
public class EthGetBalance extends Response<String> {
    public long getBalance() {
        return Utils.decodeQuantity(getResult());
    }
}
