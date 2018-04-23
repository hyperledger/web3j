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
 * <p>Generated with web3j version 3.3.1.
 */
public class Arrays extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b61054c8061001e6000396000f30060606040526004361061006c5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633a69db9481146100715780633cac14c814610149578063a30cc5f61461015c578063b96f54d1146101d1578063beda363b14610246575b600080fd5b341561007c57600080fd5b6100f660046024813581810190830135806020818102016040519081016040528181529291906000602085015b828210156100e85760408083028601906002908051908101604052809291908260026020028082843750505091835250506001909101906020016100a9565b505050505091905050610295565b60405160208082528190810183818151815260200191508051906020019060200280838360005b8381101561013557808201518382015260200161011d565b505050509050019250505060405180910390f35b341561015457600080fd5b6100f6610352565b341561016757600080fd5b6100f6600461018481600660c060405190810160405291906000835b828210156101c45783826040020160028060200260405190810160405280929190826002602002808284375050509183525050600190910190602001610183565b505050509190505061037f565b34156101dc57600080fd5b61020d600461014481600a610140604051908101604052919082826101408082843750939550610424945050505050565b604051808261014080838360005b8381101561023357808201518382015260200161021b565b5050505090500191505060405180910390f35b341561025157600080fd5b6100f6600460248135818101908301358060208181020160405190810160405280939291908181526020018383602002808284375094965061047095505050505050565b61029d6104e6565b600080600084519250826002026040518059106102b75750595b9080825280602002602001820160405250935060009150600090505b8281101561034a578481815181106102e757fe5b90602001906020020151518483815181106102fe57fe5b6020908102909101015260019091019084818151811061031a57fe5b906020019060200201516020015184838151811061033457fe5b60209081029091010152600191820191016102d3565b505050919050565b61035a6104e6565b60006040518059106103695750595b9080825280602002602001820160405250905090565b6103876104e6565b6006600080600c60405180591061039b5750595b9080825280602002602001820160405250935060009150600090505b8281101561034a578481600681106103cb57fe5b6020020151518483815181106103dd57fe5b602090810290910101526001909101908481600681106103f957fe5b60200201516020015184838151811061040e57fe5b60209081029091010152600191820191016103b7565b61042c6104f8565b600a60005b81811015610469578360001982840301600a811061044b57fe5b60200201518382600a811061045c57fe5b6020020152600101610431565b5050919050565b6104786104e6565b600080835191508160405180591061048d5750595b90808252806020026020018201604052509250600090505b818110156104695783816001018303815181106104be57fe5b906020019060200201518382815181106104d457fe5b602090810290910101526001016104a5565b60206040519081016040526000815290565b610140604051908101604052600a815b600081526020019060019003908161050857905050905600a165627a7a72305820e08a3705b5896ac5c6174880ecb237d69f7f687310914ccf81af1c32c05a11080029\n";

    public static final String FUNC_MULTIDYNAMIC = "multiDynamic";

    public static final String FUNC_RETURNARRAY = "returnArray";

    public static final String FUNC_MULTIFIXED = "multiFixed";

    public static final String FUNC_FIXEDREVERSE = "fixedReverse";

    public static final String FUNC_DYNAMICREVERSE = "dynamicReverse";

    protected Arrays(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Arrays(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> multiDynamic(List<List<BigInteger>> input) {
        final Function function = new Function(
                FUNC_MULTIDYNAMIC, 
                java.util.Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray(
                        org.web3j.abi.Utils.typeMap(input, org.web3j.abi.datatypes.generated.StaticArray2.class, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> returnArray() {
        final Function function = new Function(FUNC_RETURNARRAY, 
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

    public RemoteCall<TransactionReceipt> multiFixed(List<List<BigInteger>> input) {
        final Function function = new Function(
                FUNC_MULTIFIXED, 
                java.util.Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray6(
                        org.web3j.abi.Utils.typeMap(input, org.web3j.abi.datatypes.generated.StaticArray2.class, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> fixedReverse(List<BigInteger> input) {
        final Function function = new Function(
                FUNC_FIXEDREVERSE, 
                java.util.Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray10<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.Utils.typeMap(input, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> dynamicReverse(List<BigInteger> input) {
        final Function function = new Function(
                FUNC_DYNAMICREVERSE, 
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
