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
package org.web3j.codegen.generators.poets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.*;
import io.reactivex.Flowable;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.codegen.generators.SolidityWrapperGenerator;
import org.web3j.codegen.generators.SolidityWrapperGeneratorConfig;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.utils.Strings;

import static org.web3j.codegen.generators.SolidityConstants.*;
import static org.web3j.codegen.generators.SolidityWrapperGenerator.getNativeType;

public class EventPoet extends BasicPoet {

    public EventPoet(SolidityWrapperGeneratorConfig config) {
        super(config);
    }

    public List<MethodSpec> buildEventFunctions(
            TypeSpec.Builder classBuilder, List<AbiDefinition> functionDefinitions)
            throws ClassNotFoundException {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals(TYPE_EVENT)) {
                methodSpecs.addAll(buildEventFunctions(functionDefinition, classBuilder));
            }
        }
        return methodSpecs;
    }

    public List<MethodSpec> buildEventFunctions(
            AbiDefinition functionDefinition, TypeSpec.Builder classBuilder)
            throws ClassNotFoundException {
        String functionName = functionDefinition.getName();
        List<AbiDefinition.NamedType> inputs = functionDefinition.getInputs();
        String responseClassName = Strings.capitaliseFirstLetter(functionName) + "EventResponse";

        List<SolidityWrapperGenerator.NamedTypeName> parameters = new ArrayList<>();
        List<SolidityWrapperGenerator.NamedTypeName> indexedParameters = new ArrayList<>();
        List<SolidityWrapperGenerator.NamedTypeName> nonIndexedParameters = new ArrayList<>();

        for (AbiDefinition.NamedType namedType : inputs) {
            SolidityWrapperGenerator.NamedTypeName parameter =
                    new SolidityWrapperGenerator.NamedTypeName(
                            namedType.getName(),
                            SolidityWrapperGenerator.buildTypeName(
                                    namedType.getType(),
                                    namedType.getComponents(),
                                    config.useJavaPrimitiveTypes),
                            namedType.isIndexed(),
                            namedType.getComponents());
            if (namedType.isIndexed()) {
                indexedParameters.add(parameter);
            } else {
                nonIndexedParameters.add(parameter);
            }
            parameters.add(parameter);
        }

        classBuilder.addField(createEventDefinition(functionName, parameters));

        classBuilder.addType(
                buildEventResponseObject(
                        responseClassName, indexedParameters, nonIndexedParameters));

        List<MethodSpec> methods = new ArrayList<>();
        methods.add(
                buildEventTransactionReceiptFunction(
                        responseClassName, functionName, indexedParameters, nonIndexedParameters));

        methods.add(
                buildEventFlowableFunction(
                        responseClassName, functionName, indexedParameters, nonIndexedParameters));
        methods.add(buildDefaultEventFlowableFunction(responseClassName, functionName));
        return methods;
    }

    private FieldSpec createEventDefinition(
            String name, List<SolidityWrapperGenerator.NamedTypeName> parameters) {

        CodeBlock initializer = buildVariableLengthEventInitializer(name, parameters);

        return FieldSpec.builder(Event.class, buildEventDefinitionName(name))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(initializer)
                .build();
    }

    private static CodeBlock buildVariableLengthEventInitializer(
            String eventName, List<SolidityWrapperGenerator.NamedTypeName> parameterTypes) {

        List<Object> objects = new ArrayList<>();
        objects.add(Event.class);
        objects.add(eventName);

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (SolidityWrapperGenerator.NamedTypeName parameterType : parameterTypes) {
            objects.add(TypeReference.class);
            objects.add(parameterType.getTypeName());
        }

        String asListParams =
                parameterTypes.stream()
                        .map(
                                type -> {
                                    if (type.isIndexed()) {
                                        return "new $T<$T>(true) {}";
                                    } else {
                                        return "new $T<$T>() {}";
                                    }
                                })
                        .collect(Collectors.joining(", "));

        return CodeBlock.builder()
                .addStatement(
                        "new $T($S, \n" + "$T.<$T<?>>asList(" + asListParams + "))",
                        objects.toArray())
                .build();
    }

    private String buildEventDefinitionName(String eventName) {
        return eventName.toUpperCase() + "_EVENT";
    }

    TypeSpec buildEventResponseObject(
            String className,
            List<SolidityWrapperGenerator.NamedTypeName> indexedParameters,
            List<SolidityWrapperGenerator.NamedTypeName> nonIndexedParameters) {

        TypeSpec.Builder builder =
                TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        builder.superclass(BaseEventResponse.class);
        for (SolidityWrapperGenerator.NamedTypeName namedType : indexedParameters) {
            TypeName typeName = getIndexedEventWrapperType(namedType.typeName);
            builder.addField(typeName, namedType.getName(), Modifier.PUBLIC);
        }

        for (SolidityWrapperGenerator.NamedTypeName namedType : nonIndexedParameters) {
            if (((ClassName) namedType.typeName).simpleName().contains("TupleClass")) {
                builder.addField(namedType.typeName, namedType.getName(), Modifier.PUBLIC);
            } else {
                TypeName typeName =
                        SolidityWrapperGenerator.getWrapperType(
                                namedType.typeName, null, config.useNativeJavaTypes);
                builder.addField(typeName, namedType.getName(), Modifier.PUBLIC);
            }
        }

        return builder.build();
    }

    MethodSpec buildEventFlowableFunction(
            String responseClassName,
            String functionName,
            List<SolidityWrapperGenerator.NamedTypeName> indexedParameters,
            List<SolidityWrapperGenerator.NamedTypeName> nonIndexedParameters)
            throws ClassNotFoundException {

        String generatedFunctionName = Strings.lowercaseFirstLetter(functionName) + "EventFlowable";
        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(
                        ClassName.get(Flowable.class), ClassName.get("", responseClassName));

        MethodSpec.Builder flowableMethodBuilder =
                MethodSpec.methodBuilder(generatedFunctionName)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(EthFilter.class, FILTER)
                        .returns(parameterizedTypeName);

        TypeSpec converter =
                TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(
                                ParameterizedTypeName.get(
                                        ClassName.get(io.reactivex.functions.Function.class),
                                        ClassName.get(Log.class),
                                        ClassName.get("", responseClassName)))
                        .addMethod(
                                MethodSpec.methodBuilder("apply")
                                        .addAnnotation(Override.class)
                                        .addModifiers(Modifier.PUBLIC)
                                        .addParameter(Log.class, "log")
                                        .returns(ClassName.get("", responseClassName))
                                        .addStatement(
                                                "$T eventValues = extractEventParametersWithLog("
                                                        + buildEventDefinitionName(functionName)
                                                        + ", log)",
                                                Contract.EventValuesWithLog.class)
                                        .addStatement(
                                                "$1T typedResponse = new $1T()",
                                                ClassName.get("", responseClassName))
                                        .addCode(
                                                buildTypedResponse(
                                                        "typedResponse",
                                                        indexedParameters,
                                                        nonIndexedParameters,
                                                        true))
                                        .addStatement("return typedResponse")
                                        .build())
                        .build();

        flowableMethodBuilder.addStatement(
                "return web3j.ethLogFlowable(filter).map($L)", converter);

        return flowableMethodBuilder.build();
    }

    MethodSpec buildDefaultEventFlowableFunction(String responseClassName, String functionName) {

        String generatedFunctionName = Strings.lowercaseFirstLetter(functionName) + "EventFlowable";
        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(
                        ClassName.get(Flowable.class), ClassName.get("", responseClassName));

        MethodSpec.Builder flowableMethodBuilder =
                MethodSpec.methodBuilder(generatedFunctionName)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(DefaultBlockParameter.class, START_BLOCK)
                        .addParameter(DefaultBlockParameter.class, END_BLOCK)
                        .returns(parameterizedTypeName);

        flowableMethodBuilder
                .addStatement(
                        "$1T filter = new $1T($2L, $3L, " + "getContractAddress())",
                        EthFilter.class,
                        START_BLOCK,
                        END_BLOCK)
                .addStatement(
                        "filter.addSingleTopic($T.encode("
                                + buildEventDefinitionName(functionName)
                                + "))",
                        EventEncoder.class)
                .addStatement("return " + generatedFunctionName + "(filter)");

        return flowableMethodBuilder.build();
    }

    MethodSpec buildEventTransactionReceiptFunction(
            String responseClassName,
            String functionName,
            List<SolidityWrapperGenerator.NamedTypeName> indexedParameters,
            List<SolidityWrapperGenerator.NamedTypeName> nonIndexedParameters) {

        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(
                        ClassName.get(List.class), ClassName.get("", responseClassName));

        String generatedFunctionName =
                "get" + Strings.capitaliseFirstLetter(functionName) + "Events";
        MethodSpec.Builder transactionMethodBuilder =
                MethodSpec.methodBuilder(generatedFunctionName)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TransactionReceipt.class, "transactionReceipt")
                        .returns(parameterizedTypeName);

        transactionMethodBuilder
                .addStatement(
                        "$T valueList = extractEventParametersWithLog("
                                + buildEventDefinitionName(functionName)
                                + ", "
                                + "transactionReceipt)",
                        ParameterizedTypeName.get(List.class, Contract.EventValuesWithLog.class))
                .addStatement(
                        "$1T responses = new $1T(valueList.size())",
                        ParameterizedTypeName.get(
                                ClassName.get(ArrayList.class),
                                ClassName.get("", responseClassName)))
                .beginControlFlow(
                        "for ($T eventValues : valueList)", Contract.EventValuesWithLog.class)
                .addStatement("$1T typedResponse = new $1T()", ClassName.get("", responseClassName))
                .addCode(
                        buildTypedResponse(
                                "typedResponse", indexedParameters, nonIndexedParameters, false))
                .addStatement("responses.add(typedResponse)")
                .endControlFlow();

        transactionMethodBuilder.addStatement("return responses");
        return transactionMethodBuilder.build();
    }

    private TypeName getIndexedEventWrapperType(TypeName typeName) {
        if (config.useNativeJavaTypes) {
            return getEventNativeType(typeName);
        } else {
            return typeName;
        }
    }

    public static TypeName getEventNativeType(TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            return TypeName.get(byte[].class);
        }

        String simpleName = ((ClassName) typeName).simpleName();
        if (simpleName.equals(Utf8String.class.getSimpleName())) {
            return TypeName.get(byte[].class);
        } else {
            return getNativeType(typeName);
        }
    }

    public CodeBlock buildTypedResponse(
            String objectName,
            List<SolidityWrapperGenerator.NamedTypeName> indexedParameters,
            List<SolidityWrapperGenerator.NamedTypeName> nonIndexedParameters,
            boolean flowable) {
        String nativeConversion;

        if (config.useNativeJavaTypes) {
            nativeConversion = ".getValue()";
        } else {
            nativeConversion = "";
        }

        CodeBlock.Builder builder = CodeBlock.builder();
        if (flowable) {
            builder.addStatement("$L.log = log", objectName);
        } else {
            builder.addStatement("$L.log = eventValues.getLog()", objectName);
        }
        for (int i = 0; i < indexedParameters.size(); i++) {
            builder.addStatement(
                    "$L.$L = ($T) eventValues.getIndexedValues().get($L)" + nativeConversion,
                    objectName,
                    indexedParameters.get(i).getName(),
                    getIndexedEventWrapperType(indexedParameters.get(i).getTypeName()),
                    i);
        }

        for (int i = 0; i < nonIndexedParameters.size(); i++) {
            builder.addStatement(
                    "$L.$L = ($T) eventValues.getNonIndexedValues().get($L)" + nativeConversion,
                    objectName,
                    nonIndexedParameters.get(i).getName(),
                    SolidityWrapperGenerator.getWrapperType(
                            nonIndexedParameters.get(i).getTypeName(),
                            nonIndexedParameters.get(i).getComponents(),
                            config.useNativeJavaTypes),
                    i);
        }
        return builder.build();
    }
}
