package org.web3j.codegen;

import javax.lang.model.element.Modifier;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.web3j.abi.Contract;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AbiDefinition;

/**
 * Java wrapper source code generator for Solidity ABI format.
 */
public class Solidity {

    private String absFileLocation;
    private String destinationDirLocation;
    private String basePackageName;

    private Solidity(String absFileLocation, String destinationDirLocation, String basePackageName) {
        this.absFileLocation = absFileLocation;
        this.destinationDirLocation = destinationDirLocation;
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

        new Solidity(
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
        String className = new java.io.File(Solidity.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath())
                .getName();

        return String.format("Usage: %s <input abi file>.abi [-p|--package <base package name>] -o|--output <destination directory>\n",
                className);
    }

    public void generate() throws IOException {

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

    private static void generateSolidityWrappers(String contractName, List<AbiDefinition> functionDefinitions, String basePackageName) throws IOException {

        String className = contractName.substring(0, 1).toUpperCase() + contractName.substring(1);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(Contract.class);

        MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "contractAddress")
                .addParameter(Web3j.class, "web3j")
                .addStatement("super($N, $N)", "contractAddress", "web3j")
                .build();
        classBuilder.addMethod(constructorSpec);

        for (AbiDefinition functionDefinition:functionDefinitions) {
            if (!functionDefinition.getType().equals("function")) {
                continue;
            }

            String functionName = functionDefinition.getName();

            MethodSpec.Builder methodBuilder =
                    MethodSpec.methodBuilder(functionName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addException(InterruptedException.class)
                    .addException(ExecutionException.class);

            List<String> parameterNames = new ArrayList<>();

            for (AbiDefinition.NamedType namedType:functionDefinition.getInputs()) {
                String name = namedType.getName();
                Class<?> type = AbiTypes.getType(namedType.getType());
                methodBuilder.addParameter(type, name);
                parameterNames.add(name);
            }

            List<AbiDefinition.NamedType> outputs = functionDefinition.getOutputs();
            if (outputs.isEmpty()) {
                methodBuilder.returns(void.class);
            } else if (outputs.size() == 1) {
                AbiDefinition.NamedType namedType = outputs.get(0);
                Class<?> type = AbiTypes.getType(namedType.getType());
                methodBuilder.returns(type);
            } else {
                // TODO: Add support for tuple returns
                throw new UnsupportedOperationException("Multiple returns are not yet supported");
            }

            String params = parameterNames.stream().collect(Collectors.joining(", "));
            methodBuilder.addStatement("$T function = new $T($S, $L)",
                    Function.class, Function.class, functionName, params);
            methodBuilder.addStatement("return execute(function)");

            classBuilder.addMethod(methodBuilder.build());
        }

        JavaFile javaFile = JavaFile.builder(basePackageName, classBuilder.build())
                .build();

        javaFile.writeTo(System.out);
    }
}
