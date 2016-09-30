package org.web3j.generated;

import java.lang.InterruptedException;
import java.lang.String;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import org.web3j.abi.Contract;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.</p>
 */
public final class Fibonacci extends Contract {
  public Fibonacci(String contractAddress, Web3j web3j) {
    super(contractAddress, web3j);
  }

  public Uint256 fibonacciNotify(Uint256 number) throws InterruptedException, ExecutionException {
    Function function = new Function<>("fibonacciNotify", Arrays.asList(number), Arrays.asList(Uint256.class));
    return executeSingleValueReturn(function);
  }

  public Uint256 fibonacci(Uint256 number) throws InterruptedException, ExecutionException {
    Function function = new Function<>("fibonacci", Arrays.asList(number), Arrays.asList(Uint256.class));
    return executeSingleValueReturn(function);
  }
}
