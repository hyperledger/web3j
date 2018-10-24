package org.web3j.ens.contracts.generated;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint64;
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

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.0.0.
 */
public class ENS extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60008080526020527fad3228b676f7d3cd4284a5443f17f1962b36e491b30a40b2405849e597ba5fb58054600160a060020a033316600160a060020a0319909116179055610501806100626000396000f300606060405236156100805763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416630178b8bf811461008557806302571be3146100b757806306ab5923146100cd57806314ab9038146100f457806316a25cbd146101175780631896f70a1461014a5780635b0fc9c31461016c575b600080fd5b341561009057600080fd5b61009b60043561018e565b604051600160a060020a03909116815260200160405180910390f35b34156100c257600080fd5b61009b6004356101ac565b34156100d857600080fd5b6100f2600435602435600160a060020a03604435166101c7565b005b34156100ff57600080fd5b6100f260043567ffffffffffffffff60243516610289565b341561012257600080fd5b61012d600435610355565b60405167ffffffffffffffff909116815260200160405180910390f35b341561015557600080fd5b6100f2600435600160a060020a036024351661038c565b341561017757600080fd5b6100f2600435600160a060020a0360243516610432565b600090815260208190526040902060010154600160a060020a031690565b600090815260208190526040902054600160a060020a031690565b600083815260208190526040812054849033600160a060020a039081169116146101f057600080fd5b8484604051918252602082015260409081019051908190039020915083857fce0457fe73731f824cc272376169235128c118b49d344817417c6d108d155e8285604051600160a060020a03909116815260200160405180910390a3506000908152602081905260409020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03929092169190911790555050565b600082815260208190526040902054829033600160a060020a039081169116146102b257600080fd5b827f1d4f9bbfc9cab89d66e1a1562f2233ccbf1308cb4f63de2ead5787adddb8fa688360405167ffffffffffffffff909116815260200160405180910390a250600091825260208290526040909120600101805467ffffffffffffffff90921674010000000000000000000000000000000000000000027fffffffff0000000000000000ffffffffffffffffffffffffffffffffffffffff909216919091179055565b60009081526020819052604090206001015474010000000000000000000000000000000000000000900467ffffffffffffffff1690565b600082815260208190526040902054829033600160a060020a039081169116146103b557600080fd5b827f335721b01866dc23fbee8b6b2c7b1e14d6f05c28cd35a2c934239f94095602a083604051600160a060020a03909116815260200160405180910390a250600091825260208290526040909120600101805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03909216919091179055565b600082815260208190526040902054829033600160a060020a0390811691161461045b57600080fd5b827fd4735d920b0f87494915f556dd9b54c8f309026070caea5c737245152564d26683604051600160a060020a03909116815260200160405180910390a250600091825260208290526040909120805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039092169190911790555600a165627a7a72305820a86d8d2e491350c21d0c58f7abc7e46f74b3be5b8a5d8afdfc73c77bb3b293150029";

    public static final String FUNC_RESOLVER = "resolver";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SETSUBNODEOWNER = "setSubnodeOwner";

    public static final String FUNC_SETTTL = "setTTL";

    public static final String FUNC_TTL = "ttl";

    public static final String FUNC_SETRESOLVER = "setResolver";

    public static final String FUNC_SETOWNER = "setOwner";

    public static final Event NEWOWNER_EVENT = new Event("NewOwner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWRESOLVER_EVENT = new Event("NewResolver", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));
    ;

    public static final Event NEWTTL_EVENT = new Event("NewTTL", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Uint64>() {}));
    ;

    @Deprecated
    protected ENS(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ENS(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ENS(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ENS(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> resolver(byte[] node) {
        final Function function = new Function(FUNC_RESOLVER, 
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner(byte[] node) {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setSubnodeOwner(byte[] node, byte[] label, String owner) {
        final Function function = new Function(
                FUNC_SETSUBNODEOWNER, 
                Arrays.<Type>asList(new Bytes32(node),
                new Bytes32(label),
                new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setTTL(byte[] node, BigInteger ttl) {
        final Function function = new Function(
                FUNC_SETTTL, 
                Arrays.<Type>asList(new Bytes32(node),
                new Uint64(ttl)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> ttl(byte[] node) {
        final Function function = new Function(FUNC_TTL, 
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setResolver(byte[] node, String resolver) {
        final Function function = new Function(
                FUNC_SETRESOLVER, 
                Arrays.<Type>asList(new Bytes32(node),
                new Address(resolver)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setOwner(byte[] node, String owner) {
        final Function function = new Function(
                FUNC_SETOWNER, 
                Arrays.<Type>asList(new Bytes32(node),
                new Address(owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<ENS> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ENS.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ENS> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ENS.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ENS> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ENS.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ENS> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ENS.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public List<NewOwnerEventResponse> getNewOwnerEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWOWNER_EVENT, transactionReceipt);
        ArrayList<NewOwnerEventResponse> responses = new ArrayList<NewOwnerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewOwnerEventResponse typedResponse = new NewOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.label = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewOwnerEventResponse> newOwnerEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewOwnerEventResponse>() {
            @Override
            public NewOwnerEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWOWNER_EVENT, log);
                NewOwnerEventResponse typedResponse = new NewOwnerEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.label = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewOwnerEventResponse> newOwnerEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWOWNER_EVENT));
        return newOwnerEventFlowable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public List<NewResolverEventResponse> getNewResolverEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWRESOLVER_EVENT, transactionReceipt);
        ArrayList<NewResolverEventResponse> responses = new ArrayList<NewResolverEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewResolverEventResponse typedResponse = new NewResolverEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.resolver = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewResolverEventResponse> newResolverEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewResolverEventResponse>() {
            @Override
            public NewResolverEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWRESOLVER_EVENT, log);
                NewResolverEventResponse typedResponse = new NewResolverEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.resolver = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewResolverEventResponse> newResolverEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWRESOLVER_EVENT));
        return newResolverEventFlowable(filter);
    }

    public List<NewTTLEventResponse> getNewTTLEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWTTL_EVENT, transactionReceipt);
        ArrayList<NewTTLEventResponse> responses = new ArrayList<NewTTLEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewTTLEventResponse typedResponse = new NewTTLEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ttl = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewTTLEventResponse> newTTLEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewTTLEventResponse>() {
            @Override
            public NewTTLEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWTTL_EVENT, log);
                NewTTLEventResponse typedResponse = new NewTTLEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ttl = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewTTLEventResponse> newTTLEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWTTL_EVENT));
        return newTTLEventFlowable(filter);
    }

    @Deprecated
    public static ENS load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ENS(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ENS load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ENS(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ENS load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ENS(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ENS load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ENS(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class NewOwnerEventResponse {
        public Log log;

        public byte[] node;

        public byte[] label;

        public String owner;
    }

    public static class TransferEventResponse {
        public Log log;

        public byte[] node;

        public String owner;
    }

    public static class NewResolverEventResponse {
        public Log log;

        public byte[] node;

        public String resolver;
    }

    public static class NewTTLEventResponse {
        public Log log;

        public byte[] node;

        public BigInteger ttl;
    }
}
