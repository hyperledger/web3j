package org.web3j.protocol.core;

import java.math.BigInteger;

public class DefaultBlockParameterFactory {

    public static DefaultBlockParameter valueOf(BigInteger blockNumber) {
        return new DefaultBlockParameterNumber(blockNumber);
    }

    public static DefaultBlockParameter valueOf(String blockName) {
        return DefaultBlockParameterName.fromString(blockName);
    }
}
