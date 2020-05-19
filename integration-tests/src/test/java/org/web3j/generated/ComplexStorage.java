package org.web3j.generated;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
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
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
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
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("rawtypes")
public class ComplexStorage extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162001335380380620013358339810160408190526200003491620001d4565b81518051839160009162000050918391602090910190620000c1565b5060208281015180516200006b9260018501920190620000c1565b5050815160025550602081015160035560405133907fcbad86eed973ae35744d2e31c8bfcc2212dd14e3eb15b34f1cb862c61dcf202990620000b19085908590620002d4565b60405180910390a2505062000387565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200010457805160ff191683800117855562000134565b8280016001018555821562000134579182015b828111156200013457825182559160200191906001019062000117565b506200014292915062000146565b5090565b6200016391905b808211156200014257600081556001016200014d565b90565b600082601f83011262000177578081fd5b81516001600160401b038111156200018d578182fd5b620001a2601f8201601f19166020016200032d565b9150808252836020828501011115620001ba57600080fd5b620001cd81602084016020860162000354565b5092915050565b6000808284036060811215620001e8578283fd5b83516001600160401b0380821115620001ff578485fd5b8186016040818903121562000212578586fd5b6200021e60406200032d565b92508051828111156200022f578687fd5b6200023d8982840162000166565b84525060208101518281111562000252578687fd5b620002608982840162000166565b60208501525091945050506040601f19820112156200027d578182fd5b506200028a60406200032d565b6020840151815260408401516020820152809150509250929050565b60008151808452620002c081602086016020860162000354565b601f01601f19169290920160200192915050565b600060608252835160406060840152620002f260a0840182620002a6565b6020860151848203605f190160808601529150620003118183620002a6565b9250505082516020830152602083015160408301529392505050565b6040518181016001600160401b03811182821017156200034c57600080fd5b604052919050565b60005b838110156200037157818101518382015260200162000357565b8381111562000381576000848401525b50505050565b610f9e80620003976000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c80636bb632a91161008c578063ad204a1211610066578063ad204a1214610192578063be9c5e34146101a5578063bea80d0c146101b8578063cfd91f2b146101ce576100cf565b80636bb632a9146101595780638c9fb6f91461016c5780639096c2131461017f576100cf565b8063243dc8da146100d45780632cf07395146100f25780632f460fef146101075780633d761de61461011c57806367a23d131461012f5780636896c6ca14610144575b600080fd5b6100dc6101e4565b6040516100e99190610e81565b60405180910390f35b610105610100366004610c91565b61032f565b005b61010f610368565b6040516100e99190610efb565b61010561012a366004610b83565b6104e1565b6101376104ef565b6040516100e99190610e6d565b61014c610512565b6040516100e99190610ed8565b610105610167366004610d05565b610544565b61010561017a366004610da9565b61059e565b61010561018d366004610ba5565b6105dd565b6101056101a0366004610ccc565b610608565b6101056101b3366004610c1a565b61061f565b6101c0610640565b6040516100e9929190610eb6565b6101d6610794565b6040516100e9929190610e94565b6101ec610906565b604080516000805460606020601f60026000196101006001871615020190941693909304928301819004028401810185529383018181529293919284929091849184018282801561027e5780601f106102535761010080835404028352916020019161027e565b820191906000526020600020905b81548152906001019060200180831161026157829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103205780601f106102f557610100808354040283529160200191610320565b820191906000526020600020905b81548152906001019060200180831161030357829003601f168201915b50505050508152505090505b90565b805180518291600091610349918391602090910190610920565b5060208281015180516103629260018501920190610920565b50505050565b61037061099e565b60408051600d805460e06020601f600260001961010060018716150201909416939093049283018190040284018101855260c084018281529394929385938401928592849260608701928592849260808a0192859284929184918d018282801561041b5780601f106103f05761010080835404028352916020019161041b565b820191906000526020600020905b8154815290600101906020018083116103fe57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104bd5780601f10610492576101008083540402835291602001916104bd565b820191906000526020600020905b8154815290600101906020018083116104a057829003601f168201915b50505050508152505081525050815250508152602001600282015481525050905090565b805160025560200151600355565b6104f76109be565b50604080518082019091526002548152600354602082015290565b61051a6109d8565b50604080516080810182526004549181019182526005546060820152908152600654602082015290565b805180518051805180518594600d949093859391928492839161056e918391602090910190610920565b5060208281015180516105879260018501920190610920565b505050505050506020820151816002015590505050565b8051805180518392600b92909183916105bc91839160200190610920565b5060208281015180516105d59260018501920190610920565b505050505050565b8051805182916007916105f7918391602090910190610920565b506020820151816001015590505050565b805180516004556020908101516005550151600655565b80516009908155602080830151805184939261036292600a92910190610920565b610648610906565b604080516000805460206002600180841615610100026000190190931604601f8101829004909102840160609081018652948401818152929485949293859284928491908401828280156106dd5780601f106106b2576101008083540402835291602001916106dd565b820191906000526020600020905b8154815290600101906020018083116106c057829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561077f5780601f106107545761010080835404028352916020019161077f565b820191906000526020600020905b81548152906001019060200180831161076257829003601f168201915b50505091909252509195509193505050509091565b61079c610906565b6107a46109be565b60408051600080546020600260018316156101000260001901909216829004601f810182900490910284016060908101865294840181815292949193928592849284918401828280156108385780601f1061080d57610100808354040283529160200191610838565b820191906000526020600020905b81548152906001019060200180831161081b57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108da5780601f106108af576101008083540402835291602001916108da565b820191906000526020600020905b8154815290600101906020018083116108bd57829003601f168201915b505050919092525050604080518082019091528354815260019093015460208401525093509150509091565b604051806040016040528060608152602001606081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061096157805160ff191683800117855561098e565b8280016001018555821561098e579182015b8281111561098e578251825591602001919060010190610973565b5061099a9291506109eb565b5090565b60405180604001604052806109b1610a05565b8152602001600081525090565b604051806040016040528060008152602001600081525090565b60405180604001604052806109b16109be565b61032c91905b8082111561099a57600081556001016109f1565b6040518060200160405280610a18610a1d565b905290565b6040518060200160405280610a18610906565b600082601f830112610a40578081fd5b813567ffffffffffffffff811115610a56578182fd5b610a69601f8201601f1916602001610f41565b9150808252836020828501011115610a8057600080fd5b8060208401602084013760009082016020015292915050565b600060408284031215610aaa578081fd5b610ab46040610f41565b9050813581526020820135602082015292915050565b600060408284031215610adb578081fd5b610ae56040610f41565b9050813567ffffffffffffffff80821115610aff57600080fd5b610b0b85838601610a30565b83526020840135915080821115610b2157600080fd5b50610b2e84828501610a30565b60208301525092915050565b600060208284031215610b4b578081fd5b610b556020610f41565b9050813567ffffffffffffffff811115610b6e57600080fd5b610b7a84828501610aca565b82525092915050565b600060408284031215610b94578081fd5b610b9e8383610a99565b9392505050565b600060208284031215610bb6578081fd5b813567ffffffffffffffff80821115610bcd578283fd5b81840160408187031215610bdf578384fd5b610be96040610f41565b9250803582811115610bf9578485fd5b610c0587828401610a30565b84525060209081013590830152509392505050565b600060208284031215610c2b578081fd5b813567ffffffffffffffff80821115610c42578283fd5b81840160408187031215610c54578384fd5b610c5e6040610f41565b925080358352602081013582811115610c75578485fd5b610c8187828401610a30565b6020850152509195945050505050565b600060208284031215610ca2578081fd5b813567ffffffffffffffff811115610cb8578182fd5b610cc484828501610aca565b949350505050565b600060608284031215610cdd578081fd5b610ce76040610f41565b610cf18484610a99565b815260409290920135602083015250919050565b600060208284031215610d16578081fd5b813567ffffffffffffffff80821115610d2d578283fd5b81840160408187031215610d3f578384fd5b610d496040610f41565b9250803582811115610d59578485fd5b80820160208189031215610d6b578586fd5b610d756020610f41565b9150803584811115610d85578687fd5b610d9189828401610b3a565b83525050835260209081013590830152509392505050565b600060208284031215610dba578081fd5b813567ffffffffffffffff811115610dd0578182fd5b610cc484828501610b3a565b60008151808452815b81811015610e0157602081850181015186830182015201610de5565b81811115610e125782602083870101525b50601f01601f19169290920160200192915050565b80518252602090810151910152565b6000815160408452610e4b6040850182610ddc565b602084015191508481036020860152610e648183610ddc565b95945050505050565b60408101610e7b8284610e27565b92915050565b600060208252610b9e6020830184610e36565b600060608252610ea76060830185610e36565b9050610b9e6020830184610e27565b600060408252610ec96040830185610e36565b90508260208301529392505050565b6000606082019050610eeb828451610e27565b6020830151604083015292915050565b600060208252825160406020840152805160206060850152805191505060206080840152610f2c60a0840182610e36565b60208501516040850152809250505092915050565b60405181810167ffffffffffffffff81118282101715610f6057600080fd5b60405291905056fea264697066735822122049ef44ebd87626a9da527bdaf5ca9c648388a7dc041c7d969f64e15be614a69864736f6c63430006050033";

    public static final String FUNC_GETBAR = "getBar";

    public static final String FUNC_GETFOO = "getFoo";

    public static final String FUNC_GETFOOBAR = "getFooBar";

    public static final String FUNC_GETFOOUINT = "getFooUint";

    public static final String FUNC_GETFUZZ = "getFuzz";

    public static final String FUNC_GETNAZ = "getNaz";

    public static final String FUNC_SETBAR = "setBar";

    public static final String FUNC_SETBAZ = "setBaz";

    public static final String FUNC_SETBOZ = "setBoz";

    public static final String FUNC_SETFOO = "setFoo";

    public static final String FUNC_SETFUZZ = "setFuzz";

    public static final String FUNC_SETNAZ = "setNaz";

    public static final String FUNC_SETNUU = "setNuu";

    public static final Event ACCESS_EVENT = new Event("Access", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Foo>() {}, new TypeReference<Bar>() {}));
    ;

    @Deprecated
    protected ComplexStorage(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ComplexStorage(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ComplexStorage(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ComplexStorage(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AccessEventResponse> getAccessEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ACCESS_EVENT, transactionReceipt);
        ArrayList<AccessEventResponse> responses = new ArrayList<AccessEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AccessEventResponse typedResponse = new AccessEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._address = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._foo = (Foo) eventValues.getNonIndexedValues().get(0);
            typedResponse._bar = (Bar) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AccessEventResponse> accessEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AccessEventResponse>() {
            @Override
            public AccessEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ACCESS_EVENT, log);
                AccessEventResponse typedResponse = new AccessEventResponse();
                typedResponse.log = log;
                typedResponse._address = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._foo = (Foo) eventValues.getNonIndexedValues().get(0);
                typedResponse._bar = (Bar) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Flowable<AccessEventResponse> accessEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESS_EVENT));
        return accessEventFlowable(filter);
    }

    public RemoteFunctionCall<Bar> getBar() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBAR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bar>() {}));
        return executeRemoteCallSingleValueReturn(function, Bar.class);
    }

    public RemoteFunctionCall<Foo> getFoo() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETFOO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Foo>() {}));
        return executeRemoteCallSingleValueReturn(function, Foo.class);
    }

    public RemoteFunctionCall<Tuple2<Foo, Bar>> getFooBar() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETFOOBAR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Foo>() {}, new TypeReference<Bar>() {}));
        return new RemoteFunctionCall<Tuple2<Foo, Bar>>(function,
                new Callable<Tuple2<Foo, Bar>>() {
                    @Override
                    public Tuple2<Foo, Bar> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<Foo, Bar>(
                                (Foo) results.get(0), 
                                (Bar) results.get(1));
                    }
                });
    }

    public RemoteFunctionCall<Tuple2<Foo, BigInteger>> getFooUint() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETFOOUINT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Foo>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<Foo, BigInteger>>(function,
                new Callable<Tuple2<Foo, BigInteger>>() {
                    @Override
                    public Tuple2<Foo, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<Foo, BigInteger>(
                                (Foo) results.get(0), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Fuzz> getFuzz() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETFUZZ, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Fuzz>() {}));
        return executeRemoteCallSingleValueReturn(function, Fuzz.class);
    }

    public RemoteFunctionCall<Naz> getNaz() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETNAZ, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Naz>() {}));
        return executeRemoteCallSingleValueReturn(function, Naz.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setBar(Bar _toSet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETBAR, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setBaz(Baz _toSet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETBAZ, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setBoz(Boz _toSet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETBOZ, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setFoo(Foo _toSet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETFOO, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setFuzz(Fuzz _toSet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETFUZZ, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setNaz(Naz _naz) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETNAZ, 
                Arrays.<Type>asList(_naz), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setNuu(Nuu _toSet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETNUU, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ComplexStorage load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ComplexStorage(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ComplexStorage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ComplexStorage(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ComplexStorage load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ComplexStorage(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ComplexStorage load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ComplexStorage(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ComplexStorage> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Foo _foo, Bar _bar) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_foo, 
                _bar));
        return deployRemoteCall(ComplexStorage.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ComplexStorage> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Foo _foo, Bar _bar) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_foo, 
                _bar));
        return deployRemoteCall(ComplexStorage.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ComplexStorage> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Foo _foo, Bar _bar) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_foo, 
                _bar));
        return deployRemoteCall(ComplexStorage.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ComplexStorage> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Foo _foo, Bar _bar) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_foo, 
                _bar));
        return deployRemoteCall(ComplexStorage.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class Foo extends StaticStruct {
        public String id;

        public String name;

        public Foo(String id, String name) {
            super(new org.web3j.abi.datatypes.Utf8String(id), new org.web3j.abi.datatypes.Utf8String(name));
            this.id = id;
            this.name = name;
        }
    }

    public static class Bar extends StaticStruct {
        public BigInteger id;

        public BigInteger data;

        public Bar(BigInteger id, BigInteger data) {
            super(new Uint256(id), new Uint256(data));
            this.id = id;
            this.data = data;
        }
    }

    public static class Baz extends StaticStruct {
        public String id;

        public BigInteger data;

        public Baz(String id, BigInteger data) {
            super(new org.web3j.abi.datatypes.Utf8String(id), new Uint256(data));
            this.id = id;
            this.data = data;
        }
    }

    public static class Boz extends StaticStruct {
        public BigInteger data;

        public String id;

        public Boz(BigInteger data, String id) {
            super(new Uint256(data), new org.web3j.abi.datatypes.Utf8String(id));
            this.data = data;
            this.id = id;
        }
    }

    public static class Fuzz extends StaticStruct {
        public Bar bar;

        public BigInteger data;

        public Fuzz(Bar bar, BigInteger data) {
            super(bar, new Uint256(data));
            this.bar = bar;
            this.data = data;
        }
    }

    public static class Nuu extends StaticStruct {
        public Foo foo;

        public Nuu(Foo foo) {
            super(foo);
            this.foo = foo;
        }
    }

    public static class Nar extends StaticStruct {
        public Nuu nuu;

        public Nar(Nuu nuu) {
            super(nuu);
            this.nuu = nuu;
        }
    }

    public static class Naz extends StaticStruct {
        public Nar nar;

        public BigInteger data;

        public Naz(Nar nar, BigInteger data) {
            super(nar, new Uint256(data));
            this.nar = nar;
            this.data = data;
        }
    }

    public static class AccessEventResponse extends BaseEventResponse {
        public String _address;

        public Foo _foo;

        public Bar _bar;
    }
}
