package org.web3j.generated;

import java.lang.String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;
import org.web3j.abi.Contract;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.</p>
 */
public final class HumanStandardToken extends Contract {
    private static final String BINARY = "60a060405260046060527f48302e31000000000000000000000000000000000000000000000000000000006080526006805460008290527f48302e310000000000000000000000000000000000000000000000000000000882556100b5907ff652222313e28459528d920b65115c16c04f3efc82aaedc97be59f3f377c0d3f602060026001841615610100026000190190931692909204601f01919091048101905b8082111561018b57600081556001016100a1565b50506040516109ab3803806109ab83398101604052808051906020019091908051820191906020018051906020019091908051820191906020015050600160a060020a033316600090815260016020818152604083208790558683558551600380549481905293601f60026000196101009684161596909602959095019091169390930483018290047fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85b90810193919290919088019083901061018f57805160ff19168380011785555b506101bf9291506100a1565b5090565b8280016001018555821561017f579182015b8281111561017f5782518260005055916020019190600101906101a1565b50506004805460ff191683179055805160058054600082905290917f036b6384b5eca791c62761152d0c79bb0604c104a5fb6f4eb0703f3154bb3db0602060026101006001861615026000190190941693909304601f90810184900482019386019083901061024157805160ff19168380011785555b506102719291506100a1565b82800160010185558215610235579182015b82811115610235578251826000505591602001919060010190610253565b505050505050610726806102856000396000f36060604052361561008d5760e060020a600035046306fdde03811461009a578063095ea7b3146100ff57806318160ddd1461017857806323b872dd14610186578063313ce5671461028157806354fd4d501461029257806370a08231146102f757806395d89b411461032a578063a9059cbb1461038f578063cae9ca511461043a578063dd62ed3e14610605575b346100025761063e610002565b346100025761064060038054604080516020601f600260001961010060018816150201909516949094049384018190048102820181019092528281529291908301828280156107035780601f106106d857610100808354040283529160200191610703565b34610002576106ae600435602435600160a060020a03338116600081815260026020908152604080832094871680845294825280832086905580518681529051929493927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925929181900390910190a35060015b92915050565b346100025761031860005481565b34610002576106ae600435602435604435600160a060020a0383166000908152600160205260408120548290108015906101e75750600160a060020a0380851660009081526002602090815260408083203390941683529290522054829010155b80156101f35750600082115b1561070b57600160a060020a03808416600081815260016020908152604080832080548801905588851680845281842080548990039055600283528184203390961684529482529182902080548790039055815186815291519293927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a350600161070f565b34610002576106c260045460ff1681565b346100025761064060068054604080516020601f600260001961010060018816150201909516949094049384018190048102820181019092528281529291908301828280156107035780601f106106d857610100808354040283529160200191610703565b3461000257600160a060020a03600435166000908152600160205260409020545b60408051918252519081900360200190f35b346100025761064060058054604080516020601f600260001961010060018816150201909516949094049384018190048102820181019092528281529291908301828280156107035780601f106106d857610100808354040283529160200191610703565b34610002576106ae600435602435600160a060020a0333166000908152600160205260408120548290108015906103c65750600082115b1561071657600160a060020a03338116600081815260016020908152604080832080548890039055938716808352918490208054870190558351868152935191937fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929081900390910190a3506001610172565b3461000257604080516020600460443581810135601f81018490048402850184019095528484526106ae948235946024803595606494929391909201918190840183828082843750949650505050505050600160a060020a03338116600081815260026020908152604080832094881680845294825280832087905580518781529051929493927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925929181900390910190a383600160a060020a031660405180807f72656365697665417070726f76616c28616464726573732c75696e743235362c81526020017f616464726573732c627974657329000000000000000000000000000000000000815260200150602e019050604051809103902060e060020a9004338530866040518560e060020a0281526004018085600160a060020a0316815260200184815260200183600160a060020a031681526020018280519060200190808383829060006004602084601f0104600302600f01f150905090810190601f1680156105dd5780820380516001836020036101000a031916815260200191505b509450505050506000604051808303816000876161da5a03f192505050151561071e57610002565b3461000257610318600435602435600160a060020a03808316600090815260026020908152604080832093851683529290522054610172565b005b60405180806020018281038252838181518152602001915080519060200190808383829060006004602084601f0104600302600f01f150905090810190601f1680156106a05780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b604080519115158252519081900360200190f35b6040805160ff9092168252519081900360200190f35b820191906000526020600020905b8154815290600101906020018083116106e657829003601f168201915b505050505081565b5060005b9392505050565b506000610172565b50600161070f56";

    private HumanStandardToken(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    public Future<Utf8String> name() {
        Function function = new Function<Utf8String>("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<Utf8String>>asList(new TypeReference<Utf8String>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> approve(Address _spender, Uint256 _value) {
        Function function = new Function<Type>("approve", Arrays.<Type>asList(_spender, _value), Collections.<TypeReference<Type>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> totalSupply() {
        Function function = new Function<Uint256>("totalSupply", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<Uint256>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> transferFrom(Address _from, Address _to, Uint256 _value) {
        Function function = new Function<Type>("transferFrom", Arrays.<Type>asList(_from, _to, _value), Collections.<TypeReference<Type>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint8> decimals() {
        Function function = new Function<Uint8>("decimals", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<Uint8>>asList(new TypeReference<Uint8>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Utf8String> version() {
        Function function = new Function<Utf8String>("version", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<Utf8String>>asList(new TypeReference<Utf8String>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Uint256> balanceOf(Address _owner) {
        Function function = new Function<Uint256>("balanceOf", 
                Arrays.<Type>asList(_owner), 
                Arrays.<TypeReference<Uint256>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Utf8String> symbol() {
        Function function = new Function<Utf8String>("symbol", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<Utf8String>>asList(new TypeReference<Utf8String>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> transfer(Address _to, Uint256 _value) {
        Function function = new Function<Type>("transfer", Arrays.<Type>asList(_to, _value), Collections.<TypeReference<Type>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> approveAndCall(Address _spender, Uint256 _value, DynamicBytes _extraData) {
        Function function = new Function<Type>("approveAndCall", Arrays.<Type>asList(_spender, _value, _extraData), Collections.<TypeReference<Type>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> allowance(Address _owner, Address _spender) {
        Function function = new Function<Uint256>("allowance", 
                Arrays.<Type>asList(_owner, _spender), 
                Arrays.<TypeReference<Uint256>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<HumanStandardToken> deploy(Web3j web3j, Credentials credentials, BigInteger initialValue, Uint256 _initialAmount, Utf8String _tokenName, Uint8 _decimalUnits, Utf8String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_initialAmount, _tokenName, _decimalUnits, _tokenSymbol));
        return deployAsync(HumanStandardToken.class, web3j, credentials, BINARY, encodedConstructor, initialValue);
    }

    public EventValues processTransferEvent(TransactionReceipt transactionReceipt) {
        Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return extractEventParameters(event, transactionReceipt);
    }

    public EventValues processApprovalEvent(TransactionReceipt transactionReceipt) {
        Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return extractEventParameters(event, transactionReceipt);
    }

    public static HumanStandardToken load(String contractAddress, Web3j web3j, Credentials credentials) {
        return new HumanStandardToken(contractAddress, web3j, credentials);
    }
}
