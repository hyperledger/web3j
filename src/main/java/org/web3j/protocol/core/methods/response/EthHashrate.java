package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.core.Response;

/**
 * eth_hashrate
 */
public class EthHashrate extends Response<String> {
    public BigInteger getHashrate() {
        return Codec.decodeQuantity(getResult());
    }
}
