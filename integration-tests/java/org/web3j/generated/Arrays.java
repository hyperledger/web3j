package org.web3j.generated;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
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
 * <p>Generated with web3j version 3.0.0-alpha3.
 */
public final class Arrays extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6102868061001e6000396000f300606060405263ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663b96f54d18114610047578063beda363b146100bc57600080fd5b341561005257600080fd5b610083600461014481600a61014060405190810160405291908282610140808284375093955061015e945050505050565b604051808261014080838360005b838110156100a9578082015183820152602001610091565b5050505090500191505060405180910390f35b34156100c757600080fd5b61010b60046024813581810190830135806020818102016040519081016040528093929190818152602001838360200280828437509496506101aa95505050505050565b60405160208082528190810183818151815260200191508051906020019060200280838360005b8381101561014a578082015183820152602001610132565b505050509050019250505060405180910390f35b610166610220565b600a60005b818110156101a3578360001982840301600a811061018557fe5b60200201518382600a811061019657fe5b602002015260010161016b565b5050919050565b6101b2610248565b60008083519150816040518059106101c75750595b90808252806020026020018201604052509250600090505b818110156101a35783816001018303815181106101f857fe5b9060200190602002015183828151811061020e57fe5b602090810290910101526001016101df565b610140604051908101604052600a815b60008152602001906001900390816102305790505090565b602060405190810160405260008152905600a165627a7a72305820740687f43e378347b6ad47b837a137cf023c0a1a53b6730025e554085824fa350029";

    private Arrays(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Arrays(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> fixedReverse(List<BigInteger> input) {
        Function function = new Function(
                "fixedReverse", 
                java.util.Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray10<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(input, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> dynamicReverse(List<BigInteger> input) {
        Function function = new Function(
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
