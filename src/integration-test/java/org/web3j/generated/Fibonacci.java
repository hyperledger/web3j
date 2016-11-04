package org.web3j.generated;

import java.lang.String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Future;
import org.web3j.abi.Contract;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.</p>
 */
public final class Fibonacci extends Contract {
    private static final String BINARY = "606060405260d18060106000396000f3606060405260e060020a60003504633c7fdc708114602657806361047ff4146044575b6002565b34600257605160043560006063825b600081151560a75750600060a2565b3460025760516004356035565b60408051918252519081900360200190f35b604080518481526020810183905281519293507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed929081900390910190a15b919050565b816001141560b65750600160a2565b60c0600283036035565b60ca600184036035565b01905060a256";

    private Fibonacci(String contractAddress, Web3j web3j, Credentials credentials) {
        super(contractAddress, web3j, credentials);
    }

    public Future<TransactionReceipt> fibonacciNotify(Uint256 number) {
        Function function = new Function<>("fibonacciNotify", Arrays.asList(number), Collections.emptyList());
        return executeTransactionAsync(function);
    }

    public Future<Uint256> fibonacci(Uint256 number) {
        Function function = new Function<>("fibonacci", 
                Arrays.asList(number), 
                Arrays.asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public EventValues processNotifyEvent(TransactionReceipt transactionReceipt) {
        Event event = new Event("Notify", 
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return extractEventParameters(event, transactionReceipt);
    }

    public static Future<Fibonacci> deploy(Web3j web3j, Credentials credentials, BigInteger initialValue) {
        return deployAsync(Fibonacci.class, web3j, credentials, BINARY, "", initialValue);
    }

    public static Fibonacci load(String contractAddress, Web3j web3j, Credentials credentials) {
        return new Fibonacci(contractAddress, web3j, credentials);
    }
}
