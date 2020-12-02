/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.codegen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
import org.web3j.protocol.core.methods.response.AbiDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        generationReporter = mock(GenerationReporter.class);
        solidityFunctionWrapper =
                new SolidityFunctionWrapper(
                        true, false, false, Address.DEFAULT_LENGTH, generationReporter);
    }

    @Test
    public void testCreateValidParamName() {
        assertEquals(createValidParamName("param", 1), ("param"));
        assertEquals(createValidParamName("", 1), ("param1"));
    }

    @Test
    public void testBuildTypeName() throws Exception {
        assertEquals(buildTypeName("uint256"), (ClassName.get(Uint256.class)));
        assertEquals(buildTypeName("uint64"), (ClassName.get(Uint64.class)));
        assertEquals(buildTypeName("string"), (ClassName.get(Utf8String.class)));

        assertEquals(
                buildTypeName("uint256[]"),
                (ParameterizedTypeName.get(DynamicArray.class, Uint256.class)));

        assertEquals(
                buildTypeName("uint256[] storage"),
                (ParameterizedTypeName.get(DynamicArray.class, Uint256.class)));

        assertEquals(
                buildTypeName("uint256[] memory"),
                (ParameterizedTypeName.get(DynamicArray.class, Uint256.class)));

        assertEquals(
                buildTypeName("uint256[10]"),
                (ParameterizedTypeName.get(StaticArray10.class, Uint256.class)));

        assertEquals(
                buildTypeName("uint256[33]"),
                (ParameterizedTypeName.get(StaticArray.class, Uint256.class)));

        assertEquals(
                buildTypeName("uint256[10][3]"),
                (ParameterizedTypeName.get(
                        ClassName.get(StaticArray3.class),
                        ParameterizedTypeName.get(StaticArray10.class, Uint256.class))));

        assertEquals(
                buildTypeName("uint256[2][]"),
                (ParameterizedTypeName.get(
                        ClassName.get(DynamicArray.class),
                        ParameterizedTypeName.get(StaticArray2.class, Uint256.class))));

        assertEquals(
                buildTypeName("uint256[33][]"),
                (ParameterizedTypeName.get(
                        ClassName.get(DynamicArray.class),
                        ParameterizedTypeName.get(StaticArray.class, Uint256.class))));

        assertEquals(
                buildTypeName("uint256[][]"),
                (ParameterizedTypeName.get(
                        ClassName.get(DynamicArray.class),
                        ParameterizedTypeName.get(DynamicArray.class, Uint256.class))));
    }

    @Test
    public void testGetNativeType() {
        assertEquals(getNativeType(TypeName.get(Address.class)), (TypeName.get(String.class)));
        assertEquals(getNativeType(TypeName.get(Uint256.class)), (TypeName.get(BigInteger.class)));
        assertEquals(getNativeType(TypeName.get(Int256.class)), (TypeName.get(BigInteger.class)));
        assertEquals(getNativeType(TypeName.get(Utf8String.class)), (TypeName.get(String.class)));
        assertEquals(getNativeType(TypeName.get(Bool.class)), (TypeName.get(Boolean.class)));
        assertEquals(getNativeType(TypeName.get(Bytes32.class)), (TypeName.get(byte[].class)));
        assertEquals(getNativeType(TypeName.get(DynamicBytes.class)), (TypeName.get(byte[].class)));
    }

    @Test
    public void testGetNativeTypeParameterized() {
        assertEquals(
                getNativeType(
                        ParameterizedTypeName.get(
                                ClassName.get(DynamicArray.class), TypeName.get(Address.class))),
                (ParameterizedTypeName.get(ClassName.get(List.class), TypeName.get(String.class))));
    }

    @Test
    public void testGetNativeTypeInvalid() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> getNativeType(TypeName.get(BigInteger.class)));
    }

    @Test
    public void testGetEventNativeType() {
        assertEquals(
                getEventNativeType(TypeName.get(Utf8String.class)), (TypeName.get(byte[].class)));
    }

    @Test
    public void testGetEventNativeTypeParameterized() {
        assertEquals(
                getEventNativeType(
                        ParameterizedTypeName.get(
                                ClassName.get(DynamicArray.class), TypeName.get(Address.class))),
                (TypeName.get(byte[].class)));
    }

    @Test
    public void testBuildFunctionTransaction() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        false,
                        Arrays.asList(new AbiDefinition.NamedType("param", "uint8")),
                        "functionName",
                        Collections.emptyList(),
                        "type",
                        false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        String expected =
                "public org.web3j.protocol.core.RemoteFunctionCall<org.web3j.protocol.core.methods.response.TransactionReceipt> functionName(java.math.BigInteger param) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(\n"
                        + "      FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param)), \n"
                        + "      java.util.Collections.<org.web3j.abi.TypeReference<?>>emptyList());\n"
                        + "  return executeRemoteCallTransaction(function);\n"
                        + "}\n";

        assertEquals(methodSpec.toString(), (expected));
    }

    @Test
    public void testBuildingFunctionTransactionThatReturnsValueReportsWarning() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        false,
                        Arrays.asList(new AbiDefinition.NamedType("param", "uint8")),
                        "functionName",
                        Arrays.asList(new AbiDefinition.NamedType("result", "uint8")),
                        "type",
                        false);

        solidityFunctionWrapper.buildFunction(functionDefinition);

        verify(generationReporter)
                .report(
                        "Definition of the function functionName returns a value but is not defined as a view function. "
                                + "Please ensure it contains the view modifier if you want to read the return value");
    }

    @Test
    public void testBuildPayableFunctionTransaction() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        false,
                        Arrays.asList(new AbiDefinition.NamedType("param", "uint8")),
                        "functionName",
                        Collections.emptyList(),
                        "type",
                        true);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        String expected =
                "public org.web3j.protocol.core.RemoteFunctionCall<org.web3j.protocol.core.methods.response.TransactionReceipt> functionName(java.math.BigInteger param, java.math.BigInteger weiValue) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(\n"
                        + "      FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param)), \n"
                        + "      java.util.Collections.<org.web3j.abi.TypeReference<?>>emptyList());\n"
                        + "  return executeRemoteCallTransaction(function, weiValue);\n"
                        + "}\n";

        assertEquals(methodSpec.toString(), (expected));
    }

    @Test
    public void testBuildFunctionConstantSingleValueReturn() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        true,
                        Arrays.asList(new AbiDefinition.NamedType("param", "uint8")),
                        "functionName",
                        Arrays.asList(new AbiDefinition.NamedType("result", "int8")),
                        "type",
                        false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        String expected =
                "public org.web3j.protocol.core.RemoteFunctionCall<java.math.BigInteger> functionName(java.math.BigInteger param) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param)), \n"
                        + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int8>() {}));\n"
                        + "  return executeRemoteCallSingleValueReturn(function, java.math.BigInteger.class);\n"
                        + "}\n";

        assertEquals(methodSpec.toString(), (expected));
    }

    @Test
    public void testBuildFunctionConstantSingleValueRawListReturn() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        true,
                        Arrays.asList(new AbiDefinition.NamedType("param", "uint8")),
                        "functionName",
                        Arrays.asList(new AbiDefinition.NamedType("result", "address[]")),
                        "type",
                        false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        String expected =
                "public org.web3j.protocol.core.RemoteFunctionCall<java.util.List> functionName(java.math.BigInteger param) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param)), \n"
                        + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>>() {}));\n"
                        + "  return new org.web3j.protocol.core.RemoteFunctionCall<java.util.List>(function,\n"
                        + "      new java.util.concurrent.Callable<java.util.List>() {\n"
                        + "        @java.lang.Override\n"
                        + "        @java.lang.SuppressWarnings(\"unchecked\")\n"
                        + "        public java.util.List call() throws java.lang.Exception {\n"
                        + "          java.util.List<org.web3j.abi.datatypes.Type> result = (java.util.List<org.web3j.abi.datatypes.Type>) executeCallSingleValueReturn(function, java.util.List.class);\n"
                        + "          return convertToNative(result);\n"
                        + "        }\n"
                        + "      });\n"
                        + "}\n";

        assertEquals(methodSpec.toString(), (expected));
    }

    @Test
    public void testBuildFunctionConstantDynamicArrayRawListReturn() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        true,
                        Arrays.asList(new AbiDefinition.NamedType("param", "uint8[]")),
                        "functionName",
                        Arrays.asList(new AbiDefinition.NamedType("result", "address[]")),
                        "type",
                        false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        String expected =
                "public org.web3j.protocol.core.RemoteFunctionCall<java.util.List> functionName(java.util.List<java.math.BigInteger> param) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint8>(\n"
                        + "              org.web3j.abi.datatypes.generated.Uint8.class,\n"
                        + "              org.web3j.abi.Utils.typeMap(param, org.web3j.abi.datatypes.generated.Uint8.class))), \n"
                        + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>>() {}));\n"
                        + "  return new org.web3j.protocol.core.RemoteFunctionCall<java.util.List>(function,\n"
                        + "      new java.util.concurrent.Callable<java.util.List>() {\n"
                        + "        @java.lang.Override\n"
                        + "        @java.lang.SuppressWarnings(\"unchecked\")\n"
                        + "        public java.util.List call() throws java.lang.Exception {\n"
                        + "          java.util.List<org.web3j.abi.datatypes.Type> result = (java.util.List<org.web3j.abi.datatypes.Type>) executeCallSingleValueReturn(function, java.util.List.class);\n"
                        + "          return convertToNative(result);\n"
                        + "        }\n"
                        + "      });\n"
                        + "}\n";

        assertEquals(methodSpec.toString(), (expected));
    }

    @Test
    public void testBuildFunctionConstantMultiDynamicArrayRawListReturn() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        true,
                        Arrays.asList(new AbiDefinition.NamedType("param", "uint8[][]")),
                        "functionName",
                        Arrays.asList(new AbiDefinition.NamedType("result", "address[]")),
                        "type",
                        false);

        MethodSpec methodSpec = solidityFunctionWrapper.buildFunction(functionDefinition);

        String expected =
                "public org.web3j.protocol.core.RemoteFunctionCall<java.util.List> functionName(java.util.List<java.util.List<java.math.BigInteger>> param) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.DynamicArray>(\n"
                        + "              org.web3j.abi.datatypes.DynamicArray.class,\n"
                        + "              org.web3j.abi.Utils.typeMap(param, org.web3j.abi.datatypes.DynamicArray.class,\n"
                        + "      org.web3j.abi.datatypes.generated.Uint8.class))), \n"
                        + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>>() {}));\n"
                        + "  return new org.web3j.protocol.core.RemoteFunctionCall<java.util.List>(function,\n"
                        + "      new java.util.concurrent.Callable<java.util.List>() {\n"
                        + "        @java.lang.Override\n"
                        + "        @java.lang.SuppressWarnings(\"unchecked\")\n"
                        + "        public java.util.List call() throws java.lang.Exception {\n"
                        + "          java.util.List<org.web3j.abi.datatypes.Type> result = (java.util.List<org.web3j.abi.datatypes.Type>) executeCallSingleValueReturn(function, java.util.List.class);\n"
                        + "          return convertToNative(result);\n"
                        + "        }\n"
                        + "      });\n"
                        + "}\n";

        assertEquals(methodSpec.toString(), (expected));
    }

    @Test
    public void testBuildFunctionConstantInvalid() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        true,
                        Collections.singletonList(new AbiDefinition.NamedType("param", "uint8")),
                        "functionName",
                        Collections.emptyList(),
                        "type",
                        false);

        List<MethodSpec> methodSpecs = solidityFunctionWrapper.buildFunctions(functionDefinition);
        assertTrue(methodSpecs.isEmpty());
    }

    @Test
    public void testBuildFunctionConstantMultipleValueReturn() throws Exception {

        AbiDefinition functionDefinition =
                new AbiDefinition(
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

        String expected =
                "public org.web3j.protocol.core.RemoteFunctionCall<org.web3j.tuples.generated.Tuple2<java.math.BigInteger, java.math.BigInteger>> functionName(java.math.BigInteger param1, java.math.BigInteger param2) {\n"
                        + "  final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNCTIONNAME, \n"
                        + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(new org.web3j.abi.datatypes.generated.Uint8(param1), \n"
                        + "      new org.web3j.abi.datatypes.generated.Uint32(param2)), \n"
                        + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int8>() {}, new org.web3j.abi.TypeReference<org.web3j.abi.datatypes.generated.Int32>() {}));\n"
                        + "  return new org.web3j.protocol.core.RemoteFunctionCall<org.web3j.tuples.generated.Tuple2<java.math.BigInteger, java.math.BigInteger>>(function,\n"
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

        assertEquals(methodSpec.toString(), (expected));
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

        AbiDefinition functionDefinition =
                new AbiDefinition(
                        false,
                        Arrays.asList(id, fromAddress, toAddress, value, message),
                        "Transfer",
                        new ArrayList<>(),
                        "event",
                        false);
        TypeSpec.Builder builder = TypeSpec.classBuilder("testClass");

        builder.addMethods(
                solidityFunctionWrapper.buildEventFunctions(functionDefinition, builder));

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
                        + "  public io.reactivex.Flowable<TransferEventResponse> transferEventFlowable(org.web3j.protocol.core.methods.request.EthFilter filter) {\n"
                        + "    return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<org.web3j.protocol.core.methods.response.Log, TransferEventResponse>() {\n"
                        + "      @java.lang.Override\n"
                        + "      public TransferEventResponse apply(org.web3j.protocol.core.methods.response.Log log) {\n"
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
                        + "  public io.reactivex.Flowable<TransferEventResponse> transferEventFlowable(org.web3j.protocol.core.DefaultBlockParameter startBlock, org.web3j.protocol.core.DefaultBlockParameter endBlock) {\n"
                        + "    org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j.protocol.core.methods.request.EthFilter(startBlock, endBlock, getContractAddress());\n"
                        + "    filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(TRANSFER_EVENT));\n"
                        + "    return transferEventFlowable(filter);\n"
                        + "  }\n"
                        + "\n"
                        + "  public static class TransferEventResponse extends org.web3j.protocol.core.methods.response.BaseEventResponse {\n"
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

        assertEquals(builder.build().toString(), (expected));
    }

    @Test
    public void testBuildFuncNameConstants() throws Exception {
        AbiDefinition functionDefinition =
                new AbiDefinition(
                        false,
                        Arrays.asList(new AbiDefinition.NamedType("param", "uint8")),
                        "functionName",
                        Collections.emptyList(),
                        "function",
                        true);
        TypeSpec.Builder builder = TypeSpec.classBuilder("testClass");

        builder.addFields(
                solidityFunctionWrapper.buildFuncNameConstants(
                        Collections.singletonList(functionDefinition)));

        String expected =
                "class testClass {\n"
                        + "  public static final java.lang.String FUNC_FUNCTIONNAME = \"functionName\";\n"
                        + "}\n";

        assertEquals(builder.build().toString(), (expected));
    }
}
