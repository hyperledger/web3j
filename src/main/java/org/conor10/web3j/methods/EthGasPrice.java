package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.Utils;
import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * eth_gasPrice
 */
public class EthGasPrice extends Response<String> {
    public long getGasPrice() {
        return Utils.decodeQuantity(getResult());
    }
}
