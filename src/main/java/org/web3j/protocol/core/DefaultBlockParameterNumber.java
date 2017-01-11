package org.web3j.protocol.core;

import com.fasterxml.jackson.annotation.JsonValue;

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
    @JsonValue
    public String getValue() {
        return Numeric.encodeQuantity(blockNumber);
    }
}
