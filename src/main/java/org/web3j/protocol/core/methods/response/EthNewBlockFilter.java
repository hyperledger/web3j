package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.core.Response;

/**
 * eth_newBlockFilter
 */
public class EthNewBlockFilter extends Response<String> {
    public BigInteger getFilterId() {
        return Codec.decodeQuantity(getResult());
    }
}
