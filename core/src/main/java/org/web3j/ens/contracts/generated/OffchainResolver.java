package org.web3j.ens.contracts.generated;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class OffchainResolver extends PublicResolver {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_MAKESIGNATUREHASH = "makeSignatureHash";

    public static final String FUNC_RESOLVE = "resolve";

    public static final String FUNC_RESOLVEWITHPROOF = "resolveWithProof";

    public static final String FUNC_SIGNERS = "signers";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_URL = "url";

    public static final String FUNC_VERIFY = "verify";

    public static final Event NEWSIGNERS_EVENT = new Event("NewSigners",
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
    ;

    public OffchainResolver(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public OffchainResolver(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(contractAddress, web3j, credentials, contractGasProvider);
    }

    public OffchainResolver(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public OffchainResolver(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<NewSignersEventResponse> getNewSignersEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWSIGNERS_EVENT, transactionReceipt);
        ArrayList<NewSignersEventResponse> responses = new ArrayList<NewSignersEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewSignersEventResponse typedResponse = new NewSignersEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.signers = (List<String>) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewSignersEventResponse> newSignersEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewSignersEventResponse>() {
            @Override
            public NewSignersEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWSIGNERS_EVENT, log);
                NewSignersEventResponse typedResponse = new NewSignersEventResponse();
                typedResponse.log = log;
                typedResponse.signers = (List<String>) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewSignersEventResponse> newSignersEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWSIGNERS_EVENT));
        return newSignersEventFlowable(filter);
    }

    public RemoteFunctionCall<byte[]> makeSignatureHash(String target, BigInteger expires, byte[] request, byte[] result) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MAKESIGNATUREHASH,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, target),
                new org.web3j.abi.datatypes.generated.Uint64(expires),
                new org.web3j.abi.datatypes.DynamicBytes(request),
                new org.web3j.abi.datatypes.DynamicBytes(result)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<String> resolve(byte[] name, byte[] data) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RESOLVE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(name),
                new org.web3j.abi.datatypes.DynamicBytes(data)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));

        return new RemoteFunctionCall<>(function, () -> Numeric.cleanString(executeCallWithoutDecoding(function)));
    }

    public String executeCallWithoutDecoding(String encodedFunction) throws IOException {
        return call(contractAddress, encodedFunction, defaultBlockParameter);
    }

    public RemoteFunctionCall<String> resolveWithProof(byte[] response, byte[] extraData) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RESOLVEWITHPROOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(response),
                new org.web3j.abi.datatypes.DynamicBytes(extraData)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        return  new RemoteFunctionCall<>(function, () -> Numeric.cleanString(executeCallWithoutDecoding(function)));
    }

    public RemoteFunctionCall<Boolean> signers(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SIGNERS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceID) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUPPORTSINTERFACE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> url() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_URL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<byte[]> verify(byte[] request, byte[] response) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_VERIFY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(request),
                new org.web3j.abi.datatypes.DynamicBytes(response)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    @Deprecated
    public static OffchainResolver load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new OffchainResolver(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static OffchainResolver load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new OffchainResolver(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static OffchainResolver load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new OffchainResolver(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static OffchainResolver load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new OffchainResolver(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class NewSignersEventResponse extends BaseEventResponse {
        public List<String> signers;
    }
}
