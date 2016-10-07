package org.web3j.generated;

import java.lang.InterruptedException;
import java.lang.String;
import java.util.concurrent.ExecutionException;
import org.web3j.abi.Contract;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;

/**
 * <p>Auto generated code.<br>
 * <strong>Do not modifiy!</strong><br>
 * Please use {@link org.web3j.codegen.SolidityFunctionWrapperGenerator} to update.</p>
 */
public final class Arrays extends Contract {
  public Arrays(String contractAddress, Web3j web3j) {
    super(contractAddress, web3j);
  }

  public StaticArray<Uint256> fixedReverse(StaticArray<Uint256> input) throws InterruptedException, ExecutionException {
    Function function = new Function<>("fixedReverse", 
        java.util.Arrays.asList(input), 
        java.util.Arrays.asList(new TypeReference<StaticArray<Uint256>>() {}));
    return executeSingleValueReturn(function);
  }

  public DynamicArray<Uint256> dynamicReverse(DynamicArray<Uint256> input) throws InterruptedException, ExecutionException {
    Function function = new Function<>("dynamicReverse", 
        java.util.Arrays.asList(input), 
        java.util.Arrays.asList(new TypeReference<DynamicArray<Uint256>>() {}));
    return executeSingleValueReturn(function);
  }
}
