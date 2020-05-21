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
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162001429380380620014298339810160408190526200003491620001d4565b81518051839160009162000050918391602090910190620000c1565b5060208281015180516200006b9260018501920190620000c1565b5050815160025550602081015160035560405133907fcbad86eed973ae35744d2e31c8bfcc2212dd14e3eb15b34f1cb862c61dcf202990620000b19085908590620002d4565b60405180910390a2505062000387565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200010457805160ff191683800117855562000134565b8280016001018555821562000134579182015b828111156200013457825182559160200191906001019062000117565b506200014292915062000146565b5090565b6200016391905b808211156200014257600081556001016200014d565b90565b600082601f83011262000177578081fd5b81516001600160401b038111156200018d578182fd5b620001a2601f8201601f19166020016200032d565b9150808252836020828501011115620001ba57600080fd5b620001cd81602084016020860162000354565b5092915050565b6000808284036060811215620001e8578283fd5b83516001600160401b0380821115620001ff578485fd5b8186016040818903121562000212578586fd5b6200021e60406200032d565b92508051828111156200022f578687fd5b6200023d8982840162000166565b84525060208101518281111562000252578687fd5b620002608982840162000166565b60208501525091945050506040601f19820112156200027d578182fd5b506200028a60406200032d565b6020840151815260408401516020820152809150509250929050565b60008151808452620002c081602086016020860162000354565b601f01601f19169290920160200192915050565b600060608252835160406060840152620002f260a0840182620002a6565b6020860151848203605f190160808601529150620003118183620002a6565b9250505082516020830152602083015160408301529392505050565b6040518181016001600160401b03811182821017156200034c57600080fd5b604052919050565b60005b838110156200037157818101518382015260200162000357565b8381111562000381576000848401525b50505050565b61109280620003976000396000f3fe608060405234801561001057600080fd5b50600436106100ea5760003560e01c80638c9fb6f91161008c578063be9c5e3411610066578063be9c5e34146101c0578063bea80d0c146101d3578063c036933d146101e9578063cfd91f2b146101fc576100ea565b80638c9fb6f9146101875780639096c2131461019a578063ad204a12146101ad576100ea565b80633d761de6116100c85780633d761de61461013757806367a23d131461014a5780636896c6ca1461015f5780636bb632a914610174576100ea565b8063243dc8da146100ef5780632cf073951461010d5780632f460fef14610122575b600080fd5b6100f7610212565b6040516101049190610f75565b60405180910390f35b61012061011b366004610d11565b61035d565b005b61012a610396565b6040516101049190610fef565b610120610145366004610c03565b61050f565b61015261051d565b6040516101049190610f61565b610167610540565b6040516101049190610fcc565b610120610182366004610d85565b610572565b610120610195366004610e29565b6105cc565b6101206101a8366004610c25565b61060b565b6101206101bb366004610d4c565b610636565b6101206101ce366004610c9a565b61064d565b6101db61066e565b604051610104929190610faa565b6101206101f7366004610e5c565b6107c2565b610204610814565b604051610104929190610f88565b61021a610986565b604080516000805460606020601f6002600019610100600187161502019094169390930492830181900402840181018552938301818152929391928492909184918401828280156102ac5780601f10610281576101008083540402835291602001916102ac565b820191906000526020600020905b81548152906001019060200180831161028f57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561034e5780601f106103235761010080835404028352916020019161034e565b820191906000526020600020905b81548152906001019060200180831161033157829003601f168201915b50505050508152505090505b90565b8051805182916000916103779183916020909101906109a0565b50602082810151805161039092600185019201906109a0565b50505050565b61039e610a1e565b60408051600d805460e06020601f600260001961010060018716150201909416939093049283018190040284018101855260c084018281529394929385938401928592849260608701928592849260808a0192859284929184918d01828280156104495780601f1061041e57610100808354040283529160200191610449565b820191906000526020600020905b81548152906001019060200180831161042c57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104eb5780601f106104c0576101008083540402835291602001916104eb565b820191906000526020600020905b8154815290600101906020018083116104ce57829003601f168201915b50505050508152505081525050815250508152602001600282015481525050905090565b805160025560200151600355565b610525610a3e565b50604080518082019091526002548152600354602082015290565b610548610a58565b50604080516080810182526004549181019182526005546060820152908152600654602082015290565b805180518051805180518594600d949093859391928492839161059c9183916020909101906109a0565b5060208281015180516105b592600185019201906109a0565b505050505050506020820151816002015590505050565b8051805180518392600b92909183916105ea918391602001906109a0565b50602082810151805161060392600185019201906109a0565b505050505050565b8051805182916007916106259183916020909101906109a0565b506020820151816001015590505050565b805180516004556020908101516005550151600655565b80516009908155602080830151805184939261039092600a929101906109a0565b610676610986565b604080516000805460206002600180841615610100026000190190931604601f81018290049091028401606090810186529484018181529294859492938592849284919084018282801561070b5780601f106106e05761010080835404028352916020019161070b565b820191906000526020600020905b8154815290600101906020018083116106ee57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107ad5780601f10610782576101008083540402835291602001916107ad565b820191906000526020600020905b81548152906001019060200180831161079057829003601f168201915b50505091909252509195509193505050509091565b8051805180518392601092909183916107e0918391602001906109a0565b5060208281015180516107f992600185019201906109a0565b505050602082810151805161039092600285019201906109a0565b61081c610986565b610824610a3e565b60408051600080546020600260018316156101000260001901909216829004601f810182900490910284016060908101865294840181815292949193928592849284918401828280156108b85780601f1061088d576101008083540402835291602001916108b8565b820191906000526020600020905b81548152906001019060200180831161089b57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561095a5780601f1061092f5761010080835404028352916020019161095a565b820191906000526020600020905b81548152906001019060200180831161093d57829003601f168201915b505050919092525050604080518082019091528354815260019093015460208401525093509150509091565b604051806040016040528060608152602001606081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106109e157805160ff1916838001178555610a0e565b82800160010185558215610a0e579182015b82811115610a0e5782518255916020019190600101906109f3565b50610a1a929150610a6b565b5090565b6040518060400160405280610a31610a85565b8152602001600081525090565b604051806040016040528060008152602001600081525090565b6040518060400160405280610a31610a3e565b61035a91905b80821115610a1a5760008155600101610a71565b6040518060200160405280610a98610a9d565b905290565b6040518060200160405280610a98610986565b600082601f830112610ac0578081fd5b813567ffffffffffffffff811115610ad6578182fd5b610ae9601f8201601f1916602001611035565b9150808252836020828501011115610b0057600080fd5b8060208401602084013760009082016020015292915050565b600060408284031215610b2a578081fd5b610b346040611035565b9050813581526020820135602082015292915050565b600060408284031215610b5b578081fd5b610b656040611035565b9050813567ffffffffffffffff80821115610b7f57600080fd5b610b8b85838601610ab0565b83526020840135915080821115610ba157600080fd5b50610bae84828501610ab0565b60208301525092915050565b600060208284031215610bcb578081fd5b610bd56020611035565b9050813567ffffffffffffffff811115610bee57600080fd5b610bfa84828501610b4a565b82525092915050565b600060408284031215610c14578081fd5b610c1e8383610b19565b9392505050565b600060208284031215610c36578081fd5b813567ffffffffffffffff80821115610c4d578283fd5b81840160408187031215610c5f578384fd5b610c696040611035565b9250803582811115610c79578485fd5b610c8587828401610ab0565b84525060209081013590830152509392505050565b600060208284031215610cab578081fd5b813567ffffffffffffffff80821115610cc2578283fd5b81840160408187031215610cd4578384fd5b610cde6040611035565b925080358352602081013582811115610cf5578485fd5b610d0187828401610ab0565b6020850152509195945050505050565b600060208284031215610d22578081fd5b813567ffffffffffffffff811115610d38578182fd5b610d4484828501610b4a565b949350505050565b600060608284031215610d5d578081fd5b610d676040611035565b610d718484610b19565b815260409290920135602083015250919050565b600060208284031215610d96578081fd5b813567ffffffffffffffff80821115610dad578283fd5b81840160408187031215610dbf578384fd5b610dc96040611035565b9250803582811115610dd9578485fd5b80820160208189031215610deb578586fd5b610df56020611035565b9150803584811115610e05578687fd5b610e1189828401610bba565b83525050835260209081013590830152509392505050565b600060208284031215610e3a578081fd5b813567ffffffffffffffff811115610e50578182fd5b610d4484828501610bba565b600060208284031215610e6d578081fd5b813567ffffffffffffffff80821115610e84578283fd5b81840160408187031215610e96578384fd5b610ea06040611035565b9250803582811115610eb0578485fd5b610ebc87828401610b4a565b845250602081013582811115610cf5578485fd5b60008151808452815b81811015610ef557602081850181015186830182015201610ed9565b81811115610f065782602083870101525b50601f01601f19169290920160200192915050565b80518252602090810151910152565b6000815160408452610f3f6040850182610ed0565b602084015191508481036020860152610f588183610ed0565b95945050505050565b60408101610f6f8284610f1b565b92915050565b600060208252610c1e6020830184610f2a565b600060608252610f9b6060830185610f2a565b9050610c1e6020830184610f1b565b600060408252610fbd6040830185610f2a565b90508260208301529392505050565b6000606082019050610fdf828451610f1b565b6020830151604083015292915050565b60006020825282516040602084015280516020606085015280519150506020608084015261102060a0840182610f2a565b60208501516040850152809250505092915050565b60405181810167ffffffffffffffff8111828210171561105457600080fd5b60405291905056fea264697066735822122020bcdecaa7fab15b954aa6f0b7e283076abc2eac012fa317db616c9d61738fb564736f6c63430006050033";

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

    public static final String FUNC_SETWIZ = "setWiz";

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
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ACCESS_EVENT, transactionReceipt);
        ArrayList<AccessEventResponse> responses = new ArrayList<AccessEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ACCESS_EVENT, log);
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

    public RemoteFunctionCall<TransactionReceipt> setWiz(Wiz _toSet) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETWIZ,
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

    public static class Foo extends DynamicStruct {
        public String id;

        public String name;

        public Foo(String id, String name) {
            super(new org.web3j.abi.datatypes.Utf8String(id), new org.web3j.abi.datatypes.Utf8String(name));
            this.id = id;
            this.name = name;
        }

        public Foo(Utf8String id, Utf8String name) {
            super(id, name);
            this.id = id.getValue();
            this.name = name.getValue();
        }
    }

    public static class Bar extends StaticStruct {
        public BigInteger id;

        public BigInteger data;

        public Bar(BigInteger id, BigInteger data) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id), new org.web3j.abi.datatypes.generated.Uint256(data));
            this.id = id;
            this.data = data;
        }

        public Bar(Uint256 id, Uint256 data) {
            super(id, data);
            this.id = id.getValue();
            this.data = data.getValue();
        }
    }

    public static class Baz extends DynamicStruct {
        public String id;

        public BigInteger data;

        public Baz(String id, BigInteger data) {
            super(new org.web3j.abi.datatypes.Utf8String(id), new org.web3j.abi.datatypes.generated.Uint256(data));
            this.id = id;
            this.data = data;
        }

        public Baz(Utf8String id, Uint256 data) {
            super(id, data);
            this.id = id.getValue();
            this.data = data.getValue();
        }
    }

    public static class Boz extends DynamicStruct {
        public BigInteger data;

        public String id;

        public Boz(BigInteger data, String id) {
            super(new org.web3j.abi.datatypes.generated.Uint256(data), new org.web3j.abi.datatypes.Utf8String(id));
            this.data = data;
            this.id = id;
        }

        public Boz(Uint256 data, Utf8String id) {
            super(data, id);
            this.data = data.getValue();
            this.id = id.getValue();
        }
    }

    public static class Fuzz extends StaticStruct {
        public Bar bar;

        public BigInteger data;

        public Fuzz(Bar bar, BigInteger data) {
            super(bar, new org.web3j.abi.datatypes.generated.Uint256(data));
            this.bar = bar;
            this.data = data;
        }

        public Fuzz(Bar bar, Uint256 data) {
            super(bar, data);
            this.bar = bar;
            this.data = data.getValue();
        }
    }

    public static class Nuu extends DynamicStruct {
        public Foo foo;

        public Nuu(Foo foo) {
            super(foo);
            this.foo = foo;
        }
    }

    public static class Wiz extends DynamicStruct {
        public Foo foo;

        public String data;

        public Wiz(Foo foo, String data) {
            super(foo, new org.web3j.abi.datatypes.Utf8String(data));
            this.foo = foo;
            this.data = data;
        }

        public Wiz(Foo foo, Utf8String data) {
            super(foo, data);
            this.foo = foo;
            this.data = data.getValue();
        }
    }

    public static class Nar extends DynamicStruct {
        public Nuu nuu;

        public Nar(Nuu nuu) {
            super(nuu);
            this.nuu = nuu;
        }
    }

    public static class Naz extends DynamicStruct {
        public Nar nar;

        public BigInteger data;

        public Naz(Nar nar, BigInteger data) {
            super(nar, new org.web3j.abi.datatypes.generated.Uint256(data));
            this.nar = nar;
            this.data = data;
        }

        public Naz(Nar nar, Uint256 data) {
            super(nar, data);
            this.nar = nar;
            this.data = data.getValue();
        }
    }

    public static class AccessEventResponse extends BaseEventResponse {
        public String _address;

        public Foo _foo;

        public Bar _bar;
    }
}
