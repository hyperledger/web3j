package org.conor10.web3j.methods;

import org.conor10.web3j.protocol.Utils;
import org.conor10.web3j.protocol.jsonrpc20.Response;

/**
 * net_peerCount
 */
public class NetPeerCount extends Response<String> {

    public long getQuantity() {
        return Utils.decodeQuantity(getResult());
    }
}
