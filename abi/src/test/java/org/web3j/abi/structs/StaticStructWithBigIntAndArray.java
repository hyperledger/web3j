package org.web3j.abi.structs;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.StaticStructType;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StaticStructWithBigIntAndArray extends StaticStructType {

    // Assume 2 args
    public StaticStructWithBigIntAndArray(BigInteger data, BigInteger[] array) {
        this(new Uint256(data), new StaticArray2<>(Uint256.class,
                Arrays.stream(array).map(t -> new Uint256(t)).collect(Collectors.toList())));
    }

    public StaticStructWithBigIntAndArray(Uint256 data, StaticArray<Uint256> array) {
        super(Arrays.asList(data, array));
    }

    public static List<TypeReference> getTypes() {
        return Arrays.asList(
                new TypeReference<Uint256>() {
                },
                new TypeReference.StaticArrayTypeReference<Uint256>(2) {
                });
    }

}
