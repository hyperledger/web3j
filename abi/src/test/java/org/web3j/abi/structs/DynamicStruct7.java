package org.web3j.abi.structs;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class DynamicStruct7 extends DynamicStructType {

    public DynamicStruct7(BigInteger v1, BigInteger v2, String v3, String v4, BigInteger v5, boolean v6, String v7, BigInteger v8) {
        this(new Uint256(v1), new Uint256(v2), new Utf8String(v3), new Utf8String(v4), new Uint256(v5), new Bool(v6), new Utf8String(v7), new Uint256(v8));
    }

    public DynamicStruct7(Uint256 v1, Uint256 v2, Utf8String v3, Utf8String v4, Uint256 v5, Bool v6, Utf8String v7, Uint256 v8) {
        super(Type.class, Arrays.asList(v1, v2, v3, v4, v5, v6, v7, v8));
    }

    public static List<TypeReference> getTypes() {
        return Arrays.asList(new TypeReference<Uint256>() {
                             },
                new TypeReference<Uint256>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Uint256>() {
                },
                new TypeReference<Bool>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Uint256>() {
                }
        );
    }
}
