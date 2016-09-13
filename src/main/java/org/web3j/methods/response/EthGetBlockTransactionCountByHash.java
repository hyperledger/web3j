package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getBlockTransactionCountByHash
 */
public class EthGetBlockTransactionCountByHash extends Response<String> {
    public BigInteger getTransactionCount() {
        return Codec.decodeQuantity(getResult());
    }
}
