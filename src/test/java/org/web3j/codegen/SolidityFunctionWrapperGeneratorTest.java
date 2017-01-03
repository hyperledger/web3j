package org.web3j.codegen;


import javax.tools.*;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import org.junit.Test;

import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.utils.Strings;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.web3j.codegen.SolidityFunctionWrapperGenerator.*;


public class SolidityFunctionWrapperGeneratorTest extends GeneratorBase {

    private String solidityBaseDir;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        URL url = SolidityFunctionWrapperGeneratorTest.class.getClass().getResource("/solidity");
        solidityBaseDir = url.getPath();
    }

    @Test
    public void testCreateValidParamName() {
        assertThat(createValidParamName("param", 1), is("param"));
        assertThat(createValidParamName("", 1), is("param1"));
    }

    @Test
    public void testBuildTypeName() {
        assertThat(buildTypeName("uint256"),
                is(ClassName.get(Uint256.class)));
        assertThat(buildTypeName("uint64"),
                is(ClassName.get(Uint64.class)));
        assertThat(buildTypeName("string"),
                is(ClassName.get(Utf8String.class)));

        assertThat(buildTypeName("uint256[]"),
                is(ParameterizedTypeName.get(DynamicArray.class, Uint256.class)));
        assertThat(buildTypeName("uint256[10]"),
                is(ParameterizedTypeName.get(StaticArray.class, Uint256.class)));
    }

    @Test
    public void testGetFileNoExtension() {
        assertThat(getFileNameNoExtension(""), is(""));
        assertThat(getFileNameNoExtension("file"), is("file"));
        assertThat(getFileNameNoExtension("file."), is("file"));
        assertThat(getFileNameNoExtension("file.txt"), is("file"));
    }

    @Test
    public void testBuildFunctionTransaction() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                false,
                Arrays.asList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.emptyList(),
                "type",
                false);

        MethodSpec methodSpec = buildFunction(functionDefinition);

        String expected = "public java.util.concurrent.Future<org.web3j.protocol.core.methods.response.TransactionReceipt> functionName(org.web3j.abi.datatypes.generated.Uint8 param) {\n" +
                "  org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(\"functionName\", java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(param), java.util.Collections.<org.web3j.abi.TypeReference<?>>emptyList());\n" +
                "  return executeTransactionAsync(function);\n" +
                "}\n";

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test
    public void testBuildFunctionConstantSingleValueReturn() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                true,
                Arrays.asList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Arrays.asList(
                        new AbiDefinition.NamedType("result", "int8")),
                "type",
                false);

        MethodSpec methodSpec = buildFunction(functionDefinition);

        String expected = "public java.util.concurrent.Future<org.web3j.abi.datatypes.generated.Int8> functionName(org.web3j.abi.datatypes.generated.Uint8 param) {\n" +
                "  org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(\"functionName\", \n" +
                "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(param), \n" +
                "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int8>() {}));\n" +
                "  return executeCallSingleValueReturnAsync(function);\n" +
                "}\n";

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test(expected = RuntimeException.class)
    public void testBuildFunctionConstantInvalid() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                true,
                Arrays.asList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.emptyList(),
                "type",
                false);

        buildFunction(functionDefinition);
    }

    @Test
    public void testBuildFunctionConstantMultipleValueReturn() throws Exception {

        AbiDefinition functionDefinition = new AbiDefinition(
                true,
                Arrays.asList(
                        new AbiDefinition.NamedType("param1", "uint8"),
                        new AbiDefinition.NamedType("param2", "uint32")),
                "functionName",
                Arrays.asList(
                        new AbiDefinition.NamedType("result1", "int8"),
                        new AbiDefinition.NamedType("result2", "int32")),
                "type",
                false);

        MethodSpec methodSpec = buildFunction(functionDefinition);

        String expected = "public java.util.concurrent.Future<java.util.List<org.web3j.abi.datatypes.Type>> functionName(org.web3j.abi.datatypes.generated.Uint8 param1, org.web3j.abi.datatypes.generated.Uint32 param2) {\n" +
                "  org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(\"functionName\", \n" +
                "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(param1, param2), \n" +
                "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int8>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int32>() {}));\n" +
                "  return executeCallMultipleValueReturnAsync(function);\n" +
                "}\n";

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test
    public void testGreeterGeneration() throws Exception {
        testCodeGeneration("greeter", "greeter");
    }

    @Test
    public void testContractsGeneration() throws Exception {
        testCodeGeneration("contracts", "HumanStandardToken");
    }

    @Test
    public void testSimpleStorageGeneration() throws Exception {
        testCodeGeneration("simplestorage", "SimpleStorage");
    }

    @Test
    public void testFibonacciGeneration() throws Exception {
        testCodeGeneration("fibonacci", "Fibonacci");
    }

    @Test
    public void testArrays() throws Exception {
        testCodeGeneration("arrays", "Arrays");
    }

    @Test
    public void testShipIt() throws Exception {
        testCodeGeneration("shipit", "ShipIt");
    }

    private void testCodeGeneration(String contractName, String inputFileName) throws Exception {
        SolidityFunctionWrapperGenerator.main(Arrays.asList(
                solidityBaseDir + "/" + contractName + "/build/" + inputFileName + ".bin",
                solidityBaseDir + "/" + contractName + "/build/" + inputFileName + ".abi",
                "-p", "org.web3j.unittests",
                "-o", tempDirPath
        ).toArray(new String[0])); // https://shipilev.net/blog/2016/arrays-wisdom-ancients/

        verifyGeneratedCode(tempDirPath + "/org/web3j/unittests/" +
                Strings.capitaliseFirstLetter(inputFileName) + ".java");
    }

    private void verifyGeneratedCode(String sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager =
                     compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(Arrays.asList(sourceFile));
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, diagnostics, null, null, compilationUnits);
            assertTrue("Generated contract contains compile time error", task.call());
        }
    }
}
