package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_getBalance
 */
public class EthGetBalance extends Response<String> {
    public BigInteger getBalance() {
        return Utils.decodeQuantity(getResult());
    }
}
