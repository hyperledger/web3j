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
 * <p>Generated with web3j version 3.5.0.
 */
public class Fibonacci extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061014f806100206000396000f30060806040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633c7fdc70811461005057806361047ff41461007a575b600080fd5b34801561005c57600080fd5b50610068600435610092565b60408051918252519081900360200190f35b34801561008657600080fd5b506100686004356100e0565b600061009d826100e0565b604080518481526020810183905281519293507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed929081900390910190a1919050565b60008115156100f15750600061011e565b81600114156101025750600161011e565b61010e600283036100e0565b61011a600184036100e0565b0190505b9190505600a165627a7a72305820c4ec03fb10cdd6bf64f4d1544ffb1cfbd033aaedffe67107fd541016a1ff57740029";

    public static final String FUNC_FIBONACCINOTIFY = "fibonacciNotify";

    public static final String FUNC_FIBONACCI = "fibonacci";

    public static final Event NOTIFY_EVENT = new Event("Notify", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected Fibonacci(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Fibonacci(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> fibonacciNotify(BigInteger number) {
        final Function function = new Function(
                FUNC_FIBONACCINOTIFY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(number)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> fibonacci(BigInteger number) {
        final Function function = new Function(FUNC_FIBONACCI, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(number)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<NotifyEventResponse> getNotifyEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NOTIFY_EVENT, transactionReceipt);
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

    public Observable<NotifyEventResponse> notifyEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, NotifyEventResponse>() {
            @Override
            public NotifyEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NOTIFY_EVENT, log);
                NotifyEventResponse typedResponse = new NotifyEventResponse();
                typedResponse.log = log;
                typedResponse.input = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.result = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<NotifyEventResponse> notifyEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NOTIFY_EVENT));
        return notifyEventObservable(filter);
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
