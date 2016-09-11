package org.web3j.methods.response;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

import java.math.BigInteger;

/**
 * eth_newBlockFilter
 */
public class EthNewBlockFilter extends Response<String> {
    public BigInteger getFilterId() {
        return Utils.decodeQuantity(getResult());
    }
}
