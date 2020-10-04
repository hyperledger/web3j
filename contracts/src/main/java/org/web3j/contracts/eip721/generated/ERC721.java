package org.web3j.contracts.eip721.generated;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteTransactionCall;
import org.web3j.protocol.core.generated.RemoteCall1;
import org.web3j.protocol.core.generated.RemoteTransactionCall0;
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
 * <p>Generated with web3j version 4.1.1.
 */
public class ERC721 extends Contract {
    private static final String BINARY = "Bin file was not provided";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}));

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll", 
            Arrays.asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Bool>() {}));
    
    protected ERC721(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider, final TransactionReceipt transactionReceipt) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider, transactionReceipt);
    }
    
    protected ERC721(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider, final TransactionReceipt transactionReceipt) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider, transactionReceipt);
    }

    public RemoteCall<String> getApproved(final BigInteger _tokenId) {
        final Function function = new Function(FUNC_GETAPPROVED, 
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)),
                Collections.singletonList(new TypeReference<Address>() {
                }));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public RemoteTransactionCall<Void> approve(final String _approved, final BigInteger _tokenId, final BigInteger weiValue) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.asList(new org.web3j.abi.datatypes.Address(_approved), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.emptyList());
        return new RemoteTransactionCall0(web3j, function, contractAddress, true, transactionManager, defaultBlockParameter,
                FunctionEncoder.encode(function), weiValue, false, gasProvider);
    }

    public RemoteTransactionCall<Void> transferFrom(final String _from, final String _to, final BigInteger _tokenId, final BigInteger weiValue) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.emptyList());
        return new RemoteTransactionCall0(web3j, function, contractAddress, true, transactionManager, defaultBlockParameter,
                FunctionEncoder.encode(function), weiValue, false, gasProvider);
    }

    public RemoteTransactionCall<Void> safeTransferFrom(final String _from, final String _to, final BigInteger _tokenId, final BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SAFETRANSFERFROM, 
                Arrays.asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Collections.emptyList());
        return new RemoteTransactionCall0(web3j, function, contractAddress, true, transactionManager, defaultBlockParameter,
                FunctionEncoder.encode(function), weiValue, false, gasProvider);
    }

    public RemoteCall<String> ownerOf(final BigInteger _tokenId) {
        final Function function = new Function(FUNC_OWNEROF, 
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.asList(new TypeReference<Address>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public RemoteCall<BigInteger> balanceOf(final String _owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.asList(new org.web3j.abi.datatypes.Address(_owner)), 
                Arrays.asList(new TypeReference<Uint256>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public RemoteTransactionCall<Void> setApprovalForAll(final String _operator, final Boolean _approved) {
        final Function function = new Function(
                FUNC_SETAPPROVALFORALL, 
                Arrays.asList(new org.web3j.abi.datatypes.Address(_operator), 
                new org.web3j.abi.datatypes.Bool(_approved)), 
                Collections.emptyList());
        return new RemoteTransactionCall0(web3j, function, contractAddress, true, transactionManager, defaultBlockParameter,
                FunctionEncoder.encode(function), BigInteger.ZERO, false, gasProvider);
    }

    public RemoteTransactionCall<Void> safeTransferFrom(final String _from, final String _to, final BigInteger _tokenId, final byte[] data, final BigInteger weiValue) {
        final Function function = new Function(
                FUNC_SAFETRANSFERFROM, 
                Arrays.asList(new org.web3j.abi.datatypes.Address(_from), 
                new org.web3j.abi.datatypes.Address(_to), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenId), 
                new org.web3j.abi.datatypes.DynamicBytes(data)), 
                Collections.emptyList());
        return new RemoteTransactionCall0(web3j, function, contractAddress, true, transactionManager, defaultBlockParameter,
                FunctionEncoder.encode(function), weiValue, false, gasProvider);
    }

    public RemoteCall<Boolean> isApprovedForAll(final String _owner, final String _operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL, 
                Arrays.asList(new org.web3j.abi.datatypes.Address(_owner), 
                new org.web3j.abi.datatypes.Address(_operator)), 
                Arrays.asList(new TypeReference<Bool>() {}));
        return new RemoteCall1<>(function, contractAddress, true, transactionManager, defaultBlockParameter);
    }

    public List<TransferEventResponse> getTransferEvents(final TransactionReceipt transactionReceipt) {
        final List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        final ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (final Contract.EventValuesWithLog eventValues : valueList) {
            final TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
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
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(final TransactionReceipt transactionReceipt) {
        final List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        final ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (final Contract.EventValuesWithLog eventValues : valueList) {
            final ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._approved = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(final EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(final Log log) {
                final Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                final ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._approved = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public List<ApprovalForAllEventResponse> getApprovalForAllEvents(final TransactionReceipt transactionReceipt) {
        final List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        final ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<ApprovalForAllEventResponse>(valueList.size());
        for (final Contract.EventValuesWithLog eventValues : valueList) {
            final ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(final EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ApprovalForAllEventResponse>() {
            @Override
            public ApprovalForAllEventResponse apply(final Log log) {
                final Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVALFORALL_EVENT, log);
                final ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._operator = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalForAllEventResponse> approvalForAllEventFlowable(
            final DefaultBlockParameter startBlock, final DefaultBlockParameter endBlock) {
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventFlowable(filter);
    }

    public static ERC721 load(final String contractAddress, final Web3j web3j, final Credentials credentials, final ContractGasProvider contractGasProvider) {
        return new ERC721(contractAddress, web3j, credentials, contractGasProvider, null);
    }

    public static ERC721 load(final String contractAddress, final Web3j web3j, final TransactionManager transactionManager, final ContractGasProvider contractGasProvider) {
        return new ERC721(contractAddress, web3j, transactionManager, contractGasProvider, null);
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _tokenId;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _approved;

        public BigInteger _tokenId;
    }

    public static class ApprovalForAllEventResponse {
        public Log log;

        public String _owner;

        public String _operator;

        public Boolean _approved;
    }
}
