package org.web3j.codegen;

import javax.lang.model.element.Modifier;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;

import org.web3j.abi.Contract;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * Java wrapper source code generator for Solidity ABI format.
 */
public class SolidityFunctionWrapperGenerator extends Generator {

    private static final String BINARY = "BINARY";
    private static final String WEB3J = "web3j";
    private static final String CREDENTIALS = "credentials";
    private static final String INITIAL_VALUE = "initialValue";

    private static final String CODEGEN_WARNING = "<p>Auto generated code.<br>\n" +
            "<strong>Do not modifiy!</strong><br>\n" +
            "Please use {@link " + SolidityFunctionWrapperGenerator.class.getName() +
            "} to update.</p>\n";

    private String binaryFileLocation;
    private String absFileLocation;
    private Path destinationDirLocation;
    private String basePackageName;

    private SolidityFunctionWrapperGenerator(
            String binaryFileLocation,
            String absFileLocation,
            String destinationDirLocation,
            String basePackageName) {

        this.binaryFileLocation = binaryFileLocation;
        this.absFileLocation = absFileLocation;
        this.destinationDirLocation = Paths.get(destinationDirLocation);
        this.basePackageName = basePackageName;
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 6) {
            exitError(usage());
        }

        Optional<String> binaryFileLocation = parsePositionalArg(args, 0);
        Optional<String> absFileLocation = parsePositionalArg(args, 1);
        Optional<String> destinationDirLocation = parseParameterArgument(args, "-o", "--outputDir");
        Optional<String> basePackageName = parseParameterArgument(args, "-p", "--package");

        if (!binaryFileLocation.isPresent()
                || !absFileLocation.isPresent()
                || !destinationDirLocation.isPresent()
                || !basePackageName.isPresent()) {
            exitError(usage());
        }

        new SolidityFunctionWrapperGenerator(
                binaryFileLocation.get(),
                absFileLocation.get(),
                destinationDirLocation.get(),
                basePackageName.get())
        .generate();
    }

    private static Optional<String> parsePositionalArg(String[] args, int idx) {
        if (args != null && args.length > idx) {
            return Optional.of(args[idx]);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> parseParameterArgument(String[] args, String... parameters) {
        for (String parameter:parameters) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals(parameter)
                        && i + 1 < args.length) {
                    String parameterValue = args[i+1];
                    if (!parameterValue.startsWith("-")) {
                        return Optional.of(parameterValue);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static void exitError(String message) {
        System.err.println(message);
        System.exit(1);
    }

    private static String usage() throws URISyntaxException {
        String className = new java.io.File(
                SolidityFunctionWrapperGenerator.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath())
                .getName();

        return String.format("Usage: %s " +
                        "<input binary file>.bin <input abi file>.abi " +
                        "[-p|--package <base package name>] " +
                        "-o|--output <destination base directory>\n",
                className);
    }

    private void generate() throws IOException, ClassNotFoundException {

        File binaryFile = new File(binaryFileLocation);
        if (!binaryFile.exists()) {
            exitError("Invalid input binary file specified");
        }

        byte[] bytes = Files.readAllBytes(Paths.get(binaryFile.toURI()));
        String binary = new String(bytes);

        File absFile = new File(absFileLocation);
        if (!absFile.exists() || !absFile.canRead()) {
            exitError("Invalid input ABI file specified");
        }
        String fileName = absFile.getName();
        String contractName = getFileNameNoExtension(fileName);

        List<AbiDefinition> functionDefinitions = loadContractDefinition(absFile);

        if (functionDefinitions.isEmpty()) {
            exitError("Unable to parse input ABI file");
        } else {
            generateSolidityWrappers(binary, contractName, functionDefinitions, basePackageName);
        }
    }

    private static List<AbiDefinition> loadContractDefinition(File absFile)
            throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(absFile, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    static String getFileNameNoExtension(String fileName) {
        String[] splitName = fileName.split("\\.(?=[^\\.]*$)");
        return splitName[0];
    }

    private void generateSolidityWrappers(
            String binary, String contractName, List<AbiDefinition> functionDefinitions,
            String basePackageName) throws IOException, ClassNotFoundException {

        String className = contractName.substring(0, 1).toUpperCase() + contractName.substring(1);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc(CODEGEN_WARNING)
                .superclass(Contract.class)
                .addField(createBinaryDefinition(binary));

        MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "contractAddress")
                .addParameter(Web3j.class, WEB3J)
                .addParameter(Credentials.class, CREDENTIALS)
                .addStatement("super($N, $N, $N)", "contractAddress", WEB3J, CREDENTIALS)
                .build();
        classBuilder.addMethod(constructorSpec);

        classBuilder = createFunctionDefinitions(className, functionDefinitions, classBuilder);

        JavaFile javaFile = JavaFile.builder(basePackageName, classBuilder.build())
                .indent("    ")  // default indentation is two spaces
                .build();

        javaFile.writeTo(destinationDirLocation);
    }

    private FieldSpec createBinaryDefinition(String binary) {
        return FieldSpec.builder(String.class, BINARY)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", binary)
                .build();
    }

    private static TypeSpec.Builder createFunctionDefinitions(
            String className,
            List<AbiDefinition> functionDefinitions,
            TypeSpec.Builder classBuilder) throws ClassNotFoundException {

        for (AbiDefinition functionDefinition:functionDefinitions) {
            MethodSpec.Builder methodBuilder;

            switch (functionDefinition.getType()) {
                case "function":
                    methodBuilder = buildFunction(functionDefinition);
                    classBuilder.addMethod(methodBuilder.build());
                    break;
                case "event":
                    classBuilder.addMethod(buildEventFunction(functionDefinition));
                    break;
                case "constructor":
                    classBuilder.addMethod(buildConstructor(className, functionDefinition));
                    break;
            }
        }

        return classBuilder;
    }


    private static MethodSpec buildConstructor(
            String className, AbiDefinition functionDefinition) {

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("deploy")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(
                        ClassName.get(Future.class), TypeVariableName.get(className, Type.class)))
                .addParameter(Web3j.class, WEB3J)
                .addParameter(Credentials.class, CREDENTIALS)
                .addParameter(BigInteger.class, INITIAL_VALUE);

        String inputParams = addParameters(methodBuilder, functionDefinition.getInputs());
        methodBuilder.addStatement("$T encodedConstructor = $T.encodeConstructor(" +
                        "$T.asList($L)" +
                        ")",
                String.class, FunctionEncoder.class, Arrays.class, inputParams);

        methodBuilder.addStatement("return deployAsync($L.class, $L, $L, $L, encodedConstructor, $L)",
                className, WEB3J, CREDENTIALS, BINARY, INITIAL_VALUE);

        return methodBuilder.build();
    }

    private static String addParameters(
            MethodSpec.Builder builder, List<AbiDefinition.NamedType> inputs) {

        List<String> paramNames = new ArrayList<>(inputs.size());

        for (AbiDefinition.NamedType namedType:inputs) {
            String name = namedType.getName();
            String type = namedType.getType();

            TypeName typeName = buildTypeName(type);
            builder.addParameter(typeName, name);

            paramNames.add(name);
        }

        return paramNames.stream().collect(Collectors.joining(", "));
    }

    private static MethodSpec.Builder buildFunction(
            AbiDefinition functionDefinition) throws ClassNotFoundException {
        String functionName = functionDefinition.getName();

        MethodSpec.Builder methodBuilder =
                MethodSpec.methodBuilder(functionName)
                        .addModifiers(Modifier.PUBLIC);

        String inputParams = addParameters(methodBuilder, functionDefinition.getInputs());

        List<TypeName> outputParameterTypes = new ArrayList<>();
        for (AbiDefinition.NamedType namedType:functionDefinition.getOutputs()) {
            String type = namedType.getType();

            TypeName typeName = buildTypeName(type);
            outputParameterTypes.add(typeName);
        }

        if (functionDefinition.isConstant()) {
            methodBuilder = buildConstantFunction(
                    functionDefinition, methodBuilder, outputParameterTypes, inputParams);
        } else {
            methodBuilder = buildTransactionFunction(
                    functionDefinition, methodBuilder, inputParams);
        }

        return methodBuilder;
    }

    private static MethodSpec.Builder  buildConstantFunction(
            AbiDefinition functionDefinition,
            MethodSpec.Builder methodBuilder,
            List<TypeName> outputParameterTypes,
            String inputParams) throws ClassNotFoundException {

        String functionName = functionDefinition.getName();
        TypeVariableName typeVariableName = TypeVariableName.get("T", Type.class);

        if (outputParameterTypes.isEmpty()) {
            throw new RuntimeException("Only transactional methods should have void return types");
        } else if (outputParameterTypes.size() == 1) {
            methodBuilder.returns(ParameterizedTypeName.get(
                    ClassName.get(Future.class), outputParameterTypes.get(0)));

            methodBuilder.addStatement("$T function = " +
                            "new $T<>($S, \n$T.asList($L), \n$T.asList(new $T<$T>() {}))",
                    Function.class, Function.class, functionName,
                    Arrays.class, inputParams, Arrays.class, TypeReference.class,
                    outputParameterTypes.get(0));
            methodBuilder.addStatement("return executeCallSingleValueReturnAsync(function)");

        } else {
            methodBuilder.addTypeVariable(typeVariableName);
            methodBuilder.returns(ParameterizedTypeName.get(
                            ClassName.get(Future.class),
                            ParameterizedTypeName.get(
                                    ClassName.get(List.class), typeVariableName)));

            buildVariableLengthReturnFunctionConstructor(
                    methodBuilder, functionName, inputParams, outputParameterTypes);

            methodBuilder.addStatement("return executeCallMultipleValueReturnAsync(function)");
        }

        return methodBuilder;
    }

    private static MethodSpec.Builder buildTransactionFunction(
            AbiDefinition functionDefinition,
            MethodSpec.Builder methodBuilder,
            String inputParams) throws ClassNotFoundException {

        String functionName = functionDefinition.getName();

        methodBuilder.returns(ParameterizedTypeName.get(Future.class, TransactionReceipt.class));

        methodBuilder.addStatement("$T function = new $T<>($S, $T.asList($L), $T.emptyList())",
                Function.class, Function.class, functionName,
                Arrays.class, inputParams, Collections.class);
        methodBuilder.addStatement("return executeTransactionAsync(function)");

        return methodBuilder;
    }

    private static MethodSpec buildEventFunction(
            AbiDefinition functionDefinition) throws ClassNotFoundException {

        String functionName = functionDefinition.getName();
        String generatedFunctionName = "process" +
                functionName.substring(0, 1).toUpperCase() +
                functionName.substring(1) + "Event";

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(generatedFunctionName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TransactionReceipt.class, "transactionReceipt")
                .returns(EventValues.class);

        List<AbiDefinition.NamedType> inputs = functionDefinition.getInputs();

        List<TypeName> indexedParameters = new ArrayList<>();
        List<TypeName> nonIndexedParameters = new ArrayList<>();

        inputs.forEach(namedType -> {
            if (namedType.isIndexed()) {
                indexedParameters.add(buildTypeName(namedType.getType()));
            } else {
                nonIndexedParameters.add(buildTypeName(namedType.getType()));
            }
        });

        buildVariableLengthEventConstructor(
                methodBuilder, functionName, indexedParameters, nonIndexedParameters);

        return methodBuilder
                .addStatement("return extractEventParameters(event, transactionReceipt)")
                .build();
    }

    static TypeName buildTypeName(String type) {
        if (type.endsWith("]")) {
            String[] splitType = type.split("\\[");
            Class<?> baseType = AbiTypes.getType(splitType[0]);

            TypeName typeName;
            if (splitType[1].length() == 1) {
                typeName = ParameterizedTypeName.get(DynamicArray.class, baseType);
            } else {
                // Unfortunately we can't encode it's length as a type
                typeName = ParameterizedTypeName.get(StaticArray.class, baseType);
            }
            return typeName;
        } else {
            Class<?> cls = AbiTypes.getType(type);
            return ClassName.get(cls);
        }
    }

    private static void buildVariableLengthReturnFunctionConstructor(
            MethodSpec.Builder methodBuilder, String functionName, String inputParameters,
            List<TypeName> outputParameterTypes) throws ClassNotFoundException {

        List<Object> objects = new ArrayList<>();
        objects.add(Function.class);
        objects.add(Function.class);
        objects.add(functionName);

        objects.add(Arrays.class);
        objects.add(inputParameters);

        objects.add(Arrays.class);
        for (TypeName outputParameterType: outputParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(outputParameterType);
        }

        String asListParams = outputParameterTypes.stream().map(p -> "new $T<$T>() {}")
                .collect(Collectors.joining(", "));

        methodBuilder.addStatement("$T function = new $T<>($S, \n$T.asList($L), \n$T.asList(" +
                        asListParams + "))", objects.toArray());
    }

    private static void buildVariableLengthEventConstructor(
            MethodSpec.Builder methodBuilder, String eventName, List<TypeName> indexedParameterTypes,
            List<TypeName> nonIndexedParameterTypes) throws ClassNotFoundException {

        List<Object> objects = new ArrayList<>();
        objects.add(Event.class);
        objects.add(Event.class);
        objects.add(eventName);

        objects.add(Arrays.class);
        for (TypeName indexedParameterType: indexedParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(indexedParameterType);
        }

        objects.add(Arrays.class);
        for (TypeName indexedParameterType: nonIndexedParameterTypes) {
            objects.add(TypeReference.class);
            objects.add(indexedParameterType);
        }

        String indexedAsListParams = indexedParameterTypes.stream().map(p -> "new $T<$T>() {}")
                .collect(Collectors.joining(", "));
        String nonIndexedAsListParams = nonIndexedParameterTypes.stream().map(p -> "new $T<$T>() {}")
                .collect(Collectors.joining(", "));

        methodBuilder.addStatement("$T event = new $T($S, \n" +
                "$T.asList(" + indexedAsListParams + "),\n" +
                "$T.asList(" + nonIndexedAsListParams + "))", objects.toArray());
    }
}
