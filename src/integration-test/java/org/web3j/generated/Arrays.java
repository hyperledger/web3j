package org.web3j.generated;

import java.math.BigInteger;
import java.util.Collections;
import java.util.concurrent.Future;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version 2.2.0.
 */
public final class Arrays extends Contract {
    private static final String BINARY = "6060604052341561000c57fe5b5b61028c8061001c6000396000f300606060405263ffffffff60e060020a600035041663b96f54d1811461002c578063beda363b146100a6575bfe5b341561003457fe5b6040805161014081810190925261006891600491610144918390600a90839083908082843750939550610154945050505050565b60405180826101408083835b80518252602083111561009457601f199092019160209182019101610074565b50505090500191505060405180910390f35b34156100ae57fe5b6100f96004808035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437509496506101a295505050505050565b6040805160208082528351818301528351919283929083019185810191028083838215610141575b80518252602083111561014157601f199092019160209182019101610121565b5050509050019250505060405180910390f35b61015c610225565b600a60005b8181101561019a578360001982840301600a811061017b57fe5b60200201518382600a811061018c57fe5b60200201525b600101610161565b5b5050919050565b6101aa61024e565b815160405160009082908059106101be5750595b908082528060200260200182016040525b509250600090505b8181101561019a57838160010183038151811015156101f257fe5b90602001906020020151838281518110151561020a57fe5b602090810290910101525b6001016101d7565b5b5050919050565b61014060405190810160405280600a905b60008152602001906001900390816102365790505090565b604080516020810190915260008152905600a165627a7a7230582055320d18bbf364dc41bf0b6885a12ec739cacc25a300af31a3105b09ee5151d80029";

    private Arrays(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Arrays(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Future<TransactionReceipt> fixedReverse(StaticArray<Uint256> input) {
        Function function = new Function("fixedReverse", java.util.Arrays.<Type>asList(input), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> dynamicReverse(DynamicArray<Uint256> input) {
        Function function = new Function("dynamicReverse", java.util.Arrays.<Type>asList(input), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<Arrays> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(Arrays.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Future<Arrays> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(Arrays.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Arrays load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Arrays(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Arrays load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Arrays(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
