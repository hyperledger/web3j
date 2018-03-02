package org.web3j.generated;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
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
 * <p>Generated with web3j version none.
 */
public class Arrays extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6102da8061001e6000396000f3006060604052600436106100565763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633cac14c8811461005b578063b96f54d1146100c1578063beda363b14610136575b600080fd5b341561006657600080fd5b61006e610185565b60405160208082528190810183818151815260200191508051906020019060200280838360005b838110156100ad578082015183820152602001610095565b505050509050019250505060405180910390f35b34156100cc57600080fd5b6100fd600461014481600a6101406040519081016040529190828261014080828437509395506101b2945050505050565b604051808261014080838360005b8381101561012357808201518382015260200161010b565b5050505090500191505060405180910390f35b341561014157600080fd5b61006e60046024813581810190830135806020818102016040519081016040528093929190818152602001838360200280828437509496506101fe95505050505050565b61018d610274565b600060405180591061019c5750595b9080825280602002602001820160405250905090565b6101ba610286565b600a60005b818110156101f7578360001982840301600a81106101d957fe5b60200201518382600a81106101ea57fe5b60200201526001016101bf565b5050919050565b610206610274565b600080835191508160405180591061021b5750595b90808252806020026020018201604052509250600090505b818110156101f757838160010183038151811061024c57fe5b9060200190602002015183828151811061026257fe5b60209081029091010152600101610233565b60206040519081016040526000815290565b610140604051908101604052600a815b600081526020019060019003908161029657905050905600a165627a7a72305820305df0b85cbf8cf9c0720830cf03914a3044a0459d83288859a24d3dd311ef840029";

    protected Arrays(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Arrays(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<List> returnArray() {
        final Function function = new Function("returnArray", 
                java.util.Arrays.<Type>asList(), 
                java.util.Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<TransactionReceipt> fixedReverse(List<BigInteger> input) {
        final Function function = new Function(
                "fixedReverse", 
                java.util.Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray10<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(input, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> dynamicReverse(List<BigInteger> input) {
        final Function function = new Function(
                "dynamicReverse", 
                java.util.Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(input, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<Arrays> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Arrays.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Arrays> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Arrays.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Arrays load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Arrays(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Arrays load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Arrays(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
