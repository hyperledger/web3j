package org.web3j.protocol.jsonrpc20;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.core.methods.request.EthSendTransaction;

/**
 * Common values used by integration tests.
 */
public abstract class IntegrationTestConfig {
    public abstract String validBlockHash();
    public abstract BigInteger validBlock();
    public abstract BigInteger validBlockTransactionCount();
    public abstract BigInteger validBlockUncleCount();
    public abstract String validAccount();
    public abstract String validContractAddress();
    public abstract String validContractAddressPositionZero();
    public abstract String validContractCode();
    public abstract EthSendTransaction ethSendTransaction();
    public abstract EthCall ethCall();
    public abstract String validTransactionHash();
    public abstract String validUncleBlockHash();
    public abstract BigInteger validUncleBlock();
}
