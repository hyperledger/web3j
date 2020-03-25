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
import org.web3j.protocol.core.RemoteTransaction;
import org.web3j.protocol.core.generated.RemoteFunctionCall1;
import org.web3j.protocol.core.generated.RemoteFunctionCall2;
import org.web3j.protocol.core.generated.RemoteTransaction0;
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
    private static final String BINARY = "608060405234801561001057600080fd5b506040516020806111dd833981016040525160008054600160a060020a03909216600160a060020a031990921691909117905561118b806100526000396000f3006080604052600436106100c45763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166301ffc9a781146100c957806310f13a8c146100ff5780632203ab561461019d57806329cd62ea146102375780632dff6941146102555780633b3b57de1461027f57806359d1d43c146102b3578063623195b014610386578063691f3431146103e657806377372213146103fe578063c3d014d61461045c578063c869023314610477578063d5fa2b00146104a8575b600080fd5b3480156100d557600080fd5b506100eb600160e060020a0319600435166104cc565b604080519115158252519081900360200190f35b34801561010b57600080fd5b5060408051602060046024803582810135601f810185900485028601850190965285855261019b95833595369560449491939091019190819084018382808284375050604080516020601f89358b018035918201839004830284018301909452808352979a9998810197919650918201945092508291508401838280828437509497506106399650505050505050565b005b3480156101a957600080fd5b506101b860043560243561084d565b6040518083815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101fb5781810151838201526020016101e3565b50505050905090810190601f1680156102285780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b34801561024357600080fd5b5061019b600435602435604435610959565b34801561026157600080fd5b5061026d600435610a5d565b60408051918252519081900360200190f35b34801561028b57600080fd5b50610297600435610a73565b60408051600160a060020a039092168252519081900360200190f35b3480156102bf57600080fd5b5060408051602060046024803582810135601f8101859004850286018501909652858552610311958335953695604494919390910191908190840183828082843750949750610a8e9650505050505050565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561034b578181015183820152602001610333565b50505050905090810190601f1680156103785780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561039257600080fd5b50604080516020600460443581810135601f810184900484028501840190955284845261019b948235946024803595369594606494920191908190840183828082843750949750610b979650505050505050565b3480156103f257600080fd5b50610311600435610c9c565b34801561040a57600080fd5b5060408051602060046024803582810135601f810185900485028601850190965285855261019b958335953695604494919390910191908190840183828082843750949750610d409650505050505050565b34801561046857600080fd5b5061019b600435602435610e9a565b34801561048357600080fd5b5061048f600435610f7f565b6040805192835260208301919091528051918290030190f35b3480156104b457600080fd5b5061019b600435600160a060020a0360243516610f9c565b6000600160e060020a031982167f3b3b57de00000000000000000000000000000000000000000000000000000000148061052f5750600160e060020a031982167fd8389dc500000000000000000000000000000000000000000000000000000000145b806105635750600160e060020a031982167f691f343100000000000000000000000000000000000000000000000000000000145b806105975750600160e060020a031982167f2203ab5600000000000000000000000000000000000000000000000000000000145b806105cb5750600160e060020a031982167fc869023300000000000000000000000000000000000000000000000000000000145b806105ff5750600160e060020a031982167f59d1d43c00000000000000000000000000000000000000000000000000000000145b806106335750600160e060020a031982167f01ffc9a700000000000000000000000000000000000000000000000000000000145b92915050565b6000805460408051600080516020611140833981519152815260048101879052905186933393600160a060020a0316926302571be39260248083019360209383900390910190829087803b15801561069057600080fd5b505af11580156106a4573d6000803e3d6000fd5b505050506040513d60208110156106ba57600080fd5b5051600160a060020a0316146106cf57600080fd5b6000848152600160209081526040918290209151855185936005019287929182918401908083835b602083106107165780518252601f1990920191602091820191016106f7565b51815160209384036101000a6000190180199092169116179052920194855250604051938490038101909320845161075795919491909101925090506110a4565b50826040518082805190602001908083835b602083106107885780518252601f199092019160209182019101610769565b51815160209384036101000a60001901801990921691161790526040805192909401829003822081835289518383015289519096508a95507fd8c9334b1a9c2f9da342a0a2b32629c1a229b6445dad78947f674b44444a7550948a94508392908301919085019080838360005b8381101561080d5781810151838201526020016107f5565b50505050905090810190601f16801561083a5780820380516001836020036101000a031916815260200191505b509250505060405180910390a350505050565b60008281526001602081905260409091206060905b83831161094c578284161580159061089b5750600083815260068201602052604081205460026000196101006001841615020190911604115b1561094157600083815260068201602090815260409182902080548351601f6002600019610100600186161502019093169290920491820184900484028101840190945280845290918301828280156109355780601f1061090a57610100808354040283529160200191610935565b820191906000526020600020905b81548152906001019060200180831161091857829003601f168201915b50505050509150610951565b600290920291610862565b600092505b509250929050565b6000805460408051600080516020611140833981519152815260048101879052905186933393600160a060020a0316926302571be39260248083019360209383900390910190829087803b1580156109b057600080fd5b505af11580156109c4573d6000803e3d6000fd5b505050506040513d60208110156109da57600080fd5b5051600160a060020a0316146109ef57600080fd5b604080518082018252848152602080820185815260008881526001835284902092516003840155516004909201919091558151858152908101849052815186927f1d6f5e03d3f63eb58751986629a5439baee5079ff04f345becb66e23eb154e46928290030190a250505050565b6000908152600160208190526040909120015490565b600090815260016020526040902054600160a060020a031690565b600082815260016020908152604091829020915183516060936005019285929182918401908083835b60208310610ad65780518252601f199092019160209182019101610ab7565b518151600019602094850361010090810a820192831692199390931691909117909252949092019687526040805197889003820188208054601f6002600183161590980290950116959095049283018290048202880182019052818752929450925050830182828015610b8a5780601f10610b5f57610100808354040283529160200191610b8a565b820191906000526020600020905b815481529060010190602001808311610b6d57829003601f168201915b5050505050905092915050565b6000805460408051600080516020611140833981519152815260048101879052905186933393600160a060020a0316926302571be39260248083019360209383900390910190829087803b158015610bee57600080fd5b505af1158015610c02573d6000803e3d6000fd5b505050506040513d6020811015610c1857600080fd5b5051600160a060020a031614610c2d57600080fd5b6000198301831615610c3e57600080fd5b600084815260016020908152604080832086845260060182529091208351610c68928501906110a4565b50604051839085907faa121bbeef5f32f5961a2a28966e769023910fc9479059ee3495d4c1a696efe390600090a350505050565b6000818152600160208181526040928390206002908101805485516000199582161561010002959095011691909104601f81018390048302840183019094528383526060939091830182828015610d345780601f10610d0957610100808354040283529160200191610d34565b820191906000526020600020905b815481529060010190602001808311610d1757829003601f168201915b50505050509050919050565b6000805460408051600080516020611140833981519152815260048101869052905185933393600160a060020a0316926302571be39260248083019360209383900390910190829087803b158015610d9757600080fd5b505af1158015610dab573d6000803e3d6000fd5b505050506040513d6020811015610dc157600080fd5b5051600160a060020a031614610dd657600080fd5b60008381526001602090815260409091208351610dfb926002909201918501906110a4565b50604080516020808252845181830152845186937fb7d29e911041e8d9b843369e890bcb72c9388692ba48b65ac54e7214c4c348f79387939092839283019185019080838360005b83811015610e5b578181015183820152602001610e43565b50505050905090810190601f168015610e885780820380516001836020036101000a031916815260200191505b509250505060405180910390a2505050565b6000805460408051600080516020611140833981519152815260048101869052905185933393600160a060020a0316926302571be39260248083019360209383900390910190829087803b158015610ef157600080fd5b505af1158015610f05573d6000803e3d6000fd5b505050506040513d6020811015610f1b57600080fd5b5051600160a060020a031614610f3057600080fd5b6000838152600160208181526040928390209091018490558151848152915185927f0424b6fe0d9c3bdbece0e7879dc241bb0c22e900be8b6c168b4ee08bd9bf83bc92908290030190a2505050565b600090815260016020526040902060038101546004909101549091565b6000805460408051600080516020611140833981519152815260048101869052905185933393600160a060020a0316926302571be39260248083019360209383900390910190829087803b158015610ff357600080fd5b505af1158015611007573d6000803e3d6000fd5b505050506040513d602081101561101d57600080fd5b5051600160a060020a03161461103257600080fd5b600083815260016020908152604091829020805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0386169081179091558251908152915185927f52d7d861f09ab3d26239d492e8968629f95e9e318cf0b73bfddc441522a15fd292908290030190a2505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106110e557805160ff1916838001178555611112565b82800160010185558215611112579182015b828111156111125782518255916020019190600101906110f7565b5061111e929150611122565b5090565b61113c91905b8082111561111e5760008155600101611128565b90560002571be300000000000000000000000000000000000000000000000000000000a165627a7a72305820c27de7e59fab6007ff39ba74a6a8d7e8a5eac02905b0a26fee254f94ff3ae4b60029";

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
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {
            }, new TypeReference<Address>() {
            }));

    public static final Event CONTENTCHANGED_EVENT = new Event("ContentChanged",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {
            }, new TypeReference<Bytes32>() {
            }));

    public static final Event NAMECHANGED_EVENT = new Event("NameChanged",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {
            }, new TypeReference<Utf8String>() {
            }));

    public static final Event ABICHANGED_EVENT = new Event("ABIChanged",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {
            }, new TypeReference<Uint256>(true) {
            }));

    public static final Event PUBKEYCHANGED_EVENT = new Event("PubkeyChanged",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {
            }, new TypeReference<Bytes32>() {
            }, new TypeReference<Bytes32>() {
            }));

    public static final Event TEXTCHANGED_EVENT = new Event("TextChanged",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {
            }, new TypeReference<Utf8String>(true) {
            }, new TypeReference<Utf8String>() {
            }));

    protected PublicResolver(
            String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected PublicResolver(
            String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] interfaceID) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return new RemoteFunctionCall1<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteTransaction<Void> setText(byte[] node, String key, String value) {
        final Function function = new Function(
                FUNC_SETTEXT,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.Utf8String(key),
                        new org.web3j.abi.datatypes.Utf8String(value)),
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO,
                false, gasProvider);
    }

    public RemoteCall<Tuple2<BigInteger, byte[]>> ABI(byte[] node, BigInteger contentTypes) {
        final Function function = new Function(FUNC_ABI,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.generated.Uint256(contentTypes)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }, new TypeReference<DynamicBytes>() {
                }));
        return new RemoteFunctionCall2<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteTransaction<Void> setPubkey(byte[] node, byte[] x, byte[] y) {
        final Function function = new Function(
                FUNC_SETPUBKEY,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.generated.Bytes32(x),
                        new org.web3j.abi.datatypes.generated.Bytes32(y)),
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO,
                false, gasProvider);
    }

    public RemoteCall<byte[]> content(byte[] node) {
        final Function function = new Function(FUNC_CONTENT,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }));
        return new RemoteFunctionCall1<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteCall<String> addr(byte[] node) {
        final Function function = new Function(FUNC_ADDR,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return new RemoteFunctionCall1<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteCall<String> text(byte[] node, String key) {
        final Function function = new Function(FUNC_TEXT,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.Utf8String(key)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return new RemoteFunctionCall1<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteTransaction<Void> setABI(byte[] node, BigInteger contentType, byte[] data) {
        final Function function = new Function(
                FUNC_SETABI,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.generated.Uint256(contentType),
                        new org.web3j.abi.datatypes.DynamicBytes(data)),
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO,
                false, gasProvider);
    }

    public RemoteCall<String> name(byte[] node) {
        final Function function = new Function(FUNC_NAME,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return new RemoteFunctionCall1<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteTransaction<Void> setName(byte[] node, String name) {
        final Function function = new Function(
                FUNC_SETNAME,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.Utf8String(name)),
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO,
                false, gasProvider);
    }

    public RemoteTransaction<Void> setContent(byte[] node, byte[] hash) {
        final Function function = new Function(
                FUNC_SETCONTENT,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.generated.Bytes32(hash)),
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO,
                false, gasProvider);
    }

    public RemoteCall<Tuple2<byte[], byte[]>> pubkey(byte[] node) {
        final Function function = new Function(FUNC_PUBKEY,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {
                }, new TypeReference<Bytes32>() {
                }));
        return new RemoteFunctionCall2<>(function, contractAddress, transactionManager, defaultBlockParameter);
    }

    public RemoteTransaction<Void> setAddr(byte[] node, String addr) {
        final Function function = new Function(
                FUNC_SETADDR,
                Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.generated.Bytes32(node),
                        new org.web3j.abi.datatypes.Address(addr)),
                Collections.<TypeReference<?>>emptyList());
        return new RemoteTransaction0(web3j, function, contractAddress, transactionManager,
                defaultBlockParameter, FunctionEncoder.encode(function), BigInteger.ZERO,
                false, gasProvider);
    }

    public static RemoteCall<PublicResolver> deploy(
            Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider,
            String ensAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.Address(ensAddr)));
        return deployRemoteCall(PublicResolver.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<PublicResolver> deploy(
            Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider, String ensAddr) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type<?>>asList(new org.web3j.abi.datatypes.Address(ensAddr)));
        return deployRemoteCall(PublicResolver.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public List<AddrChangedEventResponse> getAddrChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDRCHANGED_EVENT, transactionReceipt);
        ArrayList<AddrChangedEventResponse> responses = new ArrayList<AddrChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDRCHANGED_EVENT, log);
                AddrChangedEventResponse typedResponse = new AddrChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.a = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddrChangedEventResponse> addrChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDRCHANGED_EVENT));
        return addrChangedEventFlowable(filter);
    }

    public List<ContentChangedEventResponse> getContentChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CONTENTCHANGED_EVENT, transactionReceipt);
        ArrayList<ContentChangedEventResponse> responses = new ArrayList<ContentChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONTENTCHANGED_EVENT, log);
                ContentChangedEventResponse typedResponse = new ContentChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.hash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContentChangedEventResponse> contentChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTENTCHANGED_EVENT));
        return contentChangedEventFlowable(filter);
    }

    public List<NameChangedEventResponse> getNameChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NAMECHANGED_EVENT, transactionReceipt);
        ArrayList<NameChangedEventResponse> responses = new ArrayList<NameChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NAMECHANGED_EVENT, log);
                NameChangedEventResponse typedResponse = new NameChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NameChangedEventResponse> nameChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NAMECHANGED_EVENT));
        return nameChangedEventFlowable(filter);
    }

    public List<ABIChangedEventResponse> getABIChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ABICHANGED_EVENT, transactionReceipt);
        ArrayList<ABIChangedEventResponse> responses = new ArrayList<ABIChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ABICHANGED_EVENT, log);
                ABIChangedEventResponse typedResponse = new ABIChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.contentType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ABIChangedEventResponse> aBIChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ABICHANGED_EVENT));
        return aBIChangedEventFlowable(filter);
    }

    public List<PubkeyChangedEventResponse> getPubkeyChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(PUBKEYCHANGED_EVENT, transactionReceipt);
        ArrayList<PubkeyChangedEventResponse> responses = new ArrayList<PubkeyChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PUBKEYCHANGED_EVENT, log);
                PubkeyChangedEventResponse typedResponse = new PubkeyChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.x = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.y = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PubkeyChangedEventResponse> pubkeyChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PUBKEYCHANGED_EVENT));
        return pubkeyChangedEventFlowable(filter);
    }

    public List<TextChangedEventResponse> getTextChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TEXTCHANGED_EVENT, transactionReceipt);
        ArrayList<TextChangedEventResponse> responses = new ArrayList<TextChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TEXTCHANGED_EVENT, log);
                TextChangedEventResponse typedResponse = new TextChangedEventResponse();
                typedResponse.log = log;
                typedResponse.node = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.indexedKey = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.key = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TextChangedEventResponse> textChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TEXTCHANGED_EVENT));
        return textChangedEventFlowable(filter);
    }

    public static PublicResolver load(
            String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new PublicResolver(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PublicResolver load(
            String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
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
