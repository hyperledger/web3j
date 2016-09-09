package org.web3j.protocol.jsonrpc20;

import java.math.BigInteger;

import org.web3j.protocol.Utils;

/**
 * DefaultBlockParameter implementation that takes a numeric value.
 */
public class DefaultBlockParameterNumber implements DefaultBlockParameter {

    private BigInteger blockNumber;

    public DefaultBlockParameterNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    @Override
    public String getValue() {
        return Utils.encodeQuantity(blockNumber);
    }
}
