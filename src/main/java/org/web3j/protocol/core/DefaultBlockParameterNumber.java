package org.web3j.protocol.core;

import java.math.BigInteger;

import org.web3j.utils.Numeric;

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
        return Numeric.encodeQuantity(blockNumber);
    }

    static DefaultBlockParameter valueOf(BigInteger blockNumber) {
        return new DefaultBlockParameterNumber(blockNumber);
    }
}
