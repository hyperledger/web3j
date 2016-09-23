package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.core.Response;

/**
 * eth_newFilter
 */
public class EthNewFilter extends Response<String> {
    public BigInteger getFilterId() {
        return Codec.decodeQuantity(getResult());
    }
}
