package org.web3j.protocol.core;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.request.EthCall;
import org.web3j.protocol.core.methods.request.EthSendTransaction;

/**
 * Common values used by integration tests.
 */
public interface IntegrationTestConfig {
    String validBlockHash();
    BigInteger validBlock();
    BigInteger validBlockTransactionCount();
    BigInteger validBlockUncleCount();
    String validAccount();
    String validContractAddress();
    String validContractAddressPositionZero();
    String validContractCode();
    EthSendTransaction ethSendTransaction();
    EthCall ethCall();
    String validTransactionHash();
    String validUncleBlockHash();
    BigInteger validUncleBlock();
    String encodedEvent();
}
