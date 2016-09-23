package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.core.Response;

/**
 * eth_estimateGas
 */
public class EthEstimateGas extends Response<String> {
    public BigInteger getAmountUsed() {
        return Codec.decodeQuantity(getResult());
    }
}
