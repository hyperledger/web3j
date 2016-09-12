package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_blockNumber
 */
public class EthBlockNumber extends Response<String> {
    public BigInteger getBlockNumber() {
        return Utils.decodeQuantity(getResult());
    }
}
