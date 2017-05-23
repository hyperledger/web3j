package org.web3j.protocol;

import java.math.BigInteger;
import java.util.concurrent.ScheduledExecutorService;

import org.web3j.protocol.core.Ethereum;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.rx.Web3jRx;

/**
 * JSON-RPC Request object building factory.
 */
public interface Web3j extends Ethereum, Web3jRx {

}
