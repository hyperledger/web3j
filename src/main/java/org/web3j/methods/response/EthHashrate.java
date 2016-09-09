package org.web3j.methods.response;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

import java.math.BigInteger;

/**
 * eth_hashrate
 */
public class EthHashrate extends Response<String> {
    public BigInteger getHashrate() {
        return Utils.decodeQuantity(getResult());
    }
}
