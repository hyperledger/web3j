package org.web3j.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.junit.Test;

import org.web3j.TempFileProvider;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.StaticArray10;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.protocol.core.methods.response.AbiDefinition;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.web3j.codegen.SolidityFunctionWrapper.buildEventFunctions;
import static org.web3j.codegen.SolidityFunctionWrapper.buildFunction;
import static org.web3j.codegen.SolidityFunctionWrapper.buildTypeName;
import static org.web3j.codegen.SolidityFunctionWrapper.createValidParamName;


public class SolidityFunctionWrapperTest extends TempFileProvider {

    @Override
    public void setUp() throws Exception {
        super.setUp();
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

        String expected = "public java.util.concurrent.Future<org.web3j.protocol.core.methods"
                + ".response.TransactionReceipt> functionName(org.web3j.abi.datatypes.generated"
                + ".Uint8 param) {\n"
                + "  org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes"
                + ".Function(\"functionName\", java.util.Arrays.<org.web3j.abi.datatypes"
                + ".Type>asList(param), java.util.Collections.<org.web3j.abi"
                + ".TypeReference<?>>emptyList());\n"
                + "  return executeTransactionAsync(function);\n"
                + "}\n";

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

        String expected = "public java.util.concurrent.Future<org.web3j.abi.datatypes.generated"
                + ".Int8> functionName(org.web3j.abi.datatypes.generated.Uint8 param) {\n"
                + "  org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes"
                + ".Function(\"functionName\", \n"
                + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(param), \n"
                + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi"
                + ".TypeReference<org.web3j.abi.datatypes.generated.Int8>() {}));\n"
                + "  return executeCallSingleValueReturnAsync(function);\n"
                + "}\n";

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

        String expected = "public java.util.concurrent.Future<java.util.List<org.web3j.abi"
                + ".datatypes.Type>> functionName(org.web3j.abi.datatypes.generated.Uint8 param1, "
                + "org.web3j.abi.datatypes.generated.Uint32 param2) {\n"
                + "  org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes"
                + ".Function(\"functionName\", \n"
                + "      java.util.Arrays.<org.web3j.abi.datatypes.Type>asList(param1, param2), \n"
                + "      java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j.abi"
                + ".TypeReference<org.web3j.abi.datatypes.generated.Int8>() {}, new org.web3j.abi"
                + ".TypeReference<org.web3j.abi.datatypes.generated.Int32>() {}));\n"
                + "  return executeCallMultipleValueReturnAsync(function);\n"
                + "}\n";

        assertThat(methodSpec.toString(), is(expected));
    }

    @Test
    public void testBuildEventConstantMultipleValueReturn() throws Exception {

        AbiDefinition.NamedType fromAddress = new AbiDefinition.NamedType("from", "address");
        AbiDefinition.NamedType toAddress = new AbiDefinition.NamedType("to", "address");
        AbiDefinition.NamedType value = new AbiDefinition.NamedType("value", "uint256");
        fromAddress.setIndexed(true);
        toAddress.setIndexed(true);

        AbiDefinition functionDefinition = new AbiDefinition(
                false,
                Arrays.asList(fromAddress, toAddress, value),
                "Transfer",
                new ArrayList<>(),
                "event",
                false);
        TypeSpec.Builder builder = TypeSpec.classBuilder("testClass");

        buildEventFunctions(functionDefinition, builder);

        String expected = "class testClass {\n"
                + "  public java.util.List<TransferEventResponse> getTransferEvents(org.web3j"
                + ".protocol.core.methods.response.TransactionReceipt transactionReceipt) {\n"
                + "    final org.web3j.abi.datatypes.Event event = "
                + "new org.web3j.abi.datatypes.Event("
                + "\"Transfer\", \n"
                + "        java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j"
                + ".abi.TypeReference<org.web3j.abi.datatypes.Address>() {}, new org.web3j.abi"
                + ".TypeReference<org.web3j.abi.datatypes.Address>() {}),\n"
                + "        java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j"
                + ".abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}));\n"
                + "    java.util.List<org.web3j.abi.EventValues> valueList = extractEventParameters"
                + "(event, transactionReceipt);\n"
                + "    java.util.ArrayList<TransferEventResponse> responses = new java.util"
                + ".ArrayList<TransferEventResponse>(valueList.size());\n"
                + "    for (org.web3j.abi.EventValues eventValues : valueList) {\n"
                + "      TransferEventResponse typedResponse = new TransferEventResponse();\n"
                + "      typedResponse.from = (org.web3j.abi.datatypes.Address) eventValues"
                + ".getIndexedValues().get(0);\n"
                + "      typedResponse.to = (org.web3j.abi.datatypes.Address) eventValues"
                + ".getIndexedValues().get(1);\n"
                + "      typedResponse.value = (org.web3j.abi.datatypes.generated.Uint256) "
                + "eventValues.getNonIndexedValues().get(0);\n"
                + "      responses.add(typedResponse);\n"
                + "    }\n"
                + "    return responses;\n"
                + "  }\n"
                + "\n"
                + "  public rx.Observable<TransferEventResponse> transferEventObservable("
                + "org.web3j.protocol.core.DefaultBlockParameter startBlock, "
                + "org.web3j.protocol.core.DefaultBlockParameter endBlock) {\n"
                + "    final org.web3j.abi.datatypes.Event event = "
                + "new org.web3j.abi.datatypes.Event(\"Transfer\", \n"
                + "        java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j"
                + ".abi.TypeReference<org.web3j.abi.datatypes.Address>() {}, new org.web3j.abi"
                + ".TypeReference<org.web3j.abi.datatypes.Address>() {}),\n"
                + "        java.util.Arrays.<org.web3j.abi.TypeReference<?>>asList(new org.web3j"
                + ".abi.TypeReference<org.web3j.abi.datatypes.generated.Uint256>() {}));\n"
                + "    org.web3j.protocol.core.methods.request.EthFilter filter = new org.web3j"
                + ".protocol.core.methods.request.EthFilter(startBlock, endBlock, "
                + "getContractAddress());\n"
                + "    filter.addSingleTopic(org.web3j.abi.EventEncoder.encode(event));\n"
                + "    return web3j.ethLogObservable(filter).map(new rx.functions.Func1<org.web3j"
                + ".protocol.core.methods.response.Log, TransferEventResponse>() {\n"
                + "      @java.lang.Override\n"
                + "      public TransferEventResponse call(org.web3j.protocol.core.methods.response"
                + ".Log log) {\n"
                + "        org.web3j.abi.EventValues eventValues = extractEventParameters(event, "
                + "log);\n"
                + "        TransferEventResponse typedResponse = new TransferEventResponse();\n"
                + "        typedResponse.from = (org.web3j.abi.datatypes.Address) eventValues"
                + ".getIndexedValues().get(0);\n"
                + "        typedResponse.to = (org.web3j.abi.datatypes.Address) eventValues"
                + ".getIndexedValues().get(1);\n"
                + "        typedResponse.value = (org.web3j.abi.datatypes.generated.Uint256)"
                + " eventValues.getNonIndexedValues().get(0);\n"
                + "        return typedResponse;\n"
                + "      }\n"
                + "    });\n"
                + "  }\n"
                + "\n"
                + "  public static class TransferEventResponse {\n"
                + "    public org.web3j.abi.datatypes.Address from;\n"
                + "\n"
                + "    public org.web3j.abi.datatypes.Address to;\n"
                + "\n"
                + "    public org.web3j.abi.datatypes.generated.Uint256 value;\n"
                + "  }\n"
                + "}\n";
        assertThat(builder.build().toString(), is(expected));
    }

}
