package org.web3j.protocol.core;

/**
 * Wrapper for parameter that takes either a block number or block name as input
 * <p>
 * See the <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#the-default-block-parameter">specification</a> for further information.
 */
public interface DefaultBlockParameter {
    String getValue();
}
