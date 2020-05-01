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

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticArray;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.primitive.Byte;
import org.web3j.abi.datatypes.primitive.Char;
import org.web3j.abi.datatypes.primitive.Double;
import org.web3j.abi.datatypes.primitive.Float;
import org.web3j.abi.datatypes.primitive.Int;
import org.web3j.abi.datatypes.primitive.Long;
import org.web3j.abi.datatypes.primitive.Short;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.AbiDefinition.NamedType;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Collection;
import org.web3j.utils.Strings;
import org.web3j.utils.Version;

/** Generate Java Classes based on generated Solidity bin and abi files. */
public class SolidityFunctionWrapper extends Generator {

    private static final String BINARY = "BINARY";
    private static final String WEB3J = "web3j";
    private static final String CREDENTIALS = "credentials";
    private static final String CONTRACT_GAS_PROVIDER = "contractGasProvider";
    private static final String TRANSACTION_MANAGER = "transactionManager";
    private static final String INITIAL_VALUE = "initialWeiValue";
    private static final String CONTRACT_ADDRESS = "contractAddress";
    private static final String GAS_PRICE = "gasPrice";
    private static final String GAS_LIMIT = "gasLimit";
    private static final String FILTER = "filter";
    private static final String START_BLOCK = "startBlock";
    private static final String END_BLOCK = "endBlock";
    private static final String WEI_VALUE = "weiValue";
    private static final String FUNC_NAME_PREFIX = "FUNC_";
    private static final String TYPE_FUNCTION = "function";
    private static final String TYPE_EVENT = "event";
    private static final String TYPE_CONSTRUCTOR = "constructor";

    private static final Logger LOGGER = LoggerFactory.getLogger(SolidityFunctionWrapper.class);

    private static final String CODEGEN_WARNING =
            "<p>Auto generated code.\n"
                    + "<p><strong>Do not modify!</strong>\n"
                    + "<p>Please use the "
                    + "<a href=\"https://docs.web3j.io/command_line.html\">web3j command line tools</a>,\n"
                    + "or the "
                    + SolidityFunctionWrapperGenerator.class.getName()
                    + " in the \n"
                    + "<a href=\"https://github.com/web3j/web3j/tree/master/codegen\">"
                    + "codegen module</a> to update.\n";

    private final boolean useNativeJavaTypes;
    private final boolean useJavaPrimitiveTypes;
    private final boolean generateSendTxForCalls;

    private final int addressLength;

    private final HashMap<Integer, ClassName> structClassNameMap = new HashMap<>();

    private final List<NamedType> structsNamedTypeList = new ArrayList<>();

    private static final String regex = "(\\w+)(?:\\[(.*?)\\])(?:\\[(.*?)\\])?";

    private final GenerationReporter reporter;

    public SolidityFunctionWrapper(final boolean useNativeJavaTypes, final int addressLength) {
        this(useNativeJavaTypes, false, false, addressLength);
    }

    public SolidityFunctionWrapper(
            final boolean useNativeJavaTypes,
            final boolean useJavaPrimitiveTypes,
            final boolean generateSendTxForCalls,
            final int addressLength) {
        this(
                useNativeJavaTypes,
                useJavaPrimitiveTypes,
                generateSendTxForCalls,
                addressLength,
                new LogGenerationReporter(LOGGER));
    }

    public SolidityFunctionWrapper(
            final boolean useNativeJavaTypes,
            final boolean useJavaPrimitiveTypes,
            final boolean generateSendTxForCalls,
            final int addressLength,
            final GenerationReporter reporter) {
        this.useNativeJavaTypes = useNativeJavaTypes;
        this.useJavaPrimitiveTypes = useJavaPrimitiveTypes;
        this.addressLength = addressLength;
        this.reporter = reporter;
        this.generateSendTxForCalls = generateSendTxForCalls;
    }

    public void generateJavaFiles(
            final String contractName,
            final String bin,
            final List<AbiDefinition> abi,
            final String destinationDir,
            final String basePackageName,
            final Map<String, String> addresses)
            throws IOException, ClassNotFoundException {

        generateJavaFiles(
                Contract.class, contractName, bin, abi, destinationDir, basePackageName, addresses);
    }

    public void generateJavaFiles(
            final Class<? extends Contract> contractClass,
            final String contractName,
            final String bin,
            final List<AbiDefinition> abi,
            final String destinationDir,
            final String basePackageName,
            final Map<String, String> addresses)
            throws IOException, ClassNotFoundException {

        if (!java.lang.reflect.Modifier.isAbstract(contractClass.getModifiers())) {
            throw new IllegalArgumentException("Contract base class must be abstract");
        }

        final String className = Strings.capitaliseFirstLetter(contractName);
        final TypeSpec.Builder classBuilder = createClassBuilder(contractClass, className, bin);

        classBuilder.addAnnotation(
                AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "\"rawtypes\"")
                        .build());

        classBuilder.addMethod(buildConstructor(Credentials.class, CREDENTIALS, false));
        classBuilder.addMethod(buildConstructor(Credentials.class, CREDENTIALS, true));
        classBuilder.addMethod(
                buildConstructor(TransactionManager.class, TRANSACTION_MANAGER, false));
        classBuilder.addMethod(
                buildConstructor(TransactionManager.class, TRANSACTION_MANAGER, true));
        classBuilder.addFields(buildFuncNameConstants(abi));
        classBuilder.addTypes(buildStructTypes(abi));
        buildStructsNamedTypesList(abi);
        classBuilder.addMethods(buildFunctionDefinitions(classBuilder, abi));
        classBuilder.addMethod(buildLoad(className, Credentials.class, CREDENTIALS, false));
        classBuilder.addMethod(
                buildLoad(className, TransactionManager.class, TRANSACTION_MANAGER, false));
        classBuilder.addMethod(buildLoad(className, Credentials.class, CREDENTIALS, true));
        classBuilder.addMethod(
                buildLoad(className, TransactionManager.class, TRANSACTION_MANAGER, true));
        if (!bin.equals(Contract.BIN_NOT_PROVIDED)) {
            classBuilder.addMethods(buildDeployMethods(className, abi));
        }

        addAddressesSupport(classBuilder, addresses);

        write(basePackageName, classBuilder.build(), destinationDir);
    }

    private void buildStructsNamedTypesList(List<AbiDefinition> abi) {
        structsNamedTypeList.addAll(
                abi.stream()
                        .flatMap(
                                definition -> {
                                    List<AbiDefinition.NamedType> parameters = new ArrayList<>();
                                    parameters.addAll(definition.getInputs());
                                    parameters.addAll(definition.getOutputs());
                                    return parameters.stream()
                                            .filter(
                                                    namedType ->
                                                            namedType.getType().equals("tuple"));
                                })
                        .collect(Collectors.toList()));
    }

    /**
     * Verifies if the two structs are the same. Equal structs means: - They have the same field
     * names - They have the same field types The order of declaring the fields does not matter.
     *
     * @return True if they are the same fields
     */
    private boolean isSameStruct(NamedType base, NamedType target) {
        for (NamedType baseField : base.getComponents()) {
            if (!target.getComponents().stream()
                    .anyMatch(
                            targetField ->
                                    baseField.getType().equals(targetField.getType())
                                            && baseField.getName().equals(targetField.getName())))
                return false;
        }
        return true;
    }

    private void addAddressesSupport(
            final TypeSpec.Builder classBuilder, final Map<String, String> addresses) {
        if (addresses != null) {

            final ClassName stringType = ClassName.get(String.class);
            final ClassName mapType = ClassName.get(HashMap.class);
            final TypeName mapStringString =
                    ParameterizedTypeName.get(mapType, stringType, stringType);
            final FieldSpec addressesStaticField =
                    FieldSpec.builder(
                                    mapStringString,
                                    "_addresses",
                                    Modifier.PROTECTED,
                                    Modifier.STATIC,
                                    Modifier.FINAL)
                            .build();
            classBuilder.addField(addressesStaticField);

            final CodeBlock.Builder staticInit = CodeBlock.builder();
            staticInit.addStatement("_addresses = new HashMap<String, String>()");
            addresses.forEach(
                    (k, v) ->
                            staticInit.addStatement(
                                    String.format("_addresses.put(\"%1s\", \"%2s\")", k, v)));
            classBuilder.addStaticBlock(staticInit.build());

            // See org.web3j.tx.Contract#getStaticDeployedAddress(String)
            final MethodSpec getAddress =
                    MethodSpec.methodBuilder("getStaticDeployedAddress")
                            .addModifiers(Modifier.PROTECTED)
                            .returns(stringType)
                            .addParameter(stringType, "networkId")
                            .addCode(
                                    CodeBlock.builder()
                                            .addStatement("return _addresses.get(networkId)")
                                            .build())
                            .build();
            classBuilder.addMethod(getAddress);

            final MethodSpec getPreviousAddress =
                    MethodSpec.methodBuilder("getPreviouslyDeployedAddress")
                            .addModifiers(Modifier.PUBLIC)
                            .addModifiers(Modifier.STATIC)
                            .returns(stringType)
                            .addParameter(stringType, "networkId")
                            .addCode(
                                    CodeBlock.builder()
                                            .addStatement("return _addresses.get(networkId)")
                                            .build())
                            .build();
            classBuilder.addMethod(getPreviousAddress);
        }
    }

    private TypeSpec.Builder createClassBuilder(
            final Class<? extends Contract> contractClass,
            final String className,
            final String binary) {

        final String javadoc = CODEGEN_WARNING + getWeb3jVersion();

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(javadoc)
                .superclass(contractClass)
                .addField(createBinaryDefinition(binary));
    }

    private String getWeb3jVersion() {
        String version;

        try {
            // This only works if run as part of the web3j command line tools which contains
            // a version.properties file
            version = Version.getVersion();
        } catch (final IOException | NullPointerException e) {
            version = Version.DEFAULT;
        }
        return "\n<p>Generated with web3j version " + version + ".\n";
    }

    private FieldSpec createBinaryDefinition(final String binary) {
        if (binary.length() < 65534) {
            return FieldSpec.builder(String.class, BINARY)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("$S", binary)
                    .build();
        }

        String[] argsArray = binary.split("(?<=\\G.{65534})");
        StringBuilder stringBuilderString = new StringBuilder().append("new StringBuilder()");
        for (String s : argsArray) {
            stringBuilderString.append(".append(\"");
            stringBuilderString.append(s);
            stringBuilderString.append("\")");
        }
        stringBuilderString.append(".toString()");
        return FieldSpec.builder(String.class, BINARY)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .initializer(CodeBlock.of(stringBuilderString.toString()))
                .build();
    }

    private FieldSpec createEventDefinition(
            final String name, final List<NamedTypeName> parameters) {

        final CodeBlock initializer = buildVariableLengthEventInitializer(name, parameters);

        return FieldSpec.builder(Event.class, buildEventDefinitionName(name))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(initializer)
                .build();
    }

    private String buildEventDefinitionName(final String eventName) {
        return eventName.toUpperCase() + "_EVENT";
    }

    private List<MethodSpec> buildFunctionDefinitions(
            final TypeSpec.Builder classBuilder, final List<AbiDefinition> functionDefinitions)
            throws ClassNotFoundException {

        final Set<String> duplicateFunctionNames = getDuplicateFunctionNames(functionDefinitions);
        final List<MethodSpec> methodSpecs = new ArrayList<>();
        for (final AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals(TYPE_FUNCTION)) {
                final String functionName = funcNameToConst(functionDefinition.getName(), true);
                final boolean useUpperCase = !duplicateFunctionNames.contains(functionName);
                methodSpecs.addAll(buildFunctions(functionDefinition, useUpperCase));
            } else if (functionDefinition.getType().equals(TYPE_EVENT)) {
                methodSpecs.addAll(buildEventFunctions(functionDefinition, classBuilder));
            }
        }
        return methodSpecs;
    }

    private List<TypeSpec> buildStructTypes(final List<AbiDefinition> functionDefinitions)
            throws ClassNotFoundException {
        final List<AbiDefinition.NamedType> orderedKeys = extractStructs(functionDefinitions);
        int structCounter = 0;
        final List<TypeSpec> structs = new ArrayList<>();
        for (final AbiDefinition.NamedType namedType : orderedKeys) {
            final String internalType = namedType.getInternalType();
            final String structName;
            if (internalType == null || internalType.isEmpty()) {
                structName = "Struct" + structCounter;
            } else {
                structName = internalType.substring(internalType.lastIndexOf(".") + 1);
            }

            final TypeSpec.Builder builder =
                    TypeSpec.classBuilder(structName)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            final MethodSpec.Builder constructorBuilder =
                    MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
                            .addStatement(
                                    "super("
                                            + buildStructConstructorParameterDefinition(
                                                    namedType.getComponents(), useNativeJavaTypes)
                                            + ")");

            final MethodSpec.Builder nativeConstructorBuilder =
                    MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
                            .addStatement(
                                    "super("
                                            + buildStructConstructorParameterDefinition(
                                                    namedType.getComponents(), false)
                                            + ")");

            for (AbiDefinition.NamedType component : namedType.getComponents()) {
                if (component.getType().equals("tuple")) {
                    final ClassName typeName = structClassNameMap.get(component.structIdentifier());
                    builder.addField(typeName, component.getName(), Modifier.PUBLIC);
                    constructorBuilder.addParameter(typeName, component.getName());
                    nativeConstructorBuilder.addParameter(typeName, component.getName());

                } else {
                    final TypeName nativeTypeName =
                            buildTypeName(component.getType(), useJavaPrimitiveTypes);
                    final TypeName wrappedTypeName = getWrapperType(nativeTypeName);
                    builder.addField(wrappedTypeName, component.getName(), Modifier.PUBLIC);
                    constructorBuilder.addParameter(wrappedTypeName, component.getName());
                    nativeConstructorBuilder.addParameter(nativeTypeName, component.getName());
                }
                constructorBuilder.addStatement(
                        "this." + component.getName() + " = " + component.getName());
                nativeConstructorBuilder.addStatement(
                        "this."
                                + component.getName()
                                + " = "
                                + component.getName()
                                + (useNativeJavaTypes
                                                && structClassNameMap.keySet().stream()
                                                        .noneMatch(
                                                                i ->
                                                                        i
                                                                                == component
                                                                                        .structIdentifier())
                                        ? ".getValue()"
                                        : ""));
            }

            builder.superclass(namedType.isDynamic() ? DynamicStruct.class : StaticStruct.class);
            builder.addMethod(constructorBuilder.build());
            if (useNativeJavaTypes
                    && !namedType.getComponents().isEmpty()
                    && namedType.getComponents().stream()
                            .anyMatch(
                                    component ->
                                            structClassNameMap.keySet().stream()
                                                    .noneMatch(
                                                            i ->
                                                                    i
                                                                            == component
                                                                                    .structIdentifier()))) {
                builder.addMethod(nativeConstructorBuilder.build());
            }
            structClassNameMap.put(namedType.structIdentifier(), ClassName.get("", structName));
            structs.add(builder.build());
            structCounter++;
        }
        return structs;
    }

    private NamedType normalizeNamedType(NamedType namedType) {
        if (namedType.getType().endsWith("[]") && namedType.getInternalType().endsWith("[]")) {
            return new NamedType(
                    namedType.getName(),
                    namedType.getType().substring(0, namedType.getType().length() - 2),
                    namedType.getComponents(),
                    namedType
                            .getInternalType()
                            .substring(0, namedType.getInternalType().length() - 2),
                    namedType.isIndexed());
        } else {
            return namedType;
        }
    }

    @NotNull
    private List<AbiDefinition.NamedType> extractStructs(
            final List<AbiDefinition> functionDefinitions) {
        final HashMap<Integer, AbiDefinition.NamedType> structMap = new LinkedHashMap<>();
        functionDefinitions.stream()
                .flatMap(
                        definition -> {
                            List<AbiDefinition.NamedType> parameters = new ArrayList<>();
                            parameters.addAll(definition.getInputs());
                            parameters.addAll(definition.getOutputs());
                            return parameters.stream()
                                    .map(this::normalizeNamedType)
                                    .filter(namedType -> namedType.getType().equals("tuple"));
                        })
                .forEach(
                        namedType -> {
                            structMap.put(namedType.structIdentifier(), namedType);
                            extractNested(namedType).stream()
                                    .filter(
                                            nestedNamedStruct ->
                                                    nestedNamedStruct.getType().equals("tuple"))
                                    .forEach(
                                            nestedNamedType ->
                                                    structMap.put(
                                                            nestedNamedType.structIdentifier(),
                                                            nestedNamedType));
                        });

        return structMap.values().stream()
                .sorted(Comparator.comparingInt(AbiDefinition.NamedType::nestedness))
                .collect(Collectors.toList());
    }

    private String buildStructConstructorParameterDefinition(
            final List<AbiDefinition.NamedType> components, final boolean useNativeJavaTypes)
            throws ClassNotFoundException {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < components.size(); i++) {
            final AbiDefinition.NamedType component = components.get(i);
            stringBuilder.append(i > 0 ? "," : "");
            stringBuilder.append(
                    (!component.getType().equals("tuple") && useNativeJavaTypes
                            ? "new " + buildTypeName(component.getType(), false) + "("
                            : ""));
            stringBuilder.append(
                    (component.getType().equals("tuple")
                            ? component.getName()
                            : useNativeJavaTypes
                                    ? component.getName() + ")"
                                    : component.getName()));
        }
        return stringBuilder.toString();
    }

    private java.util.Collection<? extends AbiDefinition.NamedType> extractNested(
            final AbiDefinition.NamedType namedType) {
        if (namedType.getComponents().size() == 0) {
            return new ArrayList<>();
        } else {
            List<AbiDefinition.NamedType> nestedStructs = new ArrayList<>();
            namedType
                    .getComponents()
                    .forEach(
                            nestedNamedStruct -> {
                                nestedStructs.add(nestedNamedStruct);
                                nestedStructs.addAll(extractNested(nestedNamedStruct));
                            });
            return nestedStructs;
        }
    }

    private Set<String> getDuplicateFunctionNames(final List<AbiDefinition> functionDefinitions) {
        final Set<String> duplicateNames = new HashSet<>();
        final Set<String> functionNames = new HashSet<>();
        for (final AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getName() != null
                    && TYPE_FUNCTION.equals(functionDefinition.getType())) {
                final String functionName = funcNameToConst(functionDefinition.getName(), true);
                if (!functionNames.add(functionName)) {
                    duplicateNames.add(functionName);
                }
            }
        }
        return duplicateNames;
    }

    List<MethodSpec> buildDeployMethods(
            final String className, final List<AbiDefinition> functionDefinitions)
            throws ClassNotFoundException {
        boolean constructor = false;
        final List<MethodSpec> methodSpecs = new ArrayList<>();
        for (final AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals(TYPE_CONSTRUCTOR)) {
                constructor = true;
                methodSpecs.add(
                        buildDeploy(
                                className,
                                functionDefinition,
                                Credentials.class,
                                CREDENTIALS,
                                true));
                methodSpecs.add(
                        buildDeploy(
                                className,
                                functionDefinition,
                                TransactionManager.class,
                                TRANSACTION_MANAGER,
                                true));
                methodSpecs.add(
                        buildDeploy(
                                className,
                                functionDefinition,
                                Credentials.class,
                                CREDENTIALS,
                                false));
                methodSpecs.add(
                        buildDeploy(
                                className,
                                functionDefinition,
                                TransactionManager.class,
                                TRANSACTION_MANAGER,
                                false));
            }
        }

        // constructor will not be specified in ABI file if its empty
        if (!constructor) {
            final MethodSpec.Builder credentialsMethodBuilder =
                    getDeployMethodSpec(className, Credentials.class, CREDENTIALS, false, true);
            methodSpecs.add(
                    buildDeployNoParams(
                            credentialsMethodBuilder, className, CREDENTIALS, false, true));

            final MethodSpec.Builder credentialsMethodBuilderNoGasProvider =
                    getDeployMethodSpec(className, Credentials.class, CREDENTIALS, false, false);
            methodSpecs.add(
                    buildDeployNoParams(
                            credentialsMethodBuilderNoGasProvider,
                            className,
                            CREDENTIALS,
                            false,
                            false));

            final MethodSpec.Builder transactionManagerMethodBuilder =
                    getDeployMethodSpec(
                            className, TransactionManager.class, TRANSACTION_MANAGER, false, true);
            methodSpecs.add(
                    buildDeployNoParams(
                            transactionManagerMethodBuilder,
                            className,
                            TRANSACTION_MANAGER,
                            false,
                            true));

            final MethodSpec.Builder transactionManagerMethodBuilderNoGasProvider =
                    getDeployMethodSpec(
                            className, TransactionManager.class, TRANSACTION_MANAGER, false, false);
            methodSpecs.add(
                    buildDeployNoParams(
                            transactionManagerMethodBuilderNoGasProvider,
                            className,
                            TRANSACTION_MANAGER,
                            false,
                            false));
        }

        return methodSpecs;
    }

    Iterable<FieldSpec> buildFuncNameConstants(final List<AbiDefinition> functionDefinitions) {
        final List<FieldSpec> fields = new ArrayList<>();
        final Set<String> fieldNames = new HashSet<>();
        fieldNames.add(Contract.FUNC_DEPLOY);
        final Set<String> duplicateFunctionNames = getDuplicateFunctionNames(functionDefinitions);
        if (!duplicateFunctionNames.isEmpty()) {
            System.out.println(
                    "\nWarning: Duplicate field(s) found: "
                            + duplicateFunctionNames
                            + ". Please don't use names which will be the same in uppercase.");
        }
        for (final AbiDefinition functionDefinition : functionDefinitions) {
            if (functionDefinition.getType().equals(TYPE_FUNCTION)) {
                final String funcName = functionDefinition.getName();

                if (!fieldNames.contains(funcName)) {
                    final boolean useUpperCase =
                            !duplicateFunctionNames.contains(funcNameToConst(funcName, true));
                    final FieldSpec field =
                            FieldSpec.builder(
                                            String.class,
                                            funcNameToConst(funcName, useUpperCase),
                                            Modifier.PUBLIC,
                                            Modifier.STATIC,
                                            Modifier.FINAL)
                                    .initializer("$S", funcName)
                                    .build();
                    fields.add(field);
                    fieldNames.add(funcName);
                }
            }
        }
        return fields;
    }

    private static MethodSpec buildConstructor(
            final Class<?> authType, final String authName, final boolean withGasProvider) {
        final MethodSpec.Builder toReturn =
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PROTECTED)
                        .addParameter(String.class, CONTRACT_ADDRESS)
                        .addParameter(Web3j.class, WEB3J)
                        .addParameter(authType, authName);

        if (withGasProvider) {
            toReturn.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER)
                    .addStatement(
                            "super($N, $N, $N, $N, $N)",
                            BINARY,
                            CONTRACT_ADDRESS,
                            WEB3J,
                            authName,
                            CONTRACT_GAS_PROVIDER);
        } else {
            toReturn.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT)
                    .addStatement(
                            "super($N, $N, $N, $N, $N, $N)",
                            BINARY,
                            CONTRACT_ADDRESS,
                            WEB3J,
                            authName,
                            GAS_PRICE,
                            GAS_LIMIT)
                    .addAnnotation(Deprecated.class);
        }

        return toReturn.build();
    }

    private MethodSpec buildDeploy(
            final String className,
            final AbiDefinition functionDefinition,
            final Class<?> authType,
            final String authName,
            final boolean withGasProvider)
            throws ClassNotFoundException {

        final boolean isPayable = functionDefinition.isPayable();

        final MethodSpec.Builder methodBuilder =
                getDeployMethodSpec(className, authType, authName, isPayable, withGasProvider);
        final String inputParams = addParameters(methodBuilder, functionDefinition.getInputs());

        if (!inputParams.isEmpty()) {
            return buildDeployWithParams(
                    methodBuilder, className, inputParams, authName, isPayable, withGasProvider);
        } else {
            return buildDeployNoParams(
                    methodBuilder, className, authName, isPayable, withGasProvider);
        }
    }

    private static MethodSpec buildDeployWithParams(
            final MethodSpec.Builder methodBuilder,
            final String className,
            final String inputParams,
            final String authName,
            final boolean isPayable,
            final boolean withGasProvider) {

        methodBuilder.addStatement(
                "$T encodedConstructor = $T.encodeConstructor(" + "$T.<$T>asList($L)" + ")",
                String.class,
                FunctionEncoder.class,
                Arrays.class,
                Type.class,
                inputParams);
        if (isPayable && !withGasProvider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall("
                            + "$L.class, $L, $L, $L, $L, $L, encodedConstructor, $L)",
                    className,
                    WEB3J,
                    authName,
                    GAS_PRICE,
                    GAS_LIMIT,
                    BINARY,
                    INITIAL_VALUE);
            methodBuilder.addAnnotation(Deprecated.class);
        } else if (isPayable) {
            methodBuilder.addStatement(
                    "return deployRemoteCall("
                            + "$L.class, $L, $L, $L, $L, encodedConstructor, $L)",
                    className,
                    WEB3J,
                    authName,
                    CONTRACT_GAS_PROVIDER,
                    BINARY,
                    INITIAL_VALUE);
        } else if (!withGasProvider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, encodedConstructor)",
                    className,
                    WEB3J,
                    authName,
                    GAS_PRICE,
                    GAS_LIMIT,
                    BINARY);
            methodBuilder.addAnnotation(Deprecated.class);
        } else {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, encodedConstructor)",
                    className,
                    WEB3J,
                    authName,
                    CONTRACT_GAS_PROVIDER,
                    BINARY);
        }

        return methodBuilder.build();
    }

    private static MethodSpec buildDeployNoParams(
            final MethodSpec.Builder methodBuilder,
            final String className,
            final String authName,
            final boolean isPayable,
            final boolean withGasPRovider) {
        if (isPayable && !withGasPRovider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, \"\", $L)",
                    className,
                    WEB3J,
                    authName,
                    GAS_PRICE,
                    GAS_LIMIT,
                    BINARY,
                    INITIAL_VALUE);
            methodBuilder.addAnnotation(Deprecated.class);
        } else if (isPayable) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, \"\", $L)",
                    className,
                    WEB3J,
                    authName,
                    CONTRACT_GAS_PROVIDER,
                    BINARY,
                    INITIAL_VALUE);
        } else if (!withGasPRovider) {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, $L, \"\")",
                    className,
                    WEB3J,
                    authName,
                    GAS_PRICE,
                    GAS_LIMIT,
                    BINARY);
            methodBuilder.addAnnotation(Deprecated.class);
        } else {
            methodBuilder.addStatement(
                    "return deployRemoteCall($L.class, $L, $L, $L, $L, \"\")",
                    className,
                    WEB3J,
                    authName,
                    CONTRACT_GAS_PROVIDER,
                    BINARY);
        }

        return methodBuilder.build();
    }

    private static MethodSpec.Builder getDeployMethodSpec(
            final String className,
            final Class<?> authType,
            final String authName,
            final boolean isPayable,
            final boolean withGasProvider) {
        final MethodSpec.Builder builder =
                MethodSpec.methodBuilder("deploy")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(buildRemoteCall(TypeVariableName.get(className, Type.class)))
                        .addParameter(Web3j.class, WEB3J)
                        .addParameter(authType, authName);
        if (isPayable && !withGasProvider) {
            return builder.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT)
                    .addParameter(BigInteger.class, INITIAL_VALUE);
        } else if (isPayable) {
            return builder.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER)
                    .addParameter(BigInteger.class, INITIAL_VALUE);
        } else if (withGasProvider) {
            return builder.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER);
        } else {
            return builder.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT);
        }
    }

    private static MethodSpec buildLoad(
            final String className,
            final Class<?> authType,
            final String authName,
            final boolean withGasProvider) {
        final MethodSpec.Builder toReturn =
                MethodSpec.methodBuilder("load")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeVariableName.get(className, Type.class))
                        .addParameter(String.class, CONTRACT_ADDRESS)
                        .addParameter(Web3j.class, WEB3J)
                        .addParameter(authType, authName);

        if (withGasProvider) {
            toReturn.addParameter(ContractGasProvider.class, CONTRACT_GAS_PROVIDER)
                    .addStatement(
                            "return new $L($L, $L, $L, $L)",
                            className,
                            CONTRACT_ADDRESS,
                            WEB3J,
                            authName,
                            CONTRACT_GAS_PROVIDER);
        } else {
            toReturn.addParameter(BigInteger.class, GAS_PRICE)
                    .addParameter(BigInteger.class, GAS_LIMIT)
                    .addStatement(
                            "return new $L($L, $L, $L, $L, $L)",
                            className,
                            CONTRACT_ADDRESS,
                            WEB3J,
                            authName,
                            GAS_PRICE,
                            GAS_LIMIT)
                    .addAnnotation(Deprecated.class);
        }

        return toReturn.build();
    }

    String addParameters(
            final MethodSpec.Builder methodBuilder, final List<AbiDefinition.NamedType> namedTypes)
            throws ClassNotFoundException {

        final List<ParameterSpec> inputParameterTypes =
                buildParameterTypes(namedTypes, useJavaPrimitiveTypes);

        final List<ParameterSpec> nativeInputParameterTypes =
                new ArrayList<>(inputParameterTypes.size());

        for (int i = 0; i < inputParameterTypes.size(); ++i) {
            final TypeName typeName;
            if (namedTypes.get(i).getType().equals("tuple")) {
                typeName = structClassNameMap.get(namedTypes.get(i).structIdentifier());
            } else if (namedTypes.get(i).getType().startsWith("tuple")
                    && namedTypes.get(i).getType().contains("[")) {
                typeName = buildStructArrayTypeName(namedTypes.get(i), true);
            } else {
                typeName = getWrapperType(inputParameterTypes.get(i).type);
            }
            nativeInputParameterTypes.add(
                    ParameterSpec.builder(typeName, inputParameterTypes.get(i).name).build());
        }

        methodBuilder.addParameters(nativeInputParameterTypes);

        if (useNativeJavaTypes) {
            return Collection.join(
                    inputParameterTypes,
                    ", \n",
                    // this results in fully qualified names being generated
                    this::createMappedParameterTypes);
        } else {
            return Collection.join(inputParameterTypes, ", ", parameterSpec -> parameterSpec.name);
        }
    }

    private String createMappedParameterTypes(final ParameterSpec parameterSpec) {
        if (parameterSpec.type instanceof ParameterizedTypeName) {
            final List<TypeName> typeNames =
                    ((ParameterizedTypeName) parameterSpec.type).typeArguments;
            if (typeNames.size() != 1) {
                throw new UnsupportedOperationException(
                        "Only a single parameterized type is supported");
            } else if (structClassNameMap.values().stream()
                    .map(ClassName::simpleName)
                    .anyMatch(
                            name ->
                                    name.equals(
                                            ((ClassName)
                                                            ((ParameterizedTypeName)
                                                                            parameterSpec.type)
                                                                    .typeArguments.get(0))
                                                    .simpleName()))) {
                String structName =
                        structClassNameMap.values().stream()
                                .map(ClassName::simpleName)
                                .filter(
                                        name ->
                                                name.equals(
                                                        ((ClassName)
                                                                        ((ParameterizedTypeName)
                                                                                        parameterSpec
                                                                                                .type)
                                                                                .typeArguments.get(
                                                                                        0))
                                                                .simpleName()))
                                .collect(Collectors.toList())
                                .get(0);
                return "new "
                        + parameterSpec.type
                        + "("
                        + structName
                        + ".class, "
                        + parameterSpec.name
                        + ")";
            } else {
                String parameterSpecType = parameterSpec.type.toString();
                final TypeName typeName = typeNames.get(0);
                String typeMapInput = typeName + ".class";
                String componentType = typeName.toString();
                if (typeName instanceof ParameterizedTypeName) {
                    final List<TypeName> typeArguments =
                            ((ParameterizedTypeName) typeName).typeArguments;
                    if (typeArguments.size() != 1) {
                        throw new UnsupportedOperationException(
                                "Only a single parameterized type is supported");
                    }
                    final TypeName innerTypeName = typeArguments.get(0);
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
        } else if (structClassNameMap.values().stream()
                .map(ClassName::simpleName)
                .noneMatch(name -> name.equals(parameterSpec.type.toString()))) {
            String constructor = "new " + parameterSpec.type + "(";
            if (Address.class.getCanonicalName().equals(parameterSpec.type.toString())
                    && addressLength != Address.DEFAULT_LENGTH) {

                constructor += (addressLength * java.lang.Byte.SIZE) + ", ";
            }
            return constructor + parameterSpec.name + ")";
        } else {
            return parameterSpec.name;
        }
    }

    private TypeName getWrapperType(final TypeName typeName) {
        if (useNativeJavaTypes) {
            return getNativeType(typeName);
        } else {
            return typeName;
        }
    }

    private TypeName getWrapperRawType(final TypeName typeName) {
        if (useNativeJavaTypes) {
            if (typeName instanceof ParameterizedTypeName) {
                return ClassName.get(List.class);
            }
            return getNativeType(typeName);
        } else {
            return typeName;
        }
    }

    private TypeName getIndexedEventWrapperType(final TypeName typeName) {
        if (useNativeJavaTypes) {
            return getEventNativeType(typeName);
        } else {
            return typeName;
        }
    }

    static TypeName getNativeType(final TypeName typeName) {

        if (typeName instanceof ParameterizedTypeName) {
            return getNativeType((ParameterizedTypeName) typeName);
        }

        final String simpleName = ((ClassName) typeName).simpleName();

        if (simpleName.equals(Address.class.getSimpleName())) {
            return TypeName.get(String.class);
        } else if (simpleName.startsWith("Uint")) {
            return TypeName.get(BigInteger.class);
        } else if (simpleName.equals(Utf8String.class.getSimpleName())) {
            return TypeName.get(String.class);
        } else if (simpleName.startsWith("Bytes") || simpleName.equals("DynamicBytes")) {
            return TypeName.get(byte[].class);
        } else if (simpleName.startsWith("Bool")) {
            return TypeName.get(java.lang.Boolean.class);
            // boolean cannot be a parameterized type
        } else if (simpleName.equals(Byte.class.getSimpleName())) {
            return TypeName.get(java.lang.Byte.class);
        } else if (simpleName.equals(Char.class.getSimpleName())) {
            return TypeName.get(Character.class);
        } else if (simpleName.equals(Double.class.getSimpleName())) {
            return TypeName.get(java.lang.Double.class);
        } else if (simpleName.equals(Float.class.getSimpleName())) {
            return TypeName.get(java.lang.Float.class);
        } else if (simpleName.equals(Int.class.getSimpleName())) {
            return TypeName.get(Integer.class);
        } else if (simpleName.equals(Long.class.getSimpleName())) {
            return TypeName.get(java.lang.Long.class);
        } else if (simpleName.equals(Short.class.getSimpleName())) {
            return TypeName.get(java.lang.Short.class);
        } else if (simpleName.startsWith("Int")) {
            return TypeName.get(BigInteger.class);
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported type: " + typeName + ", no native type mapping exists.");
        }
    }

    static TypeName getNativeType(final ParameterizedTypeName parameterizedTypeName) {
        final List<TypeName> typeNames = parameterizedTypeName.typeArguments;
        final List<TypeName> nativeTypeNames = new ArrayList<>(typeNames.size());
        for (final TypeName enclosedTypeName : typeNames) {
            nativeTypeNames.add(getNativeType(enclosedTypeName));
        }
        return ParameterizedTypeName.get(
                ClassName.get(List.class), nativeTypeNames.toArray(new TypeName[0]));
    }

    static TypeName getEventNativeType(final TypeName typeName) {
        if (typeName instanceof ParameterizedTypeName) {
            return TypeName.get(byte[].class);
        }

        final String simpleName = ((ClassName) typeName).simpleName();
        if (simpleName.equals(Utf8String.class.getSimpleName())) {
            return TypeName.get(byte[].class);
        } else {
            return getNativeType(typeName);
        }
    }

    private List<ParameterSpec> buildParameterTypes(
            final List<AbiDefinition.NamedType> namedTypes, final boolean primitives)
            throws ClassNotFoundException {

        final List<ParameterSpec> result = new ArrayList<>(namedTypes.size());
        for (int i = 0; i < namedTypes.size(); i++) {
            final AbiDefinition.NamedType namedType = namedTypes.get(i);

            final String name = createValidParamName(namedType.getName(), i);
            final String type = namedTypes.get(i).getType();

            if (type.equals("tuple")) {
                result.add(
                        ParameterSpec.builder(
                                        structClassNameMap.get(namedType.structIdentifier()), name)
                                .build());
            } else if (type.startsWith("tuple") && type.contains("[")) {
                result.add(
                        ParameterSpec.builder(buildStructArrayTypeName(namedType, primitives), name)
                                .build());
            } else {
                result.add(ParameterSpec.builder(buildTypeName(type, primitives), name).build());
            }
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
    static String createValidParamName(final String name, final int idx) {
        if (name == null || name.equals("")) {
            return "param" + idx;
        } else {
            return name;
        }
    }

    private List<TypeName> buildTypeNames(
            final List<AbiDefinition.NamedType> namedTypes, final boolean primitives)
            throws ClassNotFoundException {

        final List<TypeName> result = new ArrayList<>(namedTypes.size());
        for (final AbiDefinition.NamedType namedType : namedTypes) {
            if (namedType.getType().equals("tuple")) {
                result.add(structClassNameMap.get(namedType.structIdentifier()));
            } else if (namedType.getType().startsWith("tuple")
                    && namedType.getType().contains("[")) {
                result.add(buildStructArrayTypeName(namedType, primitives));
            } else {
                result.add(buildTypeName(namedType.getType(), primitives));
            }
        }
        return result;
    }

    /**
     * Builds the array of struct type name. In case of using the Java native types, we return the
     * <code>List<struct></code> class Else, we return the Web3j generated types.
     *
     * @param namedType Array of structs namedType
     * @param useNativeJavaTypes Set to true for java native types
     * @return ParametrizedTypeName of the array of structs, eg, <code>StaticArray3<StructName>
     *     </code>
     */
    private TypeName buildStructArrayTypeName(NamedType namedType, Boolean useNativeJavaTypes) {
        String structName;
        if (namedType.getInternalType().isEmpty()) {
            structName =
                    structClassNameMap
                            .get(
                                    structsNamedTypeList.stream()
                                            .filter(struct -> isSameStruct(namedType, struct))
                                            .collect(Collectors.toList())
                                            .get(0)
                                            .structIdentifier())
                            .simpleName();

        } else {
            structName =
                    namedType
                            .getInternalType()
                            .substring(
                                    namedType.getInternalType().lastIndexOf(".") + 1,
                                    namedType.getInternalType().indexOf("["));
        }

        if (useNativeJavaTypes)
            return ParameterizedTypeName.get(
                    ClassName.get(List.class), ClassName.get("", structName));

        String arrayLength =
                namedType
                        .getType()
                        .substring(
                                namedType.getType().indexOf('[') + 1,
                                namedType.getType().indexOf(']'));
        if (!arrayLength.isEmpty() && Integer.parseInt(arrayLength) > 0) {
            return ParameterizedTypeName.get(
                    ClassName.get("org.web3j.abi.datatypes.generated", "StaticArray" + arrayLength),
                    ClassName.get("", structName));
        } else {
            return ParameterizedTypeName.get(
                    ClassName.get(DynamicArray.class), ClassName.get("", structName));
        }
    }

    MethodSpec buildFunction(final AbiDefinition functionDefinition) throws ClassNotFoundException {
        return buildFunction(functionDefinition, true);
    }

    MethodSpec buildFunction(final AbiDefinition functionDefinition, final boolean useUpperCase)
            throws ClassNotFoundException {
        return buildFunctions(functionDefinition, useUpperCase).get(0);
    }

    List<MethodSpec> buildFunctions(final AbiDefinition functionDefinition)
            throws ClassNotFoundException {
        return buildFunctions(functionDefinition, true);
    }

    List<MethodSpec> buildFunctions(
            final AbiDefinition functionDefinition, final boolean useUpperCase)
            throws ClassNotFoundException {

        final List<MethodSpec> results = new ArrayList<>(2);
        String functionName = functionDefinition.getName();

        final String stateMutability = functionDefinition.getStateMutability();
        final boolean pureOrView = "pure".equals(stateMutability) || "view".equals(stateMutability);
        final boolean isFunctionDefinitionConstant = functionDefinition.isConstant() || pureOrView;

        if (generateSendTxForCalls) {
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

        final MethodSpec.Builder methodBuilder =
                MethodSpec.methodBuilder(functionName).addModifiers(Modifier.PUBLIC);

        final String inputParams = addParameters(methodBuilder, functionDefinition.getInputs());
        final List<TypeName> outputParameterTypes =
                buildTypeNames(functionDefinition.getOutputs(), useJavaPrimitiveTypes);

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
            if (generateSendTxForCalls) {
                final AbiDefinition sendFuncDefinition = new AbiDefinition(functionDefinition);
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
            final AbiDefinition functionDefinition,
            final MethodSpec.Builder methodBuilder,
            final List<TypeName> outputParameterTypes,
            final String inputParams,
            final boolean useUpperCase)
            throws ClassNotFoundException {

        final String functionName = functionDefinition.getName();

        if (outputParameterTypes.isEmpty()) {
            methodBuilder.addStatement(
                    "throw new RuntimeException"
                            + "(\"cannot call constant function with void return type\")");
        } else if (outputParameterTypes.size() == 1) {

            final TypeName typeName = outputParameterTypes.get(0);
            final TypeName nativeReturnTypeName;
            if (functionDefinition.getOutputs().get(0).getType().equals("tuple")) {
                nativeReturnTypeName =
                        structClassNameMap.get(
                                functionDefinition.getOutputs().get(0).structIdentifier());
            } else if (functionDefinition.getOutputs().get(0).getType().startsWith("tuple")
                    && functionDefinition.getOutputs().get(0).getType().contains("[")) {
                nativeReturnTypeName = ClassName.get(List.class);
            } else if (useNativeJavaTypes) {
                nativeReturnTypeName = getWrapperRawType(typeName);
            } else {
                nativeReturnTypeName = getWrapperType(typeName);
            }
            methodBuilder.returns(buildRemoteFunctionCall(nativeReturnTypeName));

            methodBuilder.addStatement(
                    "final $T function = "
                            + "new $T($N, \n$T.<$T>asList($L), "
                            + "\n$T.<$T<?>>asList(new $T<$T>() {}))",
                    Function.class,
                    Function.class,
                    funcNameToConst(functionName, useUpperCase),
                    Arrays.class,
                    Type.class,
                    inputParams,
                    Arrays.class,
                    TypeReference.class,
                    TypeReference.class,
                    typeName);

            if (useNativeJavaTypes) {
                if (nativeReturnTypeName.equals(ClassName.get(List.class))) {
                    // We return list. So all the list elements should
                    // also be converted to native types
                    final TypeName listType = ParameterizedTypeName.get(List.class, Type.class);

                    final CodeBlock.Builder callCode = CodeBlock.builder();
                    callCode.addStatement(
                            "$T result = "
                                    + "($T) executeCallSingleValueReturn(function, $T.class)",
                            listType,
                            listType,
                            nativeReturnTypeName);
                    callCode.addStatement("return convertToNative(result)");

                    final TypeSpec callableType =
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
            final List<TypeName> returnTypes = new ArrayList<>();
            for (int i = 0; i < functionDefinition.getOutputs().size(); ++i) {
                if (functionDefinition.getOutputs().get(i).getType().equals("tuple")) {
                    returnTypes.add(
                            structClassNameMap.get(
                                    functionDefinition.getOutputs().get(i).structIdentifier()));
                } else if (functionDefinition.getOutputs().get(i).getType().startsWith("tuple")
                        && functionDefinition.getOutputs().get(i).getType().contains("[")) {
                    returnTypes.add(
                            buildStructArrayTypeName(functionDefinition.getOutputs().get(i), true));
                } else {
                    returnTypes.add(getWrapperType(outputParameterTypes.get(i)));
                }
            }

            final ParameterizedTypeName parameterizedTupleType =
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

    private static ParameterizedTypeName buildRemoteCall(final TypeName typeName) {
        return ParameterizedTypeName.get(ClassName.get(RemoteCall.class), typeName);
    }

    // FIXME RemoteFunctionCall has been removed
    private static ParameterizedTypeName buildRemoteFunctionCall(final TypeName typeName) {
        return ParameterizedTypeName.get(ClassName.get(RemoteCall.class), typeName);
    }

    private void buildTransactionFunction(
            final AbiDefinition functionDefinition,
            final MethodSpec.Builder methodBuilder,
            final String inputParams,
            final boolean useUpperCase) {

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

        final String functionName = functionDefinition.getName();

        methodBuilder.returns(buildRemoteFunctionCall(TypeName.get(TransactionReceipt.class)));

        methodBuilder.addStatement(
                "final $T function = new $T(\n$N, \n$T.<$T>asList($L), \n$T"
                        + ".<$T<?>>emptyList())",
                Function.class,
                Function.class,
                funcNameToConst(functionName, useUpperCase),
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

    TypeSpec buildEventResponseObject(
            final String className,
            final List<org.web3j.codegen.SolidityFunctionWrapper.NamedTypeName> indexedParameters,
            final List<org.web3j.codegen.SolidityFunctionWrapper.NamedTypeName>
                    nonIndexedParameters) {

        final TypeSpec.Builder builder =
                TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        builder.superclass(BaseEventResponse.class);
        for (final org.web3j.codegen.SolidityFunctionWrapper.NamedTypeName namedType :
                indexedParameters) {
            final TypeName typeName;
            if (namedType.getType().equals("tuple")) {
                typeName = structClassNameMap.get(namedType.structIdentifier());
            } else if (namedType.getType().startsWith("tuple")
                    && namedType.getType().contains("[")) {
                typeName = buildStructArrayTypeName(namedType.namedType, true);
            } else {
                typeName = getIndexedEventWrapperType(namedType.typeName);
            }
            builder.addField(typeName, namedType.getName(), Modifier.PUBLIC);
        }

        for (final org.web3j.codegen.SolidityFunctionWrapper.NamedTypeName namedType :
                nonIndexedParameters) {
            final TypeName typeName;
            if (namedType.getType().equals("tuple")) {
                typeName = structClassNameMap.get(namedType.structIdentifier());
            } else if (namedType.getType().startsWith("tuple")
                    && namedType.getType().contains("[")) {
                typeName = buildStructArrayTypeName(namedType.namedType, true);
            } else {
                typeName = getWrapperType(namedType.typeName);
            }
            builder.addField(typeName, namedType.getName(), Modifier.PUBLIC);
        }

        return builder.build();
    }

    MethodSpec buildEventFlowableFunction(
            final String responseClassName,
            final String functionName,
            final List<org.web3j.codegen.SolidityFunctionWrapper.NamedTypeName> indexedParameters,
            final List<org.web3j.codegen.SolidityFunctionWrapper.NamedTypeName>
                    nonIndexedParameters) {

        final String generatedFunctionName =
                Strings.lowercaseFirstLetter(functionName) + "EventFlowable";
        final ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(
                        ClassName.get(Flowable.class), ClassName.get("", responseClassName));

        final MethodSpec.Builder flowableMethodBuilder =
                MethodSpec.methodBuilder(generatedFunctionName)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(EthFilter.class, FILTER)
                        .returns(parameterizedTypeName);

        final TypeSpec converter =
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

    MethodSpec buildDefaultEventFlowableFunction(
            final String responseClassName, final String functionName) {

        final String generatedFunctionName =
                Strings.lowercaseFirstLetter(functionName) + "EventFlowable";
        final ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(
                        ClassName.get(Flowable.class), ClassName.get("", responseClassName));

        final MethodSpec.Builder flowableMethodBuilder =
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
            final String responseClassName,
            final String functionName,
            final List<NamedTypeName> indexedParameters,
            final List<NamedTypeName> nonIndexedParameters) {

        final ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(
                        ClassName.get(List.class), ClassName.get("", responseClassName));

        final String generatedFunctionName =
                "get" + Strings.capitaliseFirstLetter(functionName) + "Events";
        final MethodSpec.Builder transactionMethodBuilder =
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

    List<MethodSpec> buildEventFunctions(
            final AbiDefinition functionDefinition, final TypeSpec.Builder classBuilder)
            throws ClassNotFoundException {
        final String functionName = functionDefinition.getName();
        final List<AbiDefinition.NamedType> inputs = functionDefinition.getInputs();
        final String responseClassName =
                Strings.capitaliseFirstLetter(functionName) + "EventResponse";

        final List<NamedTypeName> parameters = new ArrayList<>();
        final List<NamedTypeName> indexedParameters = new ArrayList<>();
        final List<NamedTypeName> nonIndexedParameters = new ArrayList<>();

        for (AbiDefinition.NamedType namedType : inputs) {
            final TypeName typeName;
            if (namedType.getType().equals("tuple")) {
                typeName = structClassNameMap.get(namedType.structIdentifier());
            } else if (namedType.getType().startsWith("tuple")
                    && namedType.getType().contains("[")) {
                typeName = buildStructArrayTypeName(namedType, true);
            } else {
                typeName = buildTypeName(namedType.getType(), useJavaPrimitiveTypes);
            }
            NamedTypeName parameter = new NamedTypeName(namedType, typeName);
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

        final List<MethodSpec> methods = new ArrayList<>();
        methods.add(
                buildEventTransactionReceiptFunction(
                        responseClassName, functionName, indexedParameters, nonIndexedParameters));

        methods.add(
                buildEventFlowableFunction(
                        responseClassName, functionName, indexedParameters, nonIndexedParameters));
        methods.add(buildDefaultEventFlowableFunction(responseClassName, functionName));
        return methods;
    }

    CodeBlock buildTypedResponse(
            String objectName,
            List<org.web3j.codegen.SolidityFunctionWrapper.NamedTypeName> indexedParameters,
            List<org.web3j.codegen.SolidityFunctionWrapper.NamedTypeName> nonIndexedParameters,
            boolean flowable) {
        CodeBlock.Builder builder = CodeBlock.builder();
        if (flowable) {
            builder.addStatement("$L.log = log", objectName);
        } else {
            builder.addStatement("$L.log = eventValues.getLog()", objectName);
        }
        for (int i = 0; i < indexedParameters.size(); i++) {
            final NamedTypeName namedTypeName = indexedParameters.get(i);
            final String nativeConversion;
            if (useNativeJavaTypes
                    && structClassNameMap.values().stream()
                            .map(ClassName::simpleName)
                            .noneMatch(
                                    name -> name.equals(namedTypeName.getTypeName().toString()))) {
                nativeConversion = ".getValue()";
            } else {
                nativeConversion = "";
            }
            final TypeName indexedEventWrapperType;
            if (namedTypeName.getType().equals("tuple")) {
                indexedEventWrapperType = structClassNameMap.get(namedTypeName.structIdentifier());
            } else if (namedTypeName.getType().startsWith("tuple")
                    && namedTypeName.getType().contains("[")) {
                indexedEventWrapperType = buildStructArrayTypeName(namedTypeName.namedType, true);
            } else {
                indexedEventWrapperType = getIndexedEventWrapperType(namedTypeName.getTypeName());
            }
            builder.addStatement(
                    "$L.$L = ($T) eventValues.getIndexedValues().get($L)" + nativeConversion,
                    objectName,
                    namedTypeName.getName(),
                    indexedEventWrapperType,
                    i);
        }

        for (int i = 0; i < nonIndexedParameters.size(); i++) {
            final NamedTypeName namedTypeName = nonIndexedParameters.get(i);
            final String nativeConversion;
            if (useNativeJavaTypes
                    && structClassNameMap.values().stream()
                            .map(ClassName::simpleName)
                            .noneMatch(
                                    name -> name.equals(namedTypeName.getTypeName().toString()))) {
                nativeConversion = ".getValue()";
            } else {
                nativeConversion = "";
            }
            final TypeName nonIndexedEventWrapperType;
            if (nonIndexedParameters.get(i).getType().equals("tuple")) {
                nonIndexedEventWrapperType =
                        structClassNameMap.get(namedTypeName.structIdentifier());
            } else if (nonIndexedParameters.get(i).getType().startsWith("tuple")
                    && nonIndexedParameters.get(i).getType().contains("[")) {
                nonIndexedEventWrapperType =
                        buildStructArrayTypeName(namedTypeName.namedType, true);
            } else {
                nonIndexedEventWrapperType =
                        getWrapperType(nonIndexedParameters.get(i).getTypeName());
            }
            builder.addStatement(
                    "$L.$L = ($T) eventValues.getNonIndexedValues().get($L)" + nativeConversion,
                    objectName,
                    namedTypeName.getName(),
                    nonIndexedEventWrapperType,
                    i);
        }
        return builder.build();
    }

    static TypeName buildTypeName(final String typeDeclaration) throws ClassNotFoundException {
        return buildTypeName(typeDeclaration, false);
    }

    static TypeName buildTypeName(final String typeDeclaration, final boolean primitives)
            throws ClassNotFoundException {

        final String solidityType = trimStorageDeclaration(typeDeclaration);

        final TypeReference<?> typeReference =
                TypeReference.makeTypeReference(solidityType, false, primitives);

        return TypeName.get(typeReference.getType());
    }

    private static String trimStorageDeclaration(final String type) {
        if (type.endsWith(" storage") || type.endsWith(" memory")) {
            return type.split(" ")[0];
        } else {
            return type;
        }
    }

    private static void buildVariableLengthReturnFunctionConstructor(
            final MethodSpec.Builder methodBuilder,
            final String functionName,
            final String inputParameters,
            final List<TypeName> outputParameterTypes,
            final boolean useUpperCase) {

        final List<Object> objects = new ArrayList<>();
        objects.add(Function.class);
        objects.add(Function.class);
        objects.add(funcNameToConst(functionName, useUpperCase));

        objects.add(Arrays.class);
        objects.add(Type.class);
        objects.add(inputParameters);

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (final TypeName outputParameterType : outputParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(outputParameterType);
        }

        final String asListParams =
                Collection.join(outputParameterTypes, ", ", typeName -> "new $T<$T>() {}");

        methodBuilder.addStatement(
                "final $T function = new $T($N, \n$T.<$T>asList($L), \n$T"
                        + ".<$T<?>>asList("
                        + asListParams
                        + "))",
                objects.toArray());
    }

    private void buildTupleResultContainer(
            final MethodSpec.Builder methodBuilder,
            final ParameterizedTypeName tupleType,
            final List<TypeName> outputParameterTypes) {

        final List<TypeName> typeArguments = tupleType.typeArguments;

        final CodeBlock.Builder tupleConstructor = CodeBlock.builder();
        tupleConstructor
                .addStatement(
                        "$T results = executeCallMultipleValueReturn(function)",
                        ParameterizedTypeName.get(List.class, Type.class))
                .add("return new $T(", tupleType)
                .add("$>$>");

        String resultStringNativeList = "\nconvertToNative(($T) results.get($L).getValue())";

        final int size = typeArguments.size();
        final ClassName classList = ClassName.get(List.class);

        for (int i = 0; i < size; i++) {
            final TypeName param = outputParameterTypes.get(i);
            TypeName convertTo = typeArguments.get(i);

            String resultStringSimple = "\n($T) results.get($L)";
            final TypeName finalConvertTo = convertTo;
            if (useNativeJavaTypes
                    && structClassNameMap.values().stream()
                            .map(ClassName::simpleName)
                            .noneMatch(name -> name.equals(finalConvertTo.toString()))) {
                resultStringSimple += ".getValue()";
            }

            String resultString = resultStringSimple;

            // If we use native java types we need to convert
            // elements of arrays to native java types too
            if (useNativeJavaTypes && param instanceof ParameterizedTypeName) {
                final ParameterizedTypeName oldContainer = (ParameterizedTypeName) param;
                final ParameterizedTypeName newContainer = (ParameterizedTypeName) convertTo;
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

        final TypeSpec callableType =
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

    private static CodeBlock buildVariableLengthEventInitializer(
            final String eventName, final List<NamedTypeName> parameterTypes) {

        final List<Object> objects = new ArrayList<>();
        objects.add(Event.class);
        objects.add(eventName);

        objects.add(Arrays.class);
        objects.add(TypeReference.class);
        for (final NamedTypeName parameterType : parameterTypes) {
            objects.add(TypeReference.class);
            objects.add(parameterType.getTypeName());
        }

        final String asListParams =
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

    private static String funcNameToConst(final String funcName, final boolean useUpperCase) {
        if (useUpperCase) {
            return FUNC_NAME_PREFIX + funcName.toUpperCase();
        } else {
            return FUNC_NAME_PREFIX + funcName;
        }
    }

    private static class NamedTypeName {
        private final TypeName typeName;
        private final AbiDefinition.NamedType namedType;

        NamedTypeName(AbiDefinition.NamedType namedType, TypeName typeName) {
            this.namedType = namedType;
            this.typeName = typeName;
        }

        public String getName() {
            return namedType.getName();
        }

        public String getType() {
            return namedType.getType();
        }

        public TypeName getTypeName() {
            return typeName;
        }

        public boolean isIndexed() {
            return namedType.isIndexed();
        }

        public int structIdentifier() {
            return namedType.structIdentifier();
        }
    }
}
