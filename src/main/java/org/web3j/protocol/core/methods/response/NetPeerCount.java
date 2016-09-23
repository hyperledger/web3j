package org.web3j.protocol.core.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.utils.Codec;
import org.web3j.protocol.core.Response;

/**
 * net_peerCount
 */
public class NetPeerCount extends Response<String> {

    public BigInteger getQuantity() {
        return Codec.decodeQuantity(getResult());
    }
}
