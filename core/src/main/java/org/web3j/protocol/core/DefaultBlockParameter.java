package org.web3j.protocol.core;

import java.math.BigInteger;

/**
 * Wrapper for parameter that takes either a block number or block name as input
 *
 * <p>See the <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter">
 *     specification</a> for further information.
 */
public interface DefaultBlockParameter {
    static DefaultBlockParameter valueOf(BigInteger blockNumber) {
        if (BigInteger.ZERO.compareTo(blockNumber) >= 0)
            blockNumber = BigInteger.ZERO;
        return new DefaultBlockParameterNumber(blockNumber);
    }

    static DefaultBlockParameter valueOf(String blockName) {
        return DefaultBlockParameterName.fromString(blockName);
    }

    String getValue();
}
