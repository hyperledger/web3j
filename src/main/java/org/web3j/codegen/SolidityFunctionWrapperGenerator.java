package org.web3j.codegen;

import javax.lang.model.element.Modifier;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;

import org.web3j.abi.Contract;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AbiDefinition;

/**
 * Java wrapper source code generator for Solidity ABI format.
 */
public class SolidityFunctionWrapperGenerator extends Generator {

    private static final String CODEGEN_WARNING = "<p>Auto generated code.<br>\n" +
            "<strong>Do not modifiy!</strong><br>\n" +
            "Please use {@link " + SolidityFunctionWrapperGenerator.class.getName() + "} to update.</p>\n";

    private String absFileLocation;
    private Path destinationDirLocation;
    private String basePackageName;

    private SolidityFunctionWrapperGenerator(
            String absFileLocation, String destinationDirLocation, String basePackageName) {

        this.absFileLocation = absFileLocation;
        this.destinationDirLocation = Paths.get(destinationDirLocation);
        this.basePackageName = basePackageName;
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 5) {
            exitError(usage());
        }

        Optional<String> absFileLocation = parsePositionalArg(args, 0);
        Optional<String> destinationDirLocation = parseParameterArgument(args, "-o", "--outputDir");
        Optional<String> basePackageName = parseParameterArgument(args, "-p", "--package");

        if (!absFileLocation.isPresent()
                || !destinationDirLocation.isPresent()
                || !basePackageName.isPresent()) {
            exitError(usage());
        }

        new SolidityFunctionWrapperGenerator(
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
        String className = new java.io.File(SolidityFunctionWrapperGenerator.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath())
                .getName();

        return String.format("Usage: %s <input abi file>.abi [-p|--package <base package name>] " +
                        "-o|--output <destination base directory>\n",
                className);
    }

    private void generate() throws IOException {

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
            generateSolidityWrappers(contractName, functionDefinitions, basePackageName);
        }
    }

    private static List<AbiDefinition> loadContractDefinition(File absFile)
            throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(absFile, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    private static String getFileNameNoExtension(String fileName) {
        String[] splitName = fileName.split("\\.(?=[^\\.]+$)");
        return splitName[0];
    }

    private void generateSolidityWrappers(
            String contractName, List<AbiDefinition> functionDefinitions,
            String basePackageName) throws IOException {

        String className = contractName.substring(0, 1).toUpperCase() + contractName.substring(1);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc(CODEGEN_WARNING)
                .superclass(Contract.class);

        MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "contractAddress")
                .addParameter(Web3j.class, "web3j")
                .addStatement("super($N, $N)", "contractAddress", "web3j")
                .build();
        classBuilder.addMethod(constructorSpec);

        classBuilder = createFunctionDefinitions(functionDefinitions, classBuilder);

        JavaFile javaFile = JavaFile.builder(basePackageName, classBuilder.build())
                .build();

        javaFile.writeTo(destinationDirLocation);
    }

    private static TypeSpec.Builder createFunctionDefinitions(
            List<AbiDefinition> functionDefinitions, TypeSpec.Builder classBuilder) {

        for (AbiDefinition functionDefinition:functionDefinitions) {
            if (!functionDefinition.getType().equals("function")) {
                continue;
            }

            String functionName = functionDefinition.getName();

            MethodSpec.Builder methodBuilder =
                    MethodSpec.methodBuilder(functionName)
                            .addModifiers(Modifier.PUBLIC)
                            .addException(InterruptedException.class)
                            .addException(ExecutionException.class);

            List<String> inputParameterNames = new ArrayList<>();

            for (AbiDefinition.NamedType namedType:functionDefinition.getInputs()) {
                String name = namedType.getName();
                Class<?> type = AbiTypes.getType(namedType.getType());
                methodBuilder.addParameter(type, name);
                inputParameterNames.add(name);
            }

            List<Class<?>> outputParameterTypes = new ArrayList<>();
            for (AbiDefinition.NamedType namedType:functionDefinition.getOutputs()) {
                Class<?> type = AbiTypes.getType(namedType.getType());
                outputParameterTypes.add(type);
            }

            String inputParams = inputParameterNames.stream().collect(Collectors.joining(", "));

            TypeVariableName typeVariableName = TypeVariableName.get("T", Type.class);

            if (outputParameterTypes.isEmpty()) {
                methodBuilder.returns(void.class);

                methodBuilder.addStatement("$T function = new $T<>($S, $T.asList($L), $T.emptyList())",
                        Function.class, Function.class, functionName,
                        Arrays.class, inputParams, Collections.class);
                methodBuilder.addStatement("return executeSingleValueReturn(function)");

            } else if (outputParameterTypes.size() == 1) {
                methodBuilder.returns(outputParameterTypes.get(0));

                methodBuilder.addStatement("$T function = new $T<>($S, $T.asList($L), $T.asList($T.class))",
                        Function.class, Function.class, functionName,
                        Arrays.class, inputParams, Arrays.class, outputParameterTypes.get(0));
                methodBuilder.addStatement("return executeSingleValueReturn(function)");

            } else {
                methodBuilder.addTypeVariable(typeVariableName);
                methodBuilder.returns(
                        ParameterizedTypeName.get(ClassName.get(List.class), typeVariableName));

                buildVariableLengthReturnFunctionConstructor(
                        methodBuilder, functionName, inputParams, outputParameterTypes);

                methodBuilder.addStatement("return executeMultipleValueReturn(function)");
            }

            classBuilder.addMethod(methodBuilder.build());
        }

        return classBuilder;
    }

    private static void buildVariableLengthReturnFunctionConstructor(
            MethodSpec.Builder methodBuilder, String functionName, String inputParameters,
            List<Class<?>> outputParameterTypes) {

        Object[] objects = new Object[6 + outputParameterTypes.size()];
        objects[0] = Function.class;
        objects[1] = Function.class;
        objects[2] = functionName;
        objects[3] = Arrays.class;
        objects[4] = inputParameters;
        objects[5] = Arrays.class;
        for (int i = 0; i < outputParameterTypes.size(); i++) {
            objects[i + 6] = outputParameterTypes.get(i);
        }

        String asListParams = outputParameterTypes.stream().map(p -> "$T.class")
                .collect(Collectors.joining(", "));

        methodBuilder.addStatement("$T function = new $T<>($S, $T.asList($L), $T.asList(" +
                        asListParams + "))", objects);
    }
}
