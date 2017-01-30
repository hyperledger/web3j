package org.web3j.generated;

import java.lang.String;
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
 * <p>Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.</p>
 */
public final class Arrays extends Contract {
    private static final String BINARY = "60606040526101f5806100126000396000f3606060405260e060020a6000350463b96f54d18114610029578063beda363b146100bc575b610002565b34610002576040805161014081810190925261018191600491610144918390600a90839083908082843750909550505050505061014060405190810160405280600a905b600081526020019060019003908161006d5750600a905060005b818110156101ee5783816001018303600a8110156100025760200201518382600a811015610002576020020152600101610087565b3461000257604080516020600480358082013583810280860185019096528085526101a49592946024949093928501928291850190849080828437509496505050505050506040805160208101825260008082528351925191929182908059106101235750595b90808252806020026020018201604052801561013a575b509250600090505b818110156101ee578381600101830381518110156100025790602001906020020151838281518110156100025760209081029091010152600101610142565b60405180826101408083818460006004602df15090500191505060405180910390f35b60405180806020018281038252838181518152602001915080519060200190602002808383829060006004602084601f0104600302600f01f1509050019250505060405180910390f35b505091905056";

    private Arrays(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Arrays(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public Future<TransactionReceipt> fixedReverse(StaticArray<Uint256> input) {
        Function function = new Function("fixedReverse", java.util.Arrays.<Type>asList(input), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public Future<TransactionReceipt> dynamicReverse(DynamicArray<Uint256> input) {
        Function function = new Function("dynamicReverse", java.util.Arrays.<Type>asList(input), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public static Future<Arrays> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(Arrays.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static Future<Arrays> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialValue) {
        return deployAsync(Arrays.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialValue);
    }

    public static Arrays load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Arrays(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Arrays load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Arrays(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
