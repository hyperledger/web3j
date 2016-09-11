package org.web3j.methods.response;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

import java.math.BigInteger;

/**
 * eth_estimateGas
 */
public class EthEstimateGas extends Response<String> {
    public BigInteger getAmountUsed() {
        return Utils.decodeQuantity(getResult());
    }
}
