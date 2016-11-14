package org.web3j.codegen;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.hamcrest.core.Is;
import org.junit.Test;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.web3j.codegen.SolidityFunctionWrapperGenerator.*;


public class SolidityFunctionWrapperGeneratorTest {

    @Test
    public void testCreateValidParamName() {
        assertThat(createValidParamName("param", 1), is("param"));
        assertThat(createValidParamName("", 1), is("param1"));
    }

    @Test
    public void testBuildTypeName() {
        assertThat(buildTypeName("uint256"),
                Is.<TypeName>is(ClassName.get(Uint256.class)));
        assertThat(buildTypeName("uint64"),
                Is.<TypeName>is(ClassName.get(Uint64.class)));
        assertThat(buildTypeName("string"),
                Is.<TypeName>is(ClassName.get(Utf8String.class)));

        assertThat(buildTypeName("uint256[]"),
                Is.<TypeName>is(ParameterizedTypeName.get(DynamicArray.class, Uint256.class)));
        assertThat(buildTypeName("uint256[10]"),
                Is.<TypeName>is(ParameterizedTypeName.get(StaticArray.class, Uint256.class)));
    }

    @Test
    public void testGetFileNoExtension() {
        assertThat(getFileNameNoExtension(""), is(""));
        assertThat(getFileNameNoExtension("file"), is("file"));
        assertThat(getFileNameNoExtension("file."), is("file"));
        assertThat(getFileNameNoExtension("file.txt"), is("file"));
    }
}
