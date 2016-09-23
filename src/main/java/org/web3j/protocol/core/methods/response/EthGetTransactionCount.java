package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.core.Response;

/**
 * eth_getTransactionCount
 */
public class EthGetTransactionCount extends Response<String> {
    public BigInteger getTransactionCount() {
        return Codec.decodeQuantity(getResult());
    }
}
