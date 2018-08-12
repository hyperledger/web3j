package org.web3j.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;

import org.web3j.TempFileProvider;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.StaticArray10;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.StaticArray3;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.web3j.codegen.SolidityFunctionWrapper.buildTypeName;
import static org.web3j.codegen.SolidityFunctionWrapper.createValidParamName;
import static org.web3j.codegen.SolidityFunctionWrapper.getEventNativeType;
import static org.web3j.codegen.SolidityFunctionWrapper.getNativeType;


public class SolidityFunctionWrapperTest extends TempFileProvider {

    private SolidityFunctionWrapper solidityFunctionWrapper;

    private GenerationReporter generationReporter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        generationReporter = mock(GenerationReporter.class);
        solidityFunctionWrapper = new SolidityFunctionWrapper(true, generationReporter);
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

        assertThat(buildTypeName("uint256[] storage"),
                is(ParameterizedTypeName.get(DynamicArray.class, Uint256.class)));

        assertThat(buildTypeName("uint256[] memory"),
                is(ParameterizedTypeName.get(DynamicArray.class, Uint256.class)));

        assertThat(buildTypeName("uint256[10]"),
                is(ParameterizedTypeName.get(StaticArray10.class, Uint256.class)));

        assertThat(buildTypeName("uint256[33]"),
                is(ParameterizedTypeName.get(StaticArray.class, Uint256.class)));

        assertThat(buildTypeName("uint256[10][3]"),
                is(ParameterizedTypeName.get(ClassName.get(StaticArray3.class),
                        ParameterizedTypeName.get(StaticArray10.class, Uint256.class))));

        assertThat(buildTypeName("uint256[2][]"),
                is(ParameterizedTypeName.get(ClassName.get(DynamicArray.class),
                        ParameterizedTypeName.get(StaticArray2.class, Uint256.class))));

        assertThat(buildTypeName("uint256[33][]"),
                is(ParameterizedTypeName.get(ClassName.get(DynamicArray.class),
                        ParameterizedTypeName.get(StaticArray.class, Uint256.class))));

        assertThat(buildTypeName("uint256[][]"),
                is(ParameterizedTypeName.get(ClassName.get(DynamicArray.class),
                        ParameterizedTypeName.get(DynamicArray.class, Uint256.class))));
    }

    @Test
    public void testGetNativeType() {
        assertThat(getNativeType(TypeName.get(Address.class)),
                equalTo(TypeName.get(String.class)));
        assertThat(getNativeType(TypeName.get(Uint256.class)),
                equalTo(TypeName.get(BigInteger.class)));
        assertThat(getNativeType(TypeName.get(Int256.class)),
                equalTo(TypeName.get(BigInteger.class)));
        assertThat(getNativeType(TypeName.get(Utf8String.class)),
                equalTo(TypeName.get(String.class)));
        assertThat(getNativeType(TypeName.get(Bool.class)),
                equalTo(TypeName.get(Boolean.class)));
        assertThat(getNativeType(TypeName.get(Bytes32.class)),
                equalTo(TypeName.get(byte[].class)));
        assertThat(getNativeType(TypeName.get(DynamicBytes.class)),
                equalTo(TypeName.get(byte[].class)));
    }

    @Test
    public void testGetNativeTypeParameterized() {
        assertThat(getNativeType(
                ParameterizedTypeName.get(
                        ClassName.get(DynamicArray.class), TypeName.get(Address.class))),
                equalTo(ParameterizedTypeName.get(
                        ClassName.get(List.class), TypeName.get(String.class))));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetNativeTypeInvalid() {
        getNativeType(TypeName.get(BigInteger.class));
    }

    @Test
    public void testGetEventNativeType() {
        assertThat(getEventNativeType(TypeName.get(Utf8String.class)),
                equalTo(TypeName.get(byte[].class)));
    }

    @Test
    public void testGetEventNativeTypeParameterized() {
        assertThat(getEventNativeType(
                ParameterizedTypeName.get(
                        ClassName.get(DynamicArray.class), TypeName.get(Address.class))),
                equalTo(TypeName.get(byte[].class)));
    }

    @Test
    public void testBuildFunctionTransaction() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                false,
                Collections.singletonList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.emptyList(),
                "type",
                false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        //CHECKSTYLE:OFF
        String expected =
                "public org.web3j.protocol.core.RemoteCall<org.web3j.protocol.core.methods.response.TransactionReceipt> functionName(java.math.BigInteger param) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(\n"
                        + "      FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param)), \n"
                        + "      java.util.Collections.<org.web3j.abi.TypeReference<?>>emptyList());\n"
                        + "  return executeRemoteCallTransaction(function);\n"
                        + "}\n";
        //CHECKSTYLE:ON

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test
    public void testBuildingFunctionTransactionThatReturnsValueReportsWarning() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                false,
                Collections.singletonList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.singletonList(
                        new AbiDefinition.NamedType("result", "uint8")),
                "type",
                false);

        solidityFunctionWrapper.buildFunction(functionDefinition);

        //CHECKSTYLE:OFF
        verify(generationReporter).report(
                "Definition of the function functionName returns a value but is not defined as a view function. " +
                        "Please ensure it contains the view modifier if you want to read the return value");
        //CHECKSTYLE:ON
    }

    @Test
    public void testBuildPayableFunctionTransaction() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                false,
                Collections.singletonList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.emptyList(),
                "type",
                true);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        //CHECKSTYLE:OFF
        String expected =
                "public org.web3j.protocol.core.RemoteCall<org.web3j.protocol.core.methods.response.TransactionReceipt> functionName(java.math.BigInteger param, java.math.BigInteger weiValue) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(\n"
                        + "      FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param)), \n"
                        + "      java.util.Collections.<org.web3j.abi.TypeReference<?>>emptyList());\n"
                        + "  return executeRemoteCallTransaction(function, weiValue);\n"
                        + "}\n";
        //CHECKSTYLE:ON

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test
    public void testBuildFunctionConstantSingleValueReturn() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                true,
                Collections.singletonList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.singletonList(
                        new AbiDefinition.NamedType("result", "int8")),
                "type",
                false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        //CHECKSTYLE:OFF
        String expected =
                "public org.web3j.protocol.core.RemoteCall<java.math.BigInteger> functionName(java.math.BigInteger param) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param)), \n"
                        + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int8>() {}));\n"
                        + "  return executeRemoteCallSingleValueReturn(function, java.math.BigInteger.class);\n"
                        + "}\n";
        //CHECKSTYLE:ON

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test
    public void testBuildFunctionConstantSingleValueRawListReturn() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                true,
                Collections.singletonList(
                    new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.singletonList(
                        new AbiDefinition.NamedType("result", "address[]")),
                "type",
                false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        //CHECKSTYLE:OFF
        String expected =
                "public org.web3j.protocol.core.RemoteCall<java.util.List> functionName(java.math.BigInteger param) {\n"
                + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNCTIONNAME, \n"
                + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param)), \n"
                + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>>() {}));\n"
                + "  return new org.web3j.protocol.core.RemoteCall<java.util.List>(\n"
                + "      new java.util.concurrent.Callable<java.util.List>() {\n"
                + "        @java.lang.Override\n"
                + "        @java.lang.SuppressWarnings(\"unchecked\")\n"
                + "        public java.util.List call() throws java.lang.Exception {\n"
                + "          java.util.List<org.web3j.abi.datatypes.Type> result = (java.util.List<org.web3j.abi.datatypes.Type>) executeCallSingleValueReturn(function, java.util.List.class);\n"
                + "          return convertToNative(result);\n"
                + "        }\n"
                + "      });\n"
                + "}\n";
        //CHECKSTYLE:ON

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test
    public void testBuildFunctionConstantInvalid() throws Exception {
        AbiDefinition functionDefinition = new AbiDefinition(
                true,
                Collections.singletonList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.emptyList(),
                "type",
                false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        //CHECKSTYLE:OFF
        String expected =
                "public void functionName(java.math.BigInteger param) {\n"
                + "  throw new RuntimeException(\"cannot call constant function with void return type\");\n"
                + "}\n";
        //CHECKSTYLE:ON

        assertThat(methodSpec.toString(), is(expected));
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

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        //CHECKSTYLE:OFF
        String expected = "public org.web3j.protocol.core.RemoteCall<org.web3j.tuples.generated.Tuple2<java.math.BigInteger, java.math.BigInteger>> functionName(java.math.BigInteger param1, java.math.BigInteger param2) {\n"
                + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNCTIONNAME, \n"
                + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param1), \n"
                + "      new org.web3j.abi.datatypes.generated.Uint32(param2)), \n"
                + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int8>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int32>() {}));\n"
                + "  return new org.web3j.protocol.core.RemoteCall<org.web3j.tuples.generated.Tuple2<java.math.BigInteger, java.math.BigInteger>>(\n"
                + "      new java.util.concurrent.Callable<org.web3j.tuples.generated.Tuple2<java.math.BigInteger, java.math.BigInteger>>() {\n"
                + "        @java.lang.Override\n"
                + "        public org.web3j.tuples.generated.Tuple2<java.math.BigInteger, java.math.BigInteger> call() throws java.lang.Exception {\n"
                + "          java.util.List<org.web3j.abi.datatypes.Type> results = executeCallMultipleValueReturn(function);\n"
                + "          return new org.web3j.tuples.generated.Tuple2<java.math.BigInteger, java.math.BigInteger>(\n"
                + "              (java.math.BigInteger) results.get(0).getValue(), \n"
                + "              (java.math.BigInteger) results.get(1).getValue());\n"
                + "        }\n"
                + "      });\n"
                + "}\n";
        //CHECKSTYLE:ON

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test
    public void testBuildEventConstantMultipleValueReturn() throws Exception {

        AbiDefinition.NamedType id = new AbiDefinition.NamedType("id", "string", true);
        AbiDefinition.NamedType fromAddress = new AbiDefinition.NamedType("from", "address");
        AbiDefinition.NamedType toAddress = new AbiDefinition.NamedType("to", "address");
        AbiDefinition.NamedType value = new AbiDefinition.NamedType("value", "uint256");
        AbiDefinition.NamedType message = new AbiDefinition.NamedType("message", "string");
        fromAddress.setIndexed(true);
        toAddress.setIndexed(true);

        AbiDefinition functionDefinition = new AbiDefinition(
                false,
                Arrays.asList(id, fromAddress, toAddress, value, message),
                "Transfer",
                new ArrayList<>(),
                "event",
                false);
        TypeSpec.Builder builder = TypeSpec.classBuilder("testClass");

        builder.addMethods(
                solidityFunctionWrapper.buildEventFunctions(functionDefinition, builder));

        //CHECKSTYLE:OFF
        String expected =
                "class testClass {\n"
                        + "  public static final org.web3j.abi.datatypes.Event TRANSFER_EVENT = new org.web3j.abi.datatypes.Event(\"Transfer\", \n"
                        + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Utf8String>(true) {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>(true) {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Address>(true) {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.Utf8String>() {}));\n  ;\n\n"
                        + "  public java.util.List<TransferEventResponse> getTransferEvents(org.web3j.protocol.core.methods.response.TransactionReceipt transactionReceipt) {\n"
                        + "    java.util.List<org.web3j.tx.Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);\n"
                        + "    java.util.ArrayList<TransferEventResponse> responses = new java.util.ArrayList<TransferEventResponse>(valueList.size());\n"
                        + "    for (org.web3j.tx.Contract.EventValuesWithLog eventValues : valueList) {\n"
                        + "      TransferEventResponse typedResponse = new TransferEventResponse();\n"
                        + "      typedResponse.log = eventValues.getLog();\n"
                        + "      typedResponse.id = (byte[]) eventValues.getIndexedValues().get(0).getValue();\n"
                        + "      typedResponse.from = (java.lang.String) eventValues.getIndexedValues().get(1).getValue();\n"
                        + "      typedResponse.to = (java.lang.String) eventValues.getIndexedValues().get(2).getValue();\n"
                        + "      typedResponse.value = (java.math.BigInteger) eventValues.getNonIndexedValues().get(0).getValue();\n"
                        + "      typedResponse.message = (java.lang.String) eventValues.getNonIndexedValues().get(1).getValue();\n"
                        + "      responses.add(typedResponse);\n"
                        + "    }\n"
                        + "    return responses;\n"
                        + "  }\n"
                        + "\n"
                        + "  public rx.Observable<TransferEventResponse> transferEventObservable(org.web3j.protocol.core.methods.request.EthFilter filter) {\n"
                        + "    return web3j.ethLogObservable(filter).map(new rx.functions.Func1<org.web3j.protocol.core.methods.response.Log, TransferEventResponse>() {\n"
                        + "      @java.lang.Override\n"
                        + "      public TransferEventResponse call(org.web3j.protocol.core.methods.response.Log log) {\n"
                        + "        org.web3j.tx.Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);\n"
                        + "        TransferEventResponse typedResponse = new TransferEventResponse();\n"
                        + "        typedResponse.log = log;\n"
                        + "        typedResponse.id = (byte[]) eventValues.getIndexedValues().get(0).getValue();\n"
                        + "        typedResponse.from = (java.lang.String) eventValues.getIndexedValues().get(1).getValue();\n"
                        + "        typedResponse.to = (java.lang.String) eventValues.getIndexedValues().get(2).getValue();\n"
                        + "        typedResponse.value = (java.math.BigInteger) eventValues.getNonIndexedValues().get(0).getValue();\n"
                        + "        typedResponse.message = (java.lang.String) eventValues.getNonIndexedValues().get(1).getValue();\n"
                        + "        return typedResponse;\n"
                        + "      }\n"
                        + "    });\n"
                        + "  }\n"
                        + "\n"
                        + "  public rx.Observable<TransferEventResponse> transferEventObservable(org.web3j.protocol.core.DefaultBlockParameter startBlock, org.web3j.protocol.core.DefaultBlockParameter endBlock) {\n"
                        + "    org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, getContractAddress());\n"
                        + "    filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(TRANSFER_EVENT));\n"
                        + "    return transferEventObservable(filter);\n"
                        + "  }\n"
                        + "\n"
                        + "  public static class TransferEventResponse {\n"
                        + "    public org.web3j.protocol.core.methods.response.Log log;\n"
                        + "\n"
                        + "    public byte[] id;\n"
                        + "\n"
                        + "    public java.lang.String from;\n"
                        + "\n"
                        + "    public java.lang.String to;\n"
                        + "\n"
                        + "    public java.math.BigInteger value;\n"
                        + "\n"
                        + "    public java.lang.String message;\n"
                        + "  }\n"
                        + "}\n";
        //CHECKSTYLE:ON

        assertThat(builder.build().toString(), is(expected));
    }

    @Test
    public void testBuildFuncNameConstants() {
        AbiDefinition functionDefinition = new AbiDefinition(
                false,
                Collections.singletonList(
                        new AbiDefinition.NamedType("param", "uint8")),
                "functionName",
                Collections.emptyList(),
                "function",
                true);
        TypeSpec.Builder builder = TypeSpec.classBuilder("testClass");

        builder.addFields(solidityFunctionWrapper
                .buildFuncNameConstants(Collections.singletonList(functionDefinition)));


        //CHECKSTYLE:OFF
        String expected =
                "class testClass {\n" +
                        "  public static final java.lang.String FUNC_FUNCTIONNAME = \"functionName\";\n" +
                        "}\n";
        //CHECKSTYLE:ON


        assertThat(builder.build().toString(), is(expected));
    }

    @Test
    public void testGenerateJavaFiles() throws IOException, ClassNotFoundException {
        String contractName = "HumanStandardToken";
        String abiPath = readPathFromClasspath("solidity/contracts/build/HumanStandardToken.abi");
        String binPath = readPathFromClasspath("solidity/contracts/build/HumanStandardToken.bin");
        String destinationDir = createTempDirectory(
                getClass().getSimpleName() + "-testGenerateJavaFiles");
        String basePackageName = "test";
        solidityFunctionWrapper.generateJavaFiles(
                contractName,
                binPath,
                abiPath,
                destinationDir,
                basePackageName
        );

        File generatedJavaFile = new File(
                new File(destinationDir, basePackageName),
                contractName + ".java");

        assertTrue("Should have created a HumanStandardToken.java in "
                + destinationDir + "/" + basePackageName,
                generatedJavaFile.exists());

        System.out.println("Generated java file is in " + generatedJavaFile);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, generatedJavaFile.getPath());

        URLClassLoader classLoader = URLClassLoader.newInstance(
                new URL[] { new File(destinationDir).toURI().toURL() });
        Class<?> generatedClass = Class.forName(
                basePackageName + "." + contractName, true, classLoader);

        assertProtectedDeclaredConstructor(
                generatedClass,
                new Class[]{
                        String.class,
                        Web3j.class,
                        Credentials.class,
                        BigInteger.class,
                        BigInteger.class
                });
        assertProtectedDeclaredConstructor(
                generatedClass,
                new Class[]{
                        String.class,
                        Web3j.class,
                        TransactionManager.class,
                        BigInteger.class,
                        BigInteger.class
                });
        assertProtectedDeclaredConstructor(
                generatedClass,
                new Class[]{
                        String.class,
                        Web3j.class,
                        Credentials.class,
                        ContractGasProvider.class
                });
        assertProtectedDeclaredConstructor(
                generatedClass,
                new Class[]{
                        String.class,
                        Web3j.class,
                        TransactionManager.class,
                        ContractGasProvider.class
                });
    }

    private String createTempDirectory(String destination) throws IOException {
        String testClasspathFile = getTestClasspathFile();
        Path destinationPath = Paths.get(testClasspathFile, destination);

        if (!Files.exists(destinationPath)) {
            Files.createDirectory(destinationPath);
        }

        Files.walk(destinationPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(file -> {
                    @SuppressWarnings("unused") boolean r = file.delete();
                });

        return destinationPath.toString();
    }

    private static void assertProtectedDeclaredConstructor(
            Class<?> targetClass, Class[] parameters) {
        Constructor<?> constructor = assertDeclaredConstructor(targetClass, parameters);
        assertTrue(constructor.getName() + " should have been protected",
                Modifier.isProtected(constructor.getModifiers()));
    }

    private static Constructor<?> assertDeclaredConstructor(
            Class<?> targetClass, Class[] parameters) {
        try {
            return targetClass.getDeclaredConstructor(parameters);
        } catch (NoSuchMethodException e) {
            String parameterList = Stream.of(parameters)
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", "));
            String constructorName = targetClass.getSimpleName() + "(" + parameterList + ")";
            throw new AssertionError(
                "Should have generated a constructor `" + constructorName + "`");
        }
    }

    private String getTestClasspathFile() throws IOException {
        Enumeration<URL> candidates =
                Thread.currentThread().getContextClassLoader().getResources("marker.txt");
        while (candidates.hasMoreElements()) {
            URL candidate = candidates.nextElement();
            if ("file".equalsIgnoreCase(candidate.getProtocol())) {
                return new File(candidate.getFile()).getParentFile().getAbsolutePath();
            }
        }
        throw new RuntimeException("Unable to find test classpath. Unable to find marker.txt");
    }

    private String readPathFromClasspath(String classpath) throws IOException {
        try (InputStream fileStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(classpath)) {
            if (fileStream == null) {
                throw new FileNotFoundException(
                        "Unable to find " + classpath + " from any of the classpaths");
            }
            byte[] allBytes = new byte[fileStream.available()];
            @SuppressWarnings("unused") int read = fileStream.read(allBytes);
            return new String(allBytes);
        }
    }
}
