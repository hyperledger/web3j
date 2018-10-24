package org.web3j.ens.contracts.generated;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
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
public class PublicResolver extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6040516020806111b08339810160405280805160008054600160a060020a03909216600160a060020a0319909216919091179055505061115c806100546000396000f300606060405236156100a95763ffffffff60e060020a60003504166301ffc9a781146100ae57806310f13a8c146100e25780632203ab561461017c57806329cd62ea146102135780632dff69411461022f5780633b3b57de1461025757806359d1d43c14610289578063623195b014610356578063691f3431146103b257806377372213146103c8578063c3d014d61461041e578063c869023314610437578063d5fa2b0014610465575b600080fd5b34156100b957600080fd5b6100ce600160e060020a031960043516610487565b604051901515815260200160405180910390f35b34156100ed57600080fd5b61017a600480359060446024803590810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405281815292919060208401838380828437509496506105f495505050505050565b005b341561018757600080fd5b610195600435602435610805565b60405182815260406020820181815290820183818151815260200191508051906020019080838360005b838110156101d75780820151838201526020016101bf565b50505050905090810190601f1680156102045780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b341561021e57600080fd5b61017a60043560243560443561092f565b341561023a57600080fd5b610245600435610a2e565b60405190815260200160405180910390f35b341561026257600080fd5b61026d600435610a44565b604051600160a060020a03909116815260200160405180910390f35b341561029457600080fd5b6102df600480359060446024803590810190830135806020601f82018190048102016040519081016040528181529291906020840183838082843750949650610a5f95505050505050565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561031b578082015183820152602001610303565b50505050905090810190601f1680156103485780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561036157600080fd5b61017a600480359060248035919060649060443590810190830135806020601f82018190048102016040519081016040528181529291906020840183838082843750949650610b7e95505050505050565b34156103bd57600080fd5b6102df600435610c7a565b34156103d357600080fd5b61017a600480359060446024803590810190830135806020601f82018190048102016040519081016040528181529291906020840183838082843750949650610d4095505050505050565b341561042957600080fd5b61017a600435602435610e8a565b341561044257600080fd5b61044d600435610f63565b60405191825260208201526040908101905180910390f35b341561047057600080fd5b61017a600435600160a060020a0360243516610f80565b6000600160e060020a031982167f3b3b57de0000000000000000000000000000000000000000000000000000000014806104ea5750600160e060020a031982167fd8389dc500000000000000000000000000000000000000000000000000000000145b8061051e5750600160e060020a031982167f691f343100000000000000000000000000000000000000000000000000000000145b806105525750600160e060020a031982167f2203ab5600000000000000000000000000000000000000000000000000000000145b806105865750600160e060020a031982167fc869023300000000000000000000000000000000000000000000000000000000145b806105ba5750600160e060020a031982167f59d1d43c00000000000000000000000000000000000000000000000000000000145b806105ee5750600160e060020a031982167f01ffc9a700000000000000000000000000000000000000000000000000000000145b92915050565b600080548491600160a060020a033381169216906302571be39084906040516020015260405160e060020a63ffffffff84160281526004810191909152602401602060405180830381600087803b151561064d57600080fd5b6102c65a03f1151561065e57600080fd5b50505060405180519050600160a060020a031614151561067d57600080fd5b6000848152600160205260409081902083916005909101908590518082805190602001908083835b602083106106c45780518252601f1990920191602091820191016106a5565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020908051610708929160200190611083565b50826040518082805190602001908083835b602083106107395780518252601f19909201916020918201910161071a565b6001836020036101000a0380198251168184511617909252505050919091019250604091505051908190039020847fd8c9334b1a9c2f9da342a0a2b32629c1a229b6445dad78947f674b44444a75508560405160208082528190810183818151815260200191508051906020019080838360005b838110156107c55780820151838201526020016107ad565b50505050905090810190601f1680156107f25780820380516001836020036101000a031916815260200191505b509250505060405180910390a350505050565b600061080f611101565b60008481526001602081905260409091209092505b838311610922578284161580159061085d5750600083815260068201602052604081205460026000196101006001841615020190911604115b15610917578060060160008481526020019081526020016000208054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561090b5780601f106108e05761010080835404028352916020019161090b565b820191906000526020600020905b8154815290600101906020018083116108ee57829003601f168201915b50505050509150610927565b600290920291610824565b600092505b509250929050565b600080548491600160a060020a033381169216906302571be39084906040516020015260405160e060020a63ffffffff84160281526004810191909152602401602060405180830381600087803b151561098857600080fd5b6102c65a03f1151561099957600080fd5b50505060405180519050600160a060020a03161415156109b857600080fd5b6040805190810160409081528482526020808301859052600087815260019091522060030181518155602082015160019091015550837f1d6f5e03d3f63eb58751986629a5439baee5079ff04f345becb66e23eb154e46848460405191825260208201526040908101905180910390a250505050565b6000908152600160208190526040909120015490565b600090815260016020526040902054600160a060020a031690565b610a67611101565b60008381526001602052604090819020600501908390518082805190602001908083835b60208310610aaa5780518252601f199092019160209182019101610a8b565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b715780601f10610b4657610100808354040283529160200191610b71565b820191906000526020600020905b815481529060010190602001808311610b5457829003601f168201915b5050505050905092915050565b600080548491600160a060020a033381169216906302571be39084906040516020015260405160e060020a63ffffffff84160281526004810191909152602401602060405180830381600087803b1515610bd757600080fd5b6102c65a03f11515610be857600080fd5b50505060405180519050600160a060020a0316141515610c0757600080fd5b6000198301831615610c1857600080fd5b60008481526001602090815260408083208684526006019091529020828051610c45929160200190611083565b5082847faa121bbeef5f32f5961a2a28966e769023910fc9479059ee3495d4c1a696efe360405160405180910390a350505050565b610c82611101565b6001600083600019166000191681526020019081526020016000206002018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610d345780601f10610d0957610100808354040283529160200191610d34565b820191906000526020600020905b815481529060010190602001808311610d1757829003601f168201915b50505050509050919050565b600080548391600160a060020a033381169216906302571be39084906040516020015260405160e060020a63ffffffff84160281526004810191909152602401602060405180830381600087803b1515610d9957600080fd5b6102c65a03f11515610daa57600080fd5b50505060405180519050600160a060020a0316141515610dc957600080fd5b6000838152600160205260409020600201828051610deb929160200190611083565b50827fb7d29e911041e8d9b843369e890bcb72c9388692ba48b65ac54e7214c4c348f78360405160208082528190810183818151815260200191508051906020019080838360005b83811015610e4b578082015183820152602001610e33565b50505050905090810190601f168015610e785780820380516001836020036101000a031916815260200191505b509250505060405180910390a2505050565b600080548391600160a060020a033381169216906302571be39084906040516020015260405160e060020a63ffffffff84160281526004810191909152602401602060405180830381600087803b1515610ee357600080fd5b6102c65a03f11515610ef457600080fd5b50505060405180519050600160a060020a0316141515610f1357600080fd5b6000838152600160208190526040918290200183905583907f0424b6fe0d9c3bdbece0e7879dc241bb0c22e900be8b6c168b4ee08bd9bf83bc9084905190815260200160405180910390a2505050565b600090815260016020526040902060038101546004909101549091565b600080548391600160a060020a033381169216906302571be39084906040516020015260405160e060020a63ffffffff84160281526004810191909152602401602060405180830381600087803b1515610fd957600080fd5b6102c65a03f11515610fea57600080fd5b50505060405180519050600160a060020a031614151561100957600080fd5b60008381526001602052604090819020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03851617905583907f52d7d861f09ab3d26239d492e8968629f95e9e318cf0b73bfddc441522a15fd290849051600160a060020a03909116815260200160405180910390a2505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106110c457805160ff19168380011785556110f1565b828001600101855582156110f1579182015b828111156110f15782518255916020019190600101906110d6565b506110fd929150611113565b5090565b60206040519081016040526000815290565b61112d91905b808211156110fd5760008155600101611119565b905600a165627a7a723058202aec3c68797bf2d53a514b8e41f08c3e96a6f341417e8ba6558fb76d1d76c21c0029";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SETTEXT = "setText";

    public static final String FUNC_ABI = "ABI";

    public static final String FUNC_SETPUBKEY = "setPubkey";

    public static final String FUNC_CONTENT = "content";

    public static final String FUNC_ADDR = "addr";

    public static final String FUNC_TEXT = "text";

    public static final String FUNC_SETABI = "setABI";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SETNAME = "setName";

    public static final String FUNC_SETCONTENT = "setContent";

    public static final String FUNC_PUBKEY = "pubkey";

    public static final String FUNC_SETADDR = "setAddr";

    public static final Event ADDRCHANGED_EVENT = new Event("AddrChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>() {}));
    ;

    public static final Event CONTENTCHANGED_EVENT = new Event("ContentChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event NAMECHANGED_EVENT = new Event("NameChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event ABICHANGED_EVENT = new Event("ABIChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event PUBKEYCHANGED_EVENT = new Event("PubkeyChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
    ;

    public static final Event TEXTCHANGED_EVENT = new Event("TextChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected PublicResolver(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PublicResolver(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PublicResolver(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PublicResolver(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] interfaceID) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceID)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> setText(byte[] node, String key, String value) {
        final Function function = new Function(
                FUNC_SETTEXT, 
                Arrays.<Type>asList(new Bytes32(node),
                new Utf8String(key),
                new Utf8String(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple2<BigInteger, byte[]>> ABI(byte[] node, BigInteger contentTypes) {
        final Function function = new Function(FUNC_ABI, 
                Arrays.<Type>asList(new Bytes32(node),
                new Uint256(contentTypes)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}));
        return new RemoteCall<Tuple2<BigInteger, byte[]>>(
                new Callable<Tuple2<BigInteger, byte[]>>() {
                    @Override
                    public Tuple2<BigInteger, byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, byte[]>(
                                (BigInteger) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> setPubkey(byte[] node, byte[] x, byte[] y) {
        final Function function = new Function(
                FUNC_SETPUBKEY, 
                Arrays.<Type>asList(new Bytes32(node),
                new Bytes32(x),
                new Bytes32(y)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<byte[]> content(byte[] node) {
        final Function function = new Function(FUNC_CONTENT, 
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<String> addr(byte[] node) {
        final Function function = new Function(FUNC_ADDR, 
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> text(byte[] node, String key) {
        final Function function = new Function(FUNC_TEXT, 
                Arrays.<Type>asList(new Bytes32(node),
                new Utf8String(key)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setABI(byte[] node, BigInteger contentType, byte[] data) {
        final Function function = new Function(
                FUNC_SETABI, 
                Arrays.<Type>asList(new Bytes32(node),
                new Uint256(contentType),
                new DynamicBytes(data)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> name(byte[] node) {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setName(byte[] node, String name) {
        final Function function = new Function(
                FUNC_SETNAME, 
                Arrays.<Type>asList(new Bytes32(node),
                new Utf8String(name)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setContent(byte[] node, byte[] hash) {
        final Function function = new Function(
                FUNC_SETCONTENT, 
                Arrays.<Type>asList(new Bytes32(node),
                new Bytes32(hash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple2<byte[], byte[]>> pubkey(byte[] node) {
        final Function function = new Function(FUNC_PUBKEY, 
                Arrays.<Type>asList(new Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple2<byte[], byte[]>>(
                new Callable<Tuple2<byte[], byte[]>>() {
                    @Override
                    public Tuple2<byte[], byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<byte[], byte[]>(
                                (byte[]) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> setAddr(byte[] node, String addr) {
        final Function function = new Function(
                FUNC_SETADDR, 
                Arrays.<Type>asList(new Bytes32(node),
                new Address(addr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<PublicResolver> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String ensAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(ensAddr)));
        return deployRemoteCall(PublicResolver.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PublicResolver> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String ensAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(ensAddr)));
        return deployRemoteCall(PublicResolver.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PublicResolver> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String ensAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(ensAddr)));
        return deployRemoteCall(PublicResolver.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<PublicResolver> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String ensAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(ensAddr)));
        return deployRemoteCall(PublicResolver.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public List<AddrChangedEventResponse> getAddrChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ADDRCHANGED_EVENT, transactionReceipt);
        ArrayList<AddrChangedEventResponse> responses = new ArrayList<AddrChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddrChangedEventResponse typedResponse = new AddrChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddrChangedEventResponse> addrChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AddrChangedEventResponse>() {
            @Override
            public AddrChangedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ADDRCHANGED_EVENT, log);
                AddrChangedEventResponse typedResponse = new AddrChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddrChangedEventResponse> addrChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDRCHANGED_EVENT));
        return addrChangedEventFlowable(filter);
    }

    public List<ContentChangedEventResponse> getContentChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CONTENTCHANGED_EVENT, transactionReceipt);
        ArrayList<ContentChangedEventResponse> responses = new ArrayList<ContentChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ContentChangedEventResponse typedResponse = new ContentChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContentChangedEventResponse> contentChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ContentChangedEventResponse>() {
            @Override
            public ContentChangedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CONTENTCHANGED_EVENT, log);
                ContentChangedEventResponse typedResponse = new ContentChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContentChangedEventResponse> contentChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTENTCHANGED_EVENT));
        return contentChangedEventFlowable(filter);
    }

    public List<NameChangedEventResponse> getNameChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NAMECHANGED_EVENT, transactionReceipt);
        ArrayList<NameChangedEventResponse> responses = new ArrayList<NameChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NameChangedEventResponse typedResponse = new NameChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NameChangedEventResponse> nameChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NameChangedEventResponse>() {
            @Override
            public NameChangedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NAMECHANGED_EVENT, log);
                NameChangedEventResponse typedResponse = new NameChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NameChangedEventResponse> nameChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NAMECHANGED_EVENT));
        return nameChangedEventFlowable(filter);
    }

    public List<ABIChangedEventResponse> getABIChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ABICHANGED_EVENT, transactionReceipt);
        ArrayList<ABIChangedEventResponse> responses = new ArrayList<ABIChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ABIChangedEventResponse typedResponse = new ABIChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.contentType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ABIChangedEventResponse> aBIChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ABIChangedEventResponse>() {
            @Override
            public ABIChangedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ABICHANGED_EVENT, log);
                ABIChangedEventResponse typedResponse = new ABIChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.contentType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ABIChangedEventResponse> aBIChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ABICHANGED_EVENT));
        return aBIChangedEventFlowable(filter);
    }

    public List<PubkeyChangedEventResponse> getPubkeyChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PUBKEYCHANGED_EVENT, transactionReceipt);
        ArrayList<PubkeyChangedEventResponse> responses = new ArrayList<PubkeyChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PubkeyChangedEventResponse typedResponse = new PubkeyChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.x = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.y = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PubkeyChangedEventResponse> pubkeyChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, PubkeyChangedEventResponse>() {
            @Override
            public PubkeyChangedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PUBKEYCHANGED_EVENT, log);
                PubkeyChangedEventResponse typedResponse = new PubkeyChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.x = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.y = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PubkeyChangedEventResponse> pubkeyChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PUBKEYCHANGED_EVENT));
        return pubkeyChangedEventFlowable(filter);
    }

    public List<TextChangedEventResponse> getTextChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TEXTCHANGED_EVENT, transactionReceipt);
        ArrayList<TextChangedEventResponse> responses = new ArrayList<TextChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TextChangedEventResponse typedResponse = new TextChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.indexedKey = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.key = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TextChangedEventResponse> textChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TextChangedEventResponse>() {
            @Override
            public TextChangedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TEXTCHANGED_EVENT, log);
                TextChangedEventResponse typedResponse = new TextChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.indexedKey = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.key = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TextChangedEventResponse> textChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TEXTCHANGED_EVENT));
        return textChangedEventFlowable(filter);
    }

    @Deprecated
    public static PublicResolver load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PublicResolver(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PublicResolver load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PublicResolver(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PublicResolver load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PublicResolver(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PublicResolver load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PublicResolver(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class AddrChangedEventResponse {
        public Log log;

        public byte[] node;

        public String a;
    }

    public static class ContentChangedEventResponse {
        public Log log;

        public byte[] node;

        public byte[] hash;
    }

    public static class NameChangedEventResponse {
        public Log log;

        public byte[] node;

        public String name;
    }

    public static class ABIChangedEventResponse {
        public Log log;

        public byte[] node;

        public BigInteger contentType;
    }

    public static class PubkeyChangedEventResponse {
        public Log log;

        public byte[] node;

        public byte[] x;

        public byte[] y;
    }

    public static class TextChangedEventResponse {
        public Log log;

        public byte[] node;

        public byte[] indexedKey;

        public String key;
    }
}
