package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getUncleCountByBlockHash
 */
public class EthGetUncleCountByBlockHash extends Response<String> {
    public BigInteger getUncleCount() {
        return Utils.decodeQuantity(getResult());
    }
}
