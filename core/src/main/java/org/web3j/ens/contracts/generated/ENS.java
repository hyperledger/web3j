package org.web3j.ens.contracts.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
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
import org.web3j.protocol.core.RemoteTransaction;
import org.web3j.protocol.core.generated.RemoteFunctionCall1;
import org.web3j.protocol.core.generated.RemoteTransaction0;
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
    private static final String BINARY = "608060405234801561001057600080fd5b5060008080526020527fad3228b676f7d3cd4284a5443f17f1962b36e491b30a40b2405849e597ba5fb58054600160a060020a03191633179055610500806100596000396000f3006080604052600436106100825763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416630178b8bf811461008757806302571be3146100bb57806306ab5923146100d357806314ab9038146100fc57806316a25cbd146101215780631896f70a146101565780635b0fc9c31461017a575b600080fd5b34801561009357600080fd5b5061009f60043561019e565b60408051600160a060020a039092168252519081900360200190f35b3480156100c757600080fd5b5061009f6004356101bc565b3480156100df57600080fd5b506100fa600435602435600160a060020a03604435166101d7565b005b34801561010857600080fd5b506100fa60043567ffffffffffffffff60243516610291565b34801561012d57600080fd5b5061013960043561035a565b6040805167ffffffffffffffff9092168252519081900360200190f35b34801561016257600080fd5b506100fa600435600160a060020a0360243516610391565b34801561018657600080fd5b506100fa600435600160a060020a0360243516610434565b600090815260208190526040902060010154600160a060020a031690565b600090815260208190526040902054600160a060020a031690565b6000838152602081905260408120548490600160a060020a031633146101fc57600080fd5b60408051868152602080820187905282519182900383018220600160a060020a03871683529251929450869288927fce0457fe73731f824cc272376169235128c118b49d344817417c6d108d155e8292908290030190a3506000908152602081905260409020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03929092169190911790555050565b6000828152602081905260409020548290600160a060020a031633146102b657600080fd5b6040805167ffffffffffffffff84168152905184917f1d4f9bbfc9cab89d66e1a1562f2233ccbf1308cb4f63de2ead5787adddb8fa68919081900360200190a250600091825260208290526040909120600101805467ffffffffffffffff90921674010000000000000000000000000000000000000000027fffffffff0000000000000000ffffffffffffffffffffffffffffffffffffffff909216919091179055565b60009081526020819052604090206001015474010000000000000000000000000000000000000000900467ffffffffffffffff1690565b6000828152602081905260409020548290600160a060020a031633146103b657600080fd5b60408051600160a060020a0384168152905184917f335721b01866dc23fbee8b6b2c7b1e14d6f05c28cd35a2c934239f94095602a0919081900360200190a250600091825260208290526040909120600101805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03909216919091179055565b6000828152602081905260409020548290600160a060020a0316331461045957600080fd5b60408051600160a060020a0384168152905184917fd4735d920b0f87494915f556dd9b54c8f309026070caea5c737245152564d266919081900360200190a250600091825260208290526040909120805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039092169190911790555600a165627a7a72305820f201bf7d54db31743dfa9e72c9529bab797c8e550d2355afb39d50e2ab48b4fb0029";

    public static final String FUNC_RESOLVER = "resolver";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SETSUBNODEOWNER = "setSubnodeOwner";

    public static final String FUNC_SETTTL = "setTTL";

    public static final String FUNC_TTL = "ttl";

    public static final String FUNC_SETRESOLVER = "setResolver";

    public static final String FUNC_SETOWNER = "setOwner";

    public static final Event NEWOWNER_EVENT = new Event("NewOwner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));

    public static final Event NEWRESOLVER_EVENT = new Event("NewResolver", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));

    public static final Event NEWTTL_EVENT = new Event("NewTTL", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Uint64>() {}));

    protected ENS(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected ENS(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> resolver(final byte[] node) {
        final Function function = new Function(FUNC_RESOLVER, 
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return new RemoteFunctionCall1<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteCall<String> owner(final byte[] node) {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return new RemoteFunctionCall1<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteTransaction<Void> setSubnodeOwner(final byte[] node, final byte[] label, final String owner) {
        final Function function = new Function(
                FUNC_SETSUBNODEOWNER, 
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node), 
                new org.web3j.abi.datatypes.generated.Bytes32(label), 
                new org.web3j.abi.datatypes.Address(owner)), 
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO, 
                false, gasProvider);
    }

    public RemoteTransaction<Void> setTTL(final byte[] node, final BigInteger ttl) {
        final Function function = new Function(
                FUNC_SETTTL, 
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node), 
                new org.web3j.abi.datatypes.generated.Uint64(ttl)), 
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO, false, gasProvider);
    }

    public RemoteCall<BigInteger> ttl(final byte[] node) {
        final Function function = new Function(FUNC_TTL, 
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return new RemoteFunctionCall1<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteTransaction<Void> setResolver(final byte[] node, final String resolver) {
        final Function function = new Function(
                FUNC_SETRESOLVER, 
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node), 
                new org.web3j.abi.datatypes.Address(resolver)), 
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO, false, gasProvider);
    }

    public RemoteTransaction<Void> setOwner(final byte[] node, final String owner) {
        final Function function = new Function(
                FUNC_SETOWNER, 
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node), 
                new org.web3j.abi.datatypes.Address(owner)), 
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO, false, gasProvider);
    }

    public static RemoteCall<ENS> deploy(final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ENS.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ENS> deploy(final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ENS.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    public List<NewOwnerEventResponse> getNewOwnerEvents(final TransactionReceipt transactionReceipt) {
        final List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWOWNER_EVENT, transactionReceipt);
        final ArrayList<NewOwnerEventResponse> responses = new ArrayList<NewOwnerEventResponse>(valueList.size());
        for (final Contract.EventValuesWithLog eventValues : valueList) {
            final NewOwnerEventResponse typedResponse = new NewOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.label = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewOwnerEventResponse> newOwnerEventFlowable(final EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewOwnerEventResponse>() {
            @Override
            public NewOwnerEventResponse apply(final Log log) {
                final Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWOWNER_EVENT, log);
                final NewOwnerEventResponse typedResponse = new NewOwnerEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.label = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewOwnerEventResponse> newOwnerEventFlowable(final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWOWNER_EVENT));
        return newOwnerEventFlowable(filter);
    }

    public List<TransferEventResponse> getTransferEvents(final TransactionReceipt transactionReceipt) {
        final List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        final ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (final Contract.EventValuesWithLog eventValues : valueList) {
            final TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(final EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(final Log log) {
                final Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                final TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public List<NewResolverEventResponse> getNewResolverEvents(final TransactionReceipt transactionReceipt) {
        final List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWRESOLVER_EVENT, transactionReceipt);
        final ArrayList<NewResolverEventResponse> responses = new ArrayList<NewResolverEventResponse>(valueList.size());
        for (final Contract.EventValuesWithLog eventValues : valueList) {
            final NewResolverEventResponse typedResponse = new NewResolverEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.resolver = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewResolverEventResponse> newResolverEventFlowable(final EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewResolverEventResponse>() {
            @Override
            public NewResolverEventResponse apply(final Log log) {
                final Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWRESOLVER_EVENT, log);
                final NewResolverEventResponse typedResponse = new NewResolverEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.resolver = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewResolverEventResponse> newResolverEventFlowable(final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWRESOLVER_EVENT));
        return newResolverEventFlowable(filter);
    }

    public List<NewTTLEventResponse> getNewTTLEvents(final TransactionReceipt transactionReceipt) {
        final List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWTTL_EVENT, transactionReceipt);
        final ArrayList<NewTTLEventResponse> responses = new ArrayList<NewTTLEventResponse>(valueList.size());
        for (final Contract.EventValuesWithLog eventValues : valueList) {
            final NewTTLEventResponse typedResponse = new NewTTLEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.ttl = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewTTLEventResponse> newTTLEventFlowable(final EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NewTTLEventResponse>() {
            @Override
            public NewTTLEventResponse apply(final Log log) {
                final Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWTTL_EVENT, log);
                final NewTTLEventResponse typedResponse = new NewTTLEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.ttl = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewTTLEventResponse> newTTLEventFlowable(final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWTTL_EVENT));
        return newTTLEventFlowable(filter);
    }
    
    public static ENS load(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider) {
        return new ENS(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ENS load(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider) {
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
