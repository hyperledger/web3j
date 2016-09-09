package org.web3j.methods.response;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

import java.math.BigInteger;

/**
 * eth_getBalance
 */
public class EthGetBalance extends Response<String> {
    public BigInteger getBalance() {
        return Utils.decodeQuantity(getResult());
    }
}
