package org.web3j.examples;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class PermissionManager extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b604051610a18380380610a188339810160405280805182019190602001805190910190506000805b83518210156100f557600080546001810161005283826101bb565b9160005260206000209001600086858151811061006b57fe5b90602001906020020151909190916101000a815481600160a060020a030219169083600160a060020a03160217905550506001600260008685815181106100ae57fe5b90602001906020020151600160a060020a031681526020810191909152604001600020805460ff191660018360028111156100e557fe5b0217905550600190910190610037565b5060005b82518110156101b2576001805480820161011383826101bb565b9160005260206000209001600085848151811061012c57fe5b90602001906020020151909190916101000a815481600160a060020a030219169083600160a060020a0316021790555050600280600085848151811061016e57fe5b90602001906020020151600160a060020a031681526020810191909152604001600020805460ff191660018360028111156101a557fe5b02179055506001016100f9565b50505050610205565b8154818355818115116101df576000838152602090206101df9181019083016101e4565b505050565b61020291905b808211156101fe57600081556001016101ea565b5090565b90565b610804806102146000396000f30060606040526004361061006c5763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663301da870811461007157806354ad6352146100aa5780636f4eaf7a146100df578063c390355c1461014b578063dd8a8a051461018e575b600080fd5b341561007c57600080fd5b610096600160a060020a036004351660ff602435166101b3565b604051901515815260200160405180910390f35b34156100b557600080fd5b6100c9600160a060020a036004351661035f565b60405160ff909116815260200160405180910390f35b34156100ea57600080fd5b6100f860ff6004351661038f565b60405160208082528190810183818151815260200191508051906020019060200280838360005b8381101561013757808201518382015260200161011f565b505050509050019250505060405180910390f35b341561015657600080fd5b61016a600160a060020a0360043516610471565b6040518082600281111561017a57fe5b60ff16815260200191505060405180910390f35b341561019957600080fd5b610096600160a060020a036004351660ff60243516610486565b33600160a060020a0381166000908152600260208190526040822054919291849160ff8084169216908111156101e557fe5b60ff16146101f257600080fd5b600160a060020a0385166000908152600260208190526040909120548691869160ff8084169291169081111561022457fe5b60ff161061023157600080fd5b60ff86166001141561029d57600080546001810161024f838261077c565b506000918252602080832091909101805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a038b1690811790915582526002905260409020805460ff191660011790555b60ff86166002141561030b57600180548082016102ba838261077c565b506000918252602080832091909101805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a038b1690811790915582526002908190526040909120805460ff191690911790555b7fca571b17c94502f9fcce67874fd8e4ac41a6139e5dd6f79836393bcc12a0e7658787604051600160a060020a03909216825260ff1660208201526040908101905180910390a15060019695505050505050565b600160a060020a038116600090815260026020819052604082205460ff169081111561038757fe5b90505b919050565b6103976107a5565b60ff8216600114156104055760008054806020026020016040519081016040528092919081815260200182805480156103f957602002820191906000526020600020905b8154600160a060020a031681526001909101906020018083116103db575b5050505050905061038a565b60ff82166002141561038a5760018054806020026020016040519081016040528092919081815260200182805480156103f957602002820191906000526020600020908154600160a060020a031681526001909101906020018083116103db575050505050905061038a565b60026020526000908152604090205460ff1681565b33600160a060020a0381166000908152600260208190526040822054919291849160ff8084169216908111156104b857fe5b60ff1610156104c657600080fd5b600160a060020a0385166000908152600260208190526040909120548691869160ff808416929116908111156104f857fe5b60ff161461050557600080fd5b8633600160a060020a031681600160a060020a03161415151561052757600080fd5b600160a060020a0388166000908152600260205260409020805460ff1916905560ff8716600114156105605761055e8860006105ce565b505b60ff871660021415610579576105778860016105ce565b505b7f76a0b4b4ddd449a15dd0e157534f1220234491c2643c254ead34327e6f51952e8888604051600160a060020a03909216825260ff1660208201526040908101905180910390a1506001979650505050505050565b6000806000610636858580548060200260200160405190810160405280929190818152602001828054801561062c57602002820191906000526020600020905b8154600160a060020a0316815260019091019060200180831161060e575b5050505050610725565b8454909250821061064a576000925061071d565b50805b8354600019018110156106cf57838160010181548110151561066b57fe5b6000918252602090912001548454600160a060020a039091169085908390811061069157fe5b6000918252602090912001805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039290921691909117905560010161064d565b8354849060001981019081106106e157fe5b6000918252602090912001805473ffffffffffffffffffffffffffffffffffffffff19169055835461071785600019830161077c565b50600192505b505092915050565b6000805b82518110156107715782818151811061073e57fe5b90602001906020020151600160a060020a031684600160a060020a0316141561076957809150610775565b600101610729565b8091505b5092915050565b8154818355818115116107a0576000838152602090206107a09181019083016107b7565b505050565b60206040519081016040526000815290565b6107d591905b808211156107d157600081556001016107bd565b5090565b905600a165627a7a72305820e7be3734f7f88eecc278d0a1a7dc1d765c4912a0f2b7e352f8bfe7ec6700c1070029";

    protected PermissionManager(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(BINARY, contractAddress, web3j, transactionManager);
    }

    public List<GrantPermissionEventResponse> getGrantPermissionEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("GrantPermission", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint8>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<GrantPermissionEventResponse> responses = new ArrayList<GrantPermissionEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            GrantPermissionEventResponse typedResponse = new GrantPermissionEventResponse();
            typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._permission = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<GrantPermissionEventResponse> grantPermissionEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("GrantPermission", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint8>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, GrantPermissionEventResponse>() {
            @Override
            public GrantPermissionEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                GrantPermissionEventResponse typedResponse = new GrantPermissionEventResponse();
                typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._permission = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<RevokePermissionEventResponse> getRevokePermissionEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("RevokePermission", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint8>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<RevokePermissionEventResponse> responses = new ArrayList<RevokePermissionEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            RevokePermissionEventResponse typedResponse = new RevokePermissionEventResponse();
            typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._permission = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RevokePermissionEventResponse> revokePermissionEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("RevokePermission", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint8>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, RevokePermissionEventResponse>() {
            @Override
            public RevokePermissionEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                RevokePermissionEventResponse typedResponse = new RevokePermissionEventResponse();
                typedResponse._user = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._permission = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> grantPermission(String _user, BigInteger _permission, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, int chainId) {
        Function function = new Function(
                "grantPermission", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.generated.Uint8(_permission)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), BigInteger.valueOf(chainId));
    }

    public RemoteCall<BigInteger> queryPermission(String _user) {
        Function function = new Function("queryPermission", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<List> queryUsersOfPermission(BigInteger _permission) {
        Function function = new Function("queryUsersOfPermission", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(_permission)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return executeRemoteCallSingleValueReturn(function, List.class);
    }

    public RemoteCall<BigInteger> user_permission(String param0) {
        Function function = new Function("user_permission", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> revokePermission(String _user, BigInteger _permission, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, int chainId) {
        Function function = new Function(
                "revokePermission", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_user), 
                new org.web3j.abi.datatypes.generated.Uint8(_permission)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), BigInteger.valueOf(chainId));
    }

    public static RemoteCall<PermissionManager> deploy(Web3j web3j, TransactionManager transactionManager,
                                                       BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, int chainId, List<String> _senders, List<String> _creators) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_senders, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_creators, org.web3j.abi.datatypes.Address.class))));
        return deployRemoteCall(PermissionManager.class, web3j, transactionManager, quota, nonce, validUntilBlock, BigInteger.valueOf(version), BigInteger.valueOf(chainId), BINARY, encodedConstructor);
    }

    public static PermissionManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        return new PermissionManager(contractAddress, web3j, transactionManager);
    }

    public static class GrantPermissionEventResponse {
        public String _user;

        public BigInteger _permission;
    }

    public static class RevokePermissionEventResponse {
        public String _user;

        public BigInteger _permission;
    }
}
