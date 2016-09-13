package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getBalance
 */
public class EthGetBalance extends Response<String> {
    public BigInteger getBalance() {
        return Codec.decodeQuantity(getResult());
    }
}
