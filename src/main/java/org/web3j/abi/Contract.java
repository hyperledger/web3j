package org.web3j.abi;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
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

    protected <T extends Type> T executeSingleValueReturn(Function function) throws InterruptedException, ExecutionException {
        List<T> values = execute(function);
        return values.get(0);
    }

    protected <T extends Type> List<T> executeMultipleValueReturn(Function function) throws InterruptedException, ExecutionException {
        return execute(function);
    }

    private <T extends Type> List<T> execute(Function function) throws InterruptedException, ExecutionException {
        String encoded = FunctionEncoder.encode(function);
        org.web3j.protocol.core.methods.response.EthCall ethCall = web3j.ethCall(
                new EthCall(contractAddress, encoded),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();

        String value = ethCall.getValue();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }
}
