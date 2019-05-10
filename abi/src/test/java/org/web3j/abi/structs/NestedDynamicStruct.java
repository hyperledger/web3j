package org.web3j.abi.structs;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicStructType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class NestedDynamicStruct extends DynamicStructType {
    //
    public static class Foo extends DynamicStructType {

        public Foo(BigInteger fooId, String fooName) {
            this(new Uint256(fooId), new Utf8String(fooName));
        }

        public Foo(Uint256 fooId, Utf8String fooName) {
            super(Arrays.asList(fooId, fooName));
        }

        public static List<TypeReference> getTypes() {
            return Arrays.asList(
                    new TypeReference<Uint256>() {
                    },
                    new TypeReference<Utf8String>() {
                    });
        }
    }

    public NestedDynamicStruct(String first, String second, NestedDynamicStruct.Foo fooPart) {
        this(new Utf8String(first), new Utf8String(second), fooPart);
    }

    public NestedDynamicStruct(Utf8String first, Utf8String second, NestedDynamicStruct.Foo fooPart) {
        super(Type.class, Arrays.asList(first, second, fooPart));
    }

    public static List<TypeReference> getTypes() {
        return Arrays.asList(
                new TypeReference<Utf8String>() {
                },
                new TypeReference<Utf8String>() {
                },
                new TypeReference<NestedDynamicStruct.Foo>() {
                });
    }

}