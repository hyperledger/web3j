package org.web3j.abi.structs;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class DynamicStructWithUintAndString extends DynamicStructType {

    public DynamicStructWithUintAndString(BigInteger intval, String data) {
        this(new Uint256(intval), new Utf8String(data));
    }

    public DynamicStructWithUintAndString(Uint256 intval, Utf8String data) {
        super(Type.class, Arrays.asList(intval, data));
    }

    public static List<TypeReference> getTypes() {
        return Arrays.asList(new TypeReference<Uint256>() {
                             },
                new TypeReference<Utf8String>() {
                }
        );
    }
}