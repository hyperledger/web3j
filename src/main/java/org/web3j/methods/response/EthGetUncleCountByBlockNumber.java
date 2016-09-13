package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getUncleCountByBlockNumber
 */
public class EthGetUncleCountByBlockNumber extends Response<String> {
    public BigInteger getUncleCount() {
        return Codec.decodeQuantity(getResult());
    }
}
