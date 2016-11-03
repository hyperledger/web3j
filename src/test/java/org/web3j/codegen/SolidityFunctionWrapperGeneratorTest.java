package org.web3j.codegen;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import org.junit.Test;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SolidityFunctionWrapperGeneratorTest {

    @Test
    public void testBuildTypeName() {
        assertThat(SolidityFunctionWrapperGenerator.buildTypeName("uint256"),
                is(ClassName.get(Uint256.class)));
        assertThat(SolidityFunctionWrapperGenerator.buildTypeName("uint64"),
                is(ClassName.get(Uint64.class)));
        assertThat(SolidityFunctionWrapperGenerator.buildTypeName("string"),
                is(ClassName.get(Utf8String.class)));

        assertThat(SolidityFunctionWrapperGenerator.buildTypeName("uint256[]"),
                is(ParameterizedTypeName.get(DynamicArray.class, Uint256.class)));
        assertThat(SolidityFunctionWrapperGenerator.buildTypeName("uint256[10]"),
                is(ParameterizedTypeName.get(StaticArray.class, Uint256.class)));
    }

    @Test
    public void testGetFileNoExtension() {
        assertThat(SolidityFunctionWrapperGenerator.getFileNameNoExtension(""), is(""));
        assertThat(SolidityFunctionWrapperGenerator.getFileNameNoExtension("file"), is("file"));
        assertThat(SolidityFunctionWrapperGenerator.getFileNameNoExtension("file."), is("file"));
        assertThat(SolidityFunctionWrapperGenerator.getFileNameNoExtension("file.txt"), is("file"));
    }
}
