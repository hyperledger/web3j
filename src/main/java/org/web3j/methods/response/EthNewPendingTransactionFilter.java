package org.web3j.methods.response;

import java.math.BigInteger;

import org.web3j.protocol.Utils;
import org.web3j.protocol.jsonrpc20.Response;

/**
 * eth_newPendingTransactionFilter
 */
public class EthNewPendingTransactionFilter extends Response<String> {
    public BigInteger getFilterId() {
        return Utils.decodeQuantity(getResult());
    }
}
