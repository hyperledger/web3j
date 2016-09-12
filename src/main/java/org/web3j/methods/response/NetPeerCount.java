package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * net_peerCount
 */
public class NetPeerCount extends Response<String> {

    public BigInteger getQuantity() {
        return Utils.decodeQuantity(getResult());
    }
}
