package org.web3j.abi;

import java.util.concurrent.ExecutionException;

import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthCall;


/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
public abstract class Contract {
    private String contractAddress;
    private Web3j web3j;

    public Contract(String contractAddress, Web3j web3j) {
        this.contractAddress = contractAddress;
        this.web3j = web3j;
    }

    <T> T execute(Function function) throws InterruptedException, ExecutionException {
        String encoded = FunctionEncoder.encode(function);
        // where to get web3j instance from?
        org.web3j.protocol.core.methods.response.EthCall ethCall = web3j.ethCall(
                new EthCall(contractAddress, encoded),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        String value = ethCall.getValue();
        // decode
        // return value
        return null;
    }
}
