/*
 * Copyright 2020 Web3 Labs Ltd.
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
package org.web3j.codegen.unit.wrapper.generators;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Callable;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.*;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.codegen.GenerationReporter;
import org.web3j.codegen.SolidityFunctionWrapper;
import org.web3j.codegen.unit.wrapper.Utils;
import org.web3j.codegen.unit.wrapper.WrapperConfig;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Collection;

import static org.web3j.codegen.unit.wrapper.Constants.*;

public class FunctionGenerator {
    private final GenerationReporter reporter;
    private final WrapperConfig config;

    public FunctionGenerator(GenerationReporter reporter, WrapperConfig config) {
        this.reporter = reporter;
        this.config = config;
    }

    public List<MethodSpec> buildFunctionDefinitions(
            String className,
            TypeSpec.Builder classBuilder,
            List<AbiDefinition> functionDefinitions)
            throws ClassNotFoundException {

        Set<String> duplicateFunctionNames = Utils.getDupeFuncNames(functionDefinitions);
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals(TYPE_FUNCTION)) {
                String functionName = Utils.funcNameToConstant(functionDefinition.getName(), true);
                boolean useUpperCase = !duplicateFunctionNames.contains(functionName);
                methodSpecs.addAll(buildFunctions(functionDefinition, useUpperCase));
            }
        }

        return methodSpecs;
    }

    MethodSpec buildFunction(AbiDefinition functionDefinition) throws ClassNotFoundException {
        return buildFunction(functionDefinition, true);
    }

    MethodSpec buildFunction(AbiDefinition functionDefinition, boolean useUpperCase)
            throws ClassNotFoundException {
        return buildFunctions(functionDefinition, useUpperCase).get(0);
    }

    List<MethodSpec> buildFunctions(AbiDefinition functionDefinition)
            throws ClassNotFoundException {
        return buildFunctions(functionDefinition, true);
    }

    List<MethodSpec> buildFunctions(AbiDefinition functionDefinition, boolean useUpperCase)
            throws ClassNotFoundException {

        List<MethodSpec> results = new ArrayList<>(2);
        String functionName = functionDefinition.getName();

        String stateMutability = functionDefinition.getStateMutability();
        boolean pureOrView = "pure".equals(stateMutability) || "view".equals(stateMutability);
        boolean isFunctionDefinitionConstant = functionDefinition.isConstant() || pureOrView;

        if (config.generateSendTxForCalls) {
            final String funcNamePrefix;
            if (isFunctionDefinitionConstant) {
                funcNamePrefix = "call";
            } else {
                funcNamePrefix = "send";
            }
            // Prefix function name to avoid naming collision
            functionName = funcNamePrefix + "_" + functionName;
        } else {
            // If the solidity function name is a reserved word
            // in the current java version prepend it with "_"
            if (!SourceVersion.isName(functionName)) {
                functionName = "_" + functionName;
            }
        }

        MethodSpec.Builder methodBuilder =
                MethodSpec.methodBuilder(functionName).addModifiers(Modifier.PUBLIC);

        final String inputParams =
                addParameters(methodBuilder, functionDefinition.getInputs(), config);
        final List<TypeName> outputParameterTypes =
                SolidityFunctionWrapper.buildTypeNames(
                        functionDefinition.getOutputs(), config.useJavaPrimitiveTypes);

        if (isFunctionDefinitionConstant) {
            // Avoid generating runtime exception call
            if (functionDefinition.hasOutputs()) {
                buildConstantFunction(
                        functionDefinition,
                        methodBuilder,
                        outputParameterTypes,
                        inputParams,
                        useUpperCase);

                results.add(methodBuilder.build());
            }
            if (config.generateSendTxForCalls) {
                AbiDefinition sendFuncDefinition = new AbiDefinition(functionDefinition);
                sendFuncDefinition.setConstant(false);
                results.addAll(buildFunctions(sendFuncDefinition));
            }
        }

        if (!isFunctionDefinitionConstant) {
            buildTransactionFunction(functionDefinition, methodBuilder, inputParams, useUpperCase);
            results.add(methodBuilder.build());
        }

        return results;
    }

    private void buildConstantFunction(
            AbiDefinition functionDefinition,
            MethodSpec.Builder methodBuilder,
            List<TypeName> outputParameterTypes,
            String inputParams,
            boolean useUpperCase)
            throws ClassNotFoundException {

        String functionName = functionDefinition.getName();

        if (outputParameterTypes.isEmpty()) {
            methodBuilder.addStatement(
                    "throw new RuntimeException"
                            + "(\"cannot call constant function with void return type\")");
        } else if (outputParameterTypes.size() == 1) {

            TypeName typeName = outputParameterTypes.get(0);
            TypeName nativeReturnTypeName;
            if (config.useNativeJavaTypes) {
                nativeReturnTypeName = getWrapperRawType(typeName);
            } else {
                nativeReturnTypeName = getWrapperTypeOriginal(typeName);
            }
            methodBuilder.returns(buildRemoteFunctionCall(nativeReturnTypeName));

            methodBuilder.addStatement(
                    "final $T function = "
                            + "new $T($N, \n$T.<$T>asList($L), "
                            + "\n$T.<$T<?>>asList(new $T<$T>() {}))",
                    Function.class,
                    Function.class,
                    Utils.funcNameToConstant(functionName, useUpperCase),
                    Arrays.class,
                    Type.class,
                    inputParams,
                    Arrays.class,
                    TypeReference.class,
                    TypeReference.class,
                    typeName);

            if (config.useNativeJavaTypes) {
                if (nativeReturnTypeName.equals(ClassName.get(List.class))) {
                    // We return list. So all the list elements should
                    // also be converted to native types
                    TypeName listType = ParameterizedTypeName.get(List.class, Type.class);

                    CodeBlock.Builder callCode = CodeBlock.builder();
                    callCode.addStatement(
                            "$T result = "
                                    + "($T) executeCallSingleValueReturn(function, $T.class)",
                            listType,
                            listType,
                            nativeReturnTypeName);
                    callCode.addStatement("return convertToNative(result)");

                    TypeSpec callableType =
                            TypeSpec.anonymousClassBuilder("")
                                    .addSuperinterface(
                                            ParameterizedTypeName.get(
                                                    ClassName.get(Callable.class),
                                                    nativeReturnTypeName))
                                    .addMethod(
                                            MethodSpec.methodBuilder("call")
                                                    .addAnnotation(Override.class)
                                                    .addAnnotation(
                                                            AnnotationSpec.builder(
                                                                            SuppressWarnings.class)
                                                                    .addMember(
                                                                            "value",
                                                                            "$S",
                                                                            "unchecked")
                                                                    .build())
                                                    .addModifiers(Modifier.PUBLIC)
                                                    .addException(Exception.class)
                                                    .returns(nativeReturnTypeName)
                                                    .addCode(callCode.build())
                                                    .build())
                                    .build();

                    methodBuilder.addStatement(
                            "return new $T(function,\n$L)",
                            buildRemoteFunctionCall(nativeReturnTypeName),
                            callableType);
                } else {
                    methodBuilder.addStatement(
                            "return executeRemoteCallSingleValueReturn(function, $T.class)",
                            nativeReturnTypeName);
                }
            } else {
                methodBuilder.addStatement("return executeRemoteCallSingleValueReturn(function)");
            }
        } else {
            List<TypeName> returnTypes =
                    buildReturnTypes(outputParameterTypes, functionDefinition.getOutputs());

            ParameterizedTypeName parameterizedTupleType =
                    ParameterizedTypeName.get(
                            ClassName.get(
                                    "org.web3j.tuples.generated", "Tuple" + returnTypes.size()),
                            returnTypes.toArray(new TypeName[0]));

            methodBuilder.returns(buildRemoteFunctionCall(parameterizedTupleType));

            buildVariableLengthReturnFunctionConstructor(
                    methodBuilder, functionName, inputParams, outputParameterTypes, useUpperCase);

            buildTupleResultContainer(methodBuilder, parameterizedTupleType, outputParameterTypes);
        }
    }

    private List<TypeName> buildReturnTypes(
            List<TypeName> outputParameterTypes, List<AbiDefinition.NamedType> outputNamedTypes) {
        List<TypeName> result = new ArrayList<>(outputParameterTypes.size());
        for (int i = 0; i < outputParameterTypes.size(); i++) {
            TypeName typeName = outputParameterTypes.get(i);
            final AbiDefinition.NamedType abiNamedType = outputNamedTypes.get(i); // tuple (foo)
            result.add(
                    SolidityFunctionWrapper.getWrapperType(
                            typeName, abiNamedType.getComponents(), config.useNativeJavaTypes));
        }
        return result;
    }

    private static ParameterizedTypeName buildRemoteFunctionCall(TypeName typeName) {
        return ParameterizedTypeName.get(ClassName.get(RemoteFunctionCall.class), typeName);
    }

    private void buildTransactionFunction(
            AbiDefinition functionDefinition,
            MethodSpec.Builder methodBuilder,
            String inputParams,
            boolean useUpperCase)
            throws ClassNotFoundException {

        if (functionDefinition.hasOutputs()) {
            reporter.report(
                    String.format(
                            "Definition of the function %s returns a value but is not defined as a view function. "
                                    + "Please ensure it contains the view modifier if you want to read the return value",
                            functionDefinition.getName()));
        }

        if (functionDefinition.isPayable()) {
            methodBuilder.addParameter(BigInteger.class, WEI_VALUE);
        }

        String functionName = functionDefinition.getName();

        methodBuilder.returns(buildRemoteFunctionCall(TypeName.get(TransactionReceipt.class)));

        methodBuilder.addStatement(
                "final $T function = new $T(\n$N, \n$T.<$T>asList($L), \n$T"
                        + ".<$T<?>>emptyList())",
                Function.class,
                Function.class,
                Utils.funcNameToConstant(functionName, useUpperCase),
                Arrays.class,
                Type.class,
                inputParams,
                Collections.class,
                TypeReference.class);
        if (functionDefinition.isPayable()) {
            methodBuilder.addStatement(
                    "return executeRemoteCallTransaction(function, $N)", WEI_VALUE);
        } else {
            methodBuilder.addStatement("return executeRemoteCallTransaction(function)");
        }
    }

    public static String addParameters(
            MethodSpec.Builder methodBuilder,
            List<AbiDefinition.NamedType> namedTypes,
            WrapperConfig config)
            throws ClassNotFoundException {

        /*        final List<ParameterSpec> inputParameterTypes =
                buildParameterTypes(namedTypes, useJavaPrimitiveTypes);

        final List<ParameterSpec> nativeInputParameterTypes =
                new ArrayList<>(inputParameterTypes.size());

        for (ParameterSpec parameterSpec : inputParameterTypes) {
            TypeName typeName = getWrapperType(parameterSpec.type);
            nativeInputParameterTypes.add(
                    ParameterSpec.builder(typeName, parameterSpec.name).build());
        }

        methodBuilder.addParameters(nativeInputParameterTypes);*/

        final int numNamedType = namedTypes.size();
        final List<ParameterSpec> inputParameterTypes = buildParameterTypes(namedTypes, false);
        final List<ParameterSpec> nativeInputParameterTypes =
                new ArrayList<>(inputParameterTypes.size());

        for (int i = 0; i < numNamedType; i++) {
            final ParameterSpec parameterSpec = inputParameterTypes.get(i);
            // TODO: re-instate this!
            //            final TypeName typeName = getWrapperType(parameterSpec,
            // namedTypes.get(i).getComponents());
            TypeName typeName =
                    SolidityFunctionWrapper.getWrapperType(
                            parameterSpec.type,
                            namedTypes.get(i).getComponents(),
                            config.useNativeJavaTypes);

            if (((ClassName) parameterSpec.type).simpleName().contains("TupleClass")) {
                // TODO: do this in a better way!
                final AbiDefinition.NamedType inputNamedType = namedTypes.get(i);
                // Re-assign typeName to TupleClass<N>.
                // TODO: sort out package name as parameter.
                final String todoPkgName = "";
                final String tupleClassName =
                        TupleGenerator.buildClassNameForTupleComponents(
                                inputNamedType.getComponents());
                typeName = ClassName.get(todoPkgName, "ComplexStorage." + tupleClassName);
            } else {
                System.out.println("HERE");
                //                typeName = getWrapperType(parameterSpec.type);
            }

            nativeInputParameterTypes.add(
                    ParameterSpec.builder(typeName, parameterSpec.name).build());
        }

        methodBuilder.addParameters(nativeInputParameterTypes);

        if (config.useNativeJavaTypes) {
            return org.web3j.utils.Collection.join(
                    inputParameterTypes,
                    ", \n",
                    // this results in fully qualified names being generated
                    parameterSpec -> createMappedParameterTypes(parameterSpec, config));
        } else {
            return Collection.join(inputParameterTypes, ", ", parameterSpec -> parameterSpec.name);
        }
    }

    private static String createMappedParameterTypes(
            ParameterSpec parameterSpec, WrapperConfig config) {
        if (parameterSpec.type instanceof ParameterizedTypeName) {
            List<TypeName> typeNames = ((ParameterizedTypeName) parameterSpec.type).typeArguments;
            if (typeNames.size() != 1) {
                throw new UnsupportedOperationException(
                        "Only a single parameterized type is supported");
            } else {
                String parameterSpecType = parameterSpec.type.toString();
                TypeName typeName = typeNames.get(0);
                String typeMapInput = typeName + ".class";
                String componentType = typeName.toString();
                if (typeName instanceof ParameterizedTypeName) {
                    List<TypeName> typeArguments = ((ParameterizedTypeName) typeName).typeArguments;
                    if (typeArguments.size() != 1) {
                        throw new UnsupportedOperationException(
                                "Only a single parameterized type is supported");
                    }
                    TypeName innerTypeName = typeArguments.get(0);
                    componentType = ((ParameterizedTypeName) typeName).rawType.toString();
                    parameterSpecType =
                            ((ParameterizedTypeName) parameterSpec.type).rawType
                                    + "<"
                                    + componentType
                                    + ">";
                    typeMapInput = componentType + ".class,\n" + innerTypeName + ".class";
                }
                return "new "
                        + parameterSpecType
                        + "(\n"
                        + "        "
                        + componentType
                        + ".class,\n"
                        + "        org.web3j.abi.Utils.typeMap("
                        + parameterSpec.name
                        + ", "
                        + typeMapInput
                        + "))";
            }
        } else {
            String constructor = "new " + parameterSpec.type + "(";
            if (Address.class.getCanonicalName().equals(parameterSpec.type.toString())
                    && config.addressLength != Address.DEFAULT_LENGTH) {

                constructor += (config.addressLength * java.lang.Byte.SIZE) + ", ";
            }
            return constructor + parameterSpec.name + ")";
        }
    }

    private void buildTupleResultContainer(
            MethodSpec.Builder methodBuilder,
            ParameterizedTypeName tupleType,
            List<TypeName> outputParameterTypes)
            throws ClassNotFoundException {

        List<TypeName> typeArguments = tupleType.typeArguments;

        CodeBlock.Builder tupleConstructor = CodeBlock.builder();
        tupleConstructor
                .addStatement(
                        "$T results = executeCallMultipleValueReturn(function)",
                        ParameterizedTypeName.get(List.class, Type.class))
                .add("return new $T(", tupleType)
                .add("$>$>");

        String resultStringSimple = "\n($T) results.get($L)";
        if (config.useNativeJavaTypes) {
            resultStringSimple += ".getValue()";
        }

        String resultStringNativeList = "\nconvertToNative(($T) results.get($L).getValue())";

        int size = typeArguments.size();
        ClassName classList = ClassName.get(List.class);

        for (int i = 0; i < size; i++) {
            TypeName param = outputParameterTypes.get(i);
            TypeName convertTo = typeArguments.get(i);

            String resultString = resultStringSimple;

            // If we use native java types we need to convert
            // elements of arrays to native java types too
            if (config.useNativeJavaTypes && param instanceof ParameterizedTypeName) {
                ParameterizedTypeName oldContainer = (ParameterizedTypeName) param;
                ParameterizedTypeName newContainer = (ParameterizedTypeName) convertTo;
                if (newContainer.rawType.compareTo(classList) == 0
                        && newContainer.typeArguments.size() == 1) {
                    convertTo =
                            ParameterizedTypeName.get(classList, oldContainer.typeArguments.get(0));
                    resultString = resultStringNativeList;
                }
            }

            tupleConstructor.add(resultString, convertTo, i);
            tupleConstructor.add(i < size - 1 ? ", " : ");\n");
        }
        tupleConstructor.add("$<$<");

        TypeSpec callableType =
                TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(
                                ParameterizedTypeName.get(ClassName.get(Callable.class), tupleType))
                        .addMethod(
                                MethodSpec.methodBuilder("call")
                                        .addAnnotation(Override.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addException(Exception.class)
                                        .returns(tupleType)
                                        .addCode(tupleConstructor.build())
                                        .build())
                        .build();

        methodBuilder.addStatement(
                "return new $T(function,\n$L)", buildRemoteFunctionCall(tupleType), callableType);
    }

    private static void buildVariableLengthReturnFunctionConstructor(
            MethodSpec.Builder methodBuilder,
            String functionName,
            String inputParameters,
            List<TypeName> outputParameterTypes,
            boolean useUpperCase) {

        List<Object> objects = new ArrayList<>();
        objects.add(Function.class);
        objects.add(Function.class);
        objects.add(Utils.funcNameToConstant(functionName, useUpperCase));

        objects.add(Arrays.class);
        objects.add(Type.class);
        objects.add(inputParameters);

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (TypeName outputParameterType : outputParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(outputParameterType);
        }

        String asListParams =
                Collection.join(outputParameterTypes, ", ", typeName -> "new $T<$T>() {}");

        methodBuilder.addStatement(
                "final $T function = new $T($N, \n$T.<$T>asList($L), \n$T"
                        + ".<$T<?>>asList("
                        + asListParams
                        + "))",
                objects.toArray());
    }

    static List<ParameterSpec> buildParameterTypes(
            List<AbiDefinition.NamedType> namedTypes, boolean primitives)
            throws ClassNotFoundException {

        List<ParameterSpec> result = new ArrayList<>(namedTypes.size());
        for (int i = 0; i < namedTypes.size(); i++) {
            AbiDefinition.NamedType namedType = namedTypes.get(i);

            String name = createValidParamName(namedType.getName(), i);
            String type = namedTypes.get(i).getType();
            final List<AbiDefinition.NamedTypeComponent> components =
                    namedTypes.get(i).getComponents();

            result.add(
                    ParameterSpec.builder(
                                    SolidityFunctionWrapper.buildTypeName(
                                            type, components, primitives),
                                    name)
                            .build());
        }
        return result;
    }

    /**
     * Public Solidity arrays and maps require an unnamed input parameter - multiple if they require
     * a struct type.
     *
     * @param name parameter name
     * @param idx parameter index
     * @return non-empty parameter name
     */
    public static String createValidParamName(String name, int idx) {
        if (name == null || name.equals("")) {
            return "param" + idx;
        } else {
            return name;
        }
    }

    private TypeName getWrapperTypeOriginal(TypeName typeName) {
        // TODO: re-instate this!

        if (config.useNativeJavaTypes) {
            return SolidityFunctionWrapper.getNativeType(typeName);
        } else {
            return typeName;
        }
    }

    private TypeName getWrapperRawType(TypeName typeName) {
        if (config.useNativeJavaTypes) {
            if (typeName instanceof ParameterizedTypeName) {
                return ClassName.get(List.class);
            }
            return SolidityFunctionWrapper.getNativeType(typeName);
        } else {
            return typeName;
        }
    }
}
