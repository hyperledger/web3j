package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_gasPrice
 */
public class EthGasPrice extends Response<String> {
    public BigInteger getGasPrice() {
        return Codec.decodeQuantity(getResult());
    }
}
