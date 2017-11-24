package org.web3j.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

/**
 * Transaction manager implementation for read-only operations on smart contracts.
 */
public class ReadonlyTransactionManager extends TransactionManager {

    public ReadonlyTransactionManager(Web3j web3j, String fromAddress) {
        super(web3j, fromAddress);
    }

    @Override
    public EthSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException {
        throw new UnsupportedOperationException(
                "Only read operations are supported by this transaction manager");
    }
}
