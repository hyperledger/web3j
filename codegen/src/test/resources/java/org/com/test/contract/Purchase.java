package org.com.test.contract;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.4.0.
 */
public class Purchase extends Contract {
    private static final String BINARY = "608060405260018054600160a060020a03191633179055600234046000819055600202341461008f57604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601560248201527f56616c75652068617320746f206265206576656e2e0000000000000000000000604482015290519081900360640190fd5b6105b58061009e6000396000f3006080604052600436106100825763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166308551a53811461008757806335a063b4146100b85780633fa4f245146100cf5780637150d8ae146100f657806373fac6f01461010b578063c19d93fb14610120578063d696069714610159575b600080fd5b34801561009357600080fd5b5061009c610161565b60408051600160a060020a039092168252519081900360200190f35b3480156100c457600080fd5b506100cd610170565b005b3480156100db57600080fd5b506100e46102d2565b60408051918252519081900360200190f35b34801561010257600080fd5b5061009c6102d8565b34801561011757600080fd5b506100cd6102e7565b34801561012c57600080fd5b5061013561048b565b6040518082600281111561014557fe5b60ff16815260200191505060405180910390f35b6100cd61049b565b600154600160a060020a031681565b600154600160a060020a031633146101d2576040805160e560020a62461bcd02815260206004820152601a60248201527f4f6e6c792073656c6c65722063616e2063616c6c20746869732e000000000000604482015290519081900360640190fd5b6000806002805460a060020a900460ff16908111156101ed57fe5b14610242576040805160e560020a62461bcd02815260206004820152600e60248201527f496e76616c69642073746174652e000000000000000000000000000000000000604482015290519081900360640190fd5b6040517f72c874aeff0b183a56e2b79c71b46e1aed4dee5e09862134b8821ba2fddbf8bf90600090a160028054819074ff0000000000000000000000000000000000000000191660a060020a820217905550600154604051600160a060020a0390911690303180156108fc02916000818181858888f193505050501580156102ce573d6000803e3d6000fd5b5050565b60005481565b600254600160a060020a031681565b600254600160a060020a03163314610349576040805160e560020a62461bcd02815260206004820152601960248201527f4f6e6c792062757965722063616e2063616c6c20746869732e00000000000000604482015290519081900360640190fd5b6001806002805460a060020a900460ff169081111561036457fe5b146103b9576040805160e560020a62461bcd02815260206004820152600e60248201527f496e76616c69642073746174652e000000000000000000000000000000000000604482015290519081900360640190fd5b6040517fe89152acd703c9d8c7d28829d443260b411454d45394e7995815140c8cbcbcf790600090a16002805474ff0000000000000000000000000000000000000000191674020000000000000000000000000000000000000000179081905560008054604051600160a060020a039093169281156108fc0292818181858888f19350505050158015610450573d6000803e3d6000fd5b50600154604051600160a060020a0390911690303180156108fc02916000818181858888f193505050501580156102ce573d6000803e3d6000fd5b60025460a060020a900460ff1681565b6000806002805460a060020a900460ff16908111156104b657fe5b1461050b576040805160e560020a62461bcd02815260206004820152600e60248201527f496e76616c69642073746174652e000000000000000000000000000000000000604482015290519081900360640190fd5b600054600202341480151561051f57600080fd5b6040517fd5d55c8a68912e9a110618df8d5e2e83b8d83211c57a8ddd1203df92885dc88190600090a150506002805473ffffffffffffffffffffffffffffffffffffffff1916331774ff0000000000000000000000000000000000000000191660a060020a1790555600a165627a7a7230582038fd26cc9b78a12bb7333df2d9cc6c687aa7f9c83f5e499d65579d0fc84658630029";

    public static final String FUNC_SELLER = "seller";

    public static final String FUNC_ABORT = "abort";

    public static final String FUNC_VALUE = "value";

    public static final String FUNC_BUYER = "buyer";

    public static final String FUNC_CONFIRMRECEIVED = "confirmReceived";

    public static final String FUNC_STATE = "state";

    public static final String FUNC_CONFIRMPURCHASE = "confirmPurchase";

    public static final Event ABORTED_EVENT = new Event("Aborted",
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event PURCHASECONFIRMED_EVENT = new Event("PurchaseConfirmed",
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event ITEMRECEIVED_EVENT = new Event("ItemReceived",
            Arrays.<TypeReference<?>>asList());
    ;

    @Deprecated
    protected Purchase(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Purchase(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Purchase(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Purchase(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> seller() {
        final Function function = new Function(FUNC_SELLER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> abort() {
        final Function function = new Function(
                FUNC_ABORT, 
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> value() {
        final Function function = new Function(FUNC_VALUE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> buyer() {
        final Function function = new Function(FUNC_BUYER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> confirmReceived() {
        final Function function = new Function(
                FUNC_CONFIRMRECEIVED, 
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> state() {
        final Function function = new Function(FUNC_STATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> confirmPurchase(BigInteger weiValue) {
        final Function function = new Function(
                FUNC_CONFIRMPURCHASE, 
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public List<AbortedEventResponse> getAbortedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ABORTED_EVENT, transactionReceipt);
        ArrayList<AbortedEventResponse> responses = new ArrayList<AbortedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AbortedEventResponse typedResponse = new AbortedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AbortedEventResponse> abortedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AbortedEventResponse>() {
            @Override
            public AbortedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ABORTED_EVENT, log);
                AbortedEventResponse typedResponse = new AbortedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Flowable<AbortedEventResponse> abortedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ABORTED_EVENT));
        return abortedEventFlowable(filter);
    }

    public List<PurchaseConfirmedEventResponse> getPurchaseConfirmedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PURCHASECONFIRMED_EVENT, transactionReceipt);
        ArrayList<PurchaseConfirmedEventResponse> responses = new ArrayList<PurchaseConfirmedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PurchaseConfirmedEventResponse typedResponse = new PurchaseConfirmedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PurchaseConfirmedEventResponse> purchaseConfirmedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, PurchaseConfirmedEventResponse>() {
            @Override
            public PurchaseConfirmedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PURCHASECONFIRMED_EVENT, log);
                PurchaseConfirmedEventResponse typedResponse = new PurchaseConfirmedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Flowable<PurchaseConfirmedEventResponse> purchaseConfirmedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PURCHASECONFIRMED_EVENT));
        return purchaseConfirmedEventFlowable(filter);
    }

    public List<ItemReceivedEventResponse> getItemReceivedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ITEMRECEIVED_EVENT, transactionReceipt);
        ArrayList<ItemReceivedEventResponse> responses = new ArrayList<ItemReceivedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ItemReceivedEventResponse typedResponse = new ItemReceivedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ItemReceivedEventResponse> itemReceivedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ItemReceivedEventResponse>() {
            @Override
            public ItemReceivedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ITEMRECEIVED_EVENT, log);
                ItemReceivedEventResponse typedResponse = new ItemReceivedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Flowable<ItemReceivedEventResponse> itemReceivedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ITEMRECEIVED_EVENT));
        return itemReceivedEventFlowable(filter);
    }

    @Deprecated
    public static Purchase load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Purchase(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Purchase load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Purchase(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Purchase load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Purchase(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Purchase load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Purchase(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Purchase> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger initialWeiValue) {
        return deployRemoteCall(Purchase.class, web3j, credentials, contractGasProvider, BINARY, "", initialWeiValue);
    }

    public static RemoteCall<Purchase> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger initialWeiValue) {
        return deployRemoteCall(Purchase.class, web3j, transactionManager, contractGasProvider, BINARY, "", initialWeiValue);
    }

    @Deprecated
    public static RemoteCall<Purchase> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployRemoteCall(Purchase.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    @Deprecated
    public static RemoteCall<Purchase> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployRemoteCall(Purchase.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static class AbortedEventResponse {
        public Log log;
    }

    public static class PurchaseConfirmedEventResponse {
        public Log log;
    }

    public static class ItemReceivedEventResponse {
        public Log log;
    }
}
