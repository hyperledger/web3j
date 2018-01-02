package org.web3j.codegen;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.utils.Files;
import org.web3j.utils.Strings;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.utils.Collection.tail;

/**
 * Java wrapper source code generator for Solidity ABI format.
 */
public class SolidityFunctionWrapperGenerator extends FunctionWrapperGenerator {

    private static final String USAGE = "solidity generate "
            + "[--javaTypes|--solidityTypes] "
            + "<input binary file>.bin <input abi file>.abi "
            + "-p|--package <base package name> "
            + "-o|--output <destination base directory>";

    private final String binaryFileLocation;
    private final String absFileLocation;

    private SolidityFunctionWrapperGenerator(
            String binaryFileLocation,
            String absFileLocation,
            String destinationDirLocation,
            String basePackageName,
            boolean useJavaNativeTypes) {

        super(destinationDirLocation, basePackageName, useJavaNativeTypes);
        this.binaryFileLocation = binaryFileLocation;
        this.absFileLocation = absFileLocation;
    }

    public static void run(String[] args) throws Exception {
        if (args.length < 1 || !args[0].equals("generate")) {
            exitError(USAGE);
        } else {
            main(tail(args));
        }
    }

    public static void main(String[] args) throws Exception {

        String[] fullArgs;
        if (args.length == 6) {
            fullArgs = new String[args.length + 1];
            fullArgs[0] = JAVA_TYPES_ARG;
            System.arraycopy(args, 0, fullArgs, 1, args.length);
        } else {
            fullArgs = args;
        }

        if (fullArgs.length != 7) {
            exitError(USAGE);
        }

        boolean useJavaNativeTypes = useJavaNativeTypes(fullArgs[0], USAGE);

        String binaryFileLocation = parsePositionalArg(fullArgs, 1);
        String absFileLocation = parsePositionalArg(fullArgs, 2);
        String destinationDirLocation = parseParameterArgument(fullArgs, "-o", "--outputDir");
        String basePackageName = parseParameterArgument(fullArgs, "-p", "--package");

        if (binaryFileLocation.equals("")
                || absFileLocation.equals("")
                || destinationDirLocation.equals("")
                || basePackageName.equals("")) {
            exitError(USAGE);
        }

        new SolidityFunctionWrapperGenerator(
                binaryFileLocation,
                absFileLocation,
                destinationDirLocation,
                basePackageName,
                useJavaNativeTypes)
                .generate();
    }

    static List<AbiDefinition> loadContractDefinition(File absFile)
            throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(absFile, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    private void generate() throws IOException, ClassNotFoundException {

        File binaryFile = new File(binaryFileLocation);
        if (!binaryFile.exists()) {
            exitError("Invalid input binary file specified: " + binaryFileLocation);
        }

        byte[] bytes = Files.readBytes(new File(binaryFile.toURI()));
        String binary = new String(bytes);

        File absFile = new File(absFileLocation);
        if (!absFile.exists() || !absFile.canRead()) {
            exitError("Invalid input ABI file specified: " + absFileLocation);
        }
        String fileName = absFile.getName();
        String contractName = getFileNameNoExtension(fileName);
        bytes = Files.readBytes(new File(absFile.toURI()));
        String abi = new String(bytes);

        List<AbiDefinition> functionDefinitions = loadContractDefinition(absFile);

        if (functionDefinitions.isEmpty()) {
            exitError("Unable to parse input ABI file");
        } else {
            String className = Strings.capitaliseFirstLetter(contractName);
            System.out.printf("Generating " + basePackageName + "." + className + " ... ");
            new SolidityFunctionWrapper(useJavaNativeTypes).generateJavaFiles(
                    contractName, binary, abi, destinationDirLocation.toString(), basePackageName);
            System.out.println("File written to " + destinationDirLocation.toString() + "\n");
        }
    }


}