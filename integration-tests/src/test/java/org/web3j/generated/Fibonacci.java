package org.web3j.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.0.
 */
public class Fibonacci extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6101498061001e6000396000f30060606040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633c7fdc70811461005057806361047ff414610078575b600080fd5b341561005b57600080fd5b61006660043561008e565b60405190815260200160405180910390f35b341561008357600080fd5b6100666004356100da565b6000610099826100da565b90507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed828260405191825260208201526040908101905180910390a1919050565b60008115156100eb57506000610118565b81600114156100fc57506001610118565b610108600283036100da565b610114600184036100da565b0190505b9190505600a165627a7a72305820e408d0180959faf2f0168a105318c66ad9cee6a322569be9157dfc267cf450ce0029";

    protected Fibonacci(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Fibonacci(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<NotifyEventResponse> getNotifyEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Notify", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<NotifyEventResponse> responses = new ArrayList<NotifyEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NotifyEventResponse typedResponse = new NotifyEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.input = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.result = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NotifyEventResponse> notifyEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Notify", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, NotifyEventResponse>() {
            @Override
            public NotifyEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                NotifyEventResponse typedResponse = new NotifyEventResponse();
                typedResponse.log = log;
                typedResponse.input = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.result = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> fibonacciNotify(BigInteger number) {
        Function function = new Function(
                "fibonacciNotify", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(number)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> fibonacci(BigInteger number) {
        Function function = new Function("fibonacci", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(number)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<Fibonacci> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Fibonacci.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Fibonacci> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Fibonacci.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Fibonacci load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Fibonacci(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Fibonacci load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Fibonacci(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class NotifyEventResponse {
        public Log log;

        public BigInteger input;

        public BigInteger result;
    }
}
