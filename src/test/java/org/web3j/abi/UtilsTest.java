package org.web3j.abi;


import org.junit.Test;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.abi.datatypes.generated.Uint64;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UtilsTest {

    @Test
    public void testGetTypeName() throws ClassNotFoundException {
        assertThat(Utils.getTypeName(new TypeReference<Uint>(){}), is("uint256"));
        assertThat(Utils.getTypeName(new TypeReference<Int>(){}), is("int256"));
        assertThat(Utils.getTypeName(new TypeReference<Ufixed>(){}), is("ufixed256"));
        assertThat(Utils.getTypeName(new TypeReference<Fixed>(){}), is("fixed256"));

        assertThat(Utils.getTypeName(new TypeReference<Uint64>(){}), is("uint64"));
        assertThat(Utils.getTypeName(new TypeReference<Int64>(){}), is("int64"));
        assertThat(Utils.getTypeName(new TypeReference<Bool>(){}), is("bool"));
        assertThat(Utils.getTypeName(new TypeReference<Utf8String>(){}), is("string"));
        assertThat(Utils.getTypeName(new TypeReference<DynamicBytes>(){}), is("bytes"));

        assertThat(Utils.getTypeName(
                new TypeReference.StaticArrayTypeReference<StaticArray<Uint>>(5){}),
                is("uint256[5]"));
    }
}
