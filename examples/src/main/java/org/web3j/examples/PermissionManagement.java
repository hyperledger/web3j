package org.web3j.examples;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class PermissionManagement extends Contract {
    private static final int version = 0;
    private static final String BINARY = "606060405260008054600160a060020a031990811663013241b31791829055600180548216600160a060020a0393841617905560028054821663013241b41790819055600380549092169216919091179055341561005c57600080fd5b6108108061006b6000396000f3006060604052600436106100745763ffffffff60e060020a6000350416630f5aa9f381146100795780633482e0c9146100b2578063537bf9a3146100d75780636446ebd8146100f957806398a05bb114610196578063a5925b5b146101b5578063f036ed56146101d4578063fc4a089c14610271575b600080fd5b341561008457600080fd5b61009e600160a060020a0360043581169060243516610321565b604051901515815260200160405180910390f35b34156100bd57600080fd5b61009e600160a060020a03600435811690602435166103a5565b34156100e257600080fd5b61009e600160a060020a0360043516602435610408565b341561010457600080fd5b61009e60048035600160a060020a03169060446024803590810190830135806020808202016040519081016040528093929190818152602001838360200280828437820191505050505050919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284375094965061047b95505050505050565b34156101a157600080fd5b61009e600160a060020a0360043516610575565b34156101c057600080fd5b61009e600160a060020a03600435166105d2565b34156101df57600080fd5b61009e60048035600160a060020a03169060446024803590810190830135806020808202016040519081016040528093929190818152602001838360200280828437820191505050505050919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284375094965061064d95505050505050565b341561027c57600080fd5b61030560048035906044602480359081019083013580602081810201604051908101604052809392919081815260200183836020028082843782019150505050505091908035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437509496506106be95505050505050565b604051600160a060020a03909116815260200160405180910390f35b600354600090600160a060020a031663f10a77988484846040516020015260405160e060020a63ffffffff8516028152600160a060020a03928316600482015291166024820152604401602060405180830381600087803b151561038457600080fd5b6102c65a03f1151561039557600080fd5b5050506040518051949350505050565b600354600090600160a060020a0316630c0a5c558484846040516020015260405160e060020a63ffffffff8516028152600160a060020a03928316600482015291166024820152604401602060405180830381600087803b151561038457600080fd5b600082600160a060020a038116631ae97bd984846040516020015260405160e060020a63ffffffff84160281526004810191909152602401602060405180830381600087803b151561045957600080fd5b6102c65a03f1151561046a57600080fd5b505050604051805195945050505050565b600083600160a060020a038116632f8cfe0e858585604051602001526040518363ffffffff1660e060020a028152600401808060200180602001838103835285818151815260200191508051906020019060200280838360005b838110156104ed5780820151838201526020016104d5565b50505050905001838103825284818151815260200191508051906020019060200280838360005b8381101561052c578082015183820152602001610514565b50505050905001945050505050602060405180830381600087803b151561055257600080fd5b6102c65a03f1151561056357600080fd5b50505060405180519695505050505050565b600081600160a060020a0381166343d726d66040518163ffffffff1660e060020a028152600401600060405180830381600087803b15156105b557600080fd5b6102c65a03f115156105c657600080fd5b50600195945050505050565b600354600090600160a060020a031663b4026ed583836040516020015260405160e060020a63ffffffff8416028152600160a060020a039091166004820152602401602060405180830381600087803b151561062d57600080fd5b6102c65a03f1151561063e57600080fd5b50505060405180519392505050565b600083600160a060020a0381166387f0bf31858585604051602001526040518363ffffffff1660e060020a02815260040180806020018060200183810383528581815181526020019150805190602001906020028083836000838110156104ed5780820151838201526020016104d5565b6000828260008251116106d057600080fd5b80518251146106de57600080fd5b600154600160a060020a031663ae8f1d2987878760006040516020015260405160e060020a63ffffffff8616028152600481018481526060602483019081529091604481019060640185818151815260200191508051906020019060200280838360005b8381101561075a578082015183820152602001610742565b50505050905001838103825284818151815260200191508051906020019060200280838360005b83811015610799578082015183820152602001610781565b5050505090500195505050505050602060405180830381600087803b15156107c057600080fd5b6102c65a03f115156107d157600080fd5b50505060405180519796505050505050505600a165627a7a72305820c9c78cdadca32aeb7f73a088394ca00ecbb34284dd2de653b3d75cf8891638190029";
    private  static  final int chainId = 1;
    protected PermissionManagement(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(BINARY, contractAddress, web3j, transactionManager);
    }

    public RemoteCall<TransactionReceipt> setAuthorization(String _account, String _permission, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, BigInteger chainId) {
        Function function = new Function(
                "setAuthorization", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_account), 
                new org.web3j.abi.datatypes.Address(_permission)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), chainId);
    }

    public RemoteCall<TransactionReceipt> cancelAuthorization(String _account, String _permission, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, int chainId) {
        Function function = new Function(
                "cancelAuthorization", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_account), 
                new org.web3j.abi.datatypes.Address(_permission)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), BigInteger.valueOf(chainId));
    }

    public RemoteCall<TransactionReceipt> updatePermissionName(String _permission, byte[] _name, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, BigInteger chainId) {
        Function function = new Function(
                "updatePermissionName", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_permission), 
                new org.web3j.abi.datatypes.generated.Bytes32(_name)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), chainId);
    }

    public RemoteCall<TransactionReceipt> deleteResources(String _permission, List<String> _conts, List<byte[]> _funcs, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, BigInteger chainId) {
        Function function = new Function(
                "deleteResources", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_permission), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_conts, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes4>(
                        org.web3j.abi.Utils.typeMap(_funcs, org.web3j.abi.datatypes.generated.Bytes4.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), chainId);
    }

    public RemoteCall<TransactionReceipt> deletePermission(String _permission, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, BigInteger chainId) {
        Function function = new Function(
                "deletePermission", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_permission)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), chainId);
    }

    public RemoteCall<TransactionReceipt> clearAuthorization(String _account, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version,BigInteger chainId) {
        Function function = new Function(
                "clearAuthorization", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), chainId);
    }

    public RemoteCall<TransactionReceipt> addResources(String _permission, List<String> _conts, List<byte[]> _funcs, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, BigInteger chainId) {
        Function function = new Function(
                "addResources", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_permission), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_conts, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes4>(
                        org.web3j.abi.Utils.typeMap(_funcs, org.web3j.abi.datatypes.generated.Bytes4.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), chainId);
    }

    public RemoteCall<TransactionReceipt> newPermission(byte[] _name, List<String> _conts, List<byte[]> _funcs, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version, BigInteger chainId) {
        Function function = new Function(
                "newPermission", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_name), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(_conts, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes4>(
                        org.web3j.abi.Utils.typeMap(_funcs, org.web3j.abi.datatypes.generated.Bytes4.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, BigInteger.valueOf(version), chainId);
    }

    public static RemoteCall<PermissionManagement> deploy(Web3j web3j, TransactionManager transactionManager,
                                                          BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, int version) {
        return deployRemoteCall(PermissionManagement.class, web3j, transactionManager, quota, nonce, validUntilBlock, BigInteger.valueOf(version), BigInteger.valueOf(chainId), BINARY, "");
    }

    public static PermissionManagement load(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        return new PermissionManagement(contractAddress, web3j, transactionManager);
    }
}
