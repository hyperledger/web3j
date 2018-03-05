package org.web3j.codegen;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.cli.*;

import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.tx.Contract;
import org.web3j.utils.Files;
import org.web3j.utils.Strings;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.utils.Collection.tail;

/**
 * Java wrapper source code generator for Solidity ABI format.
 */
public class SolidityFunctionWrapperGenerator extends FunctionWrapperGenerator {
    static final String COMMAND_PREFIX = "solidity generate";

    static final Option OPTION_JAVA_TYPE = Option.builder("jt")
            .longOpt("javaTypes")
            .desc("use native java types")
            .hasArg(false)
            .required(false)
            .build();
    static final Option OPTION_SOLIDITY_TYPE = Option.builder("st")
            .longOpt("solidityTypes")
            .desc("use solidity types")
            .hasArg(false)
            .required(false)
            .build();
    static final Option OPTION_BIN_FILE = Option.builder("b")
            .longOpt("binFile")
            .desc("bin file with contract compiled code in order to generate deploy methods")
            .hasArg(true)
            .required(false)
            .build();
    static final Option OPTION_ABI_FILE = Option.builder("a")
            .longOpt("abiFile")
            .desc("abi file with contract definition")
            .hasArg(true)
            .required(true)
            .build();
    static final Option OPTION_OUTPUT = Option.builder("o")
            .longOpt("output")
            .desc("destination base directory")
            .hasArg(true)
            .required(true)
            .build();
    static final Option OPTION_PACKAGE = Option.builder("p")
            .longOpt("package")
            .desc("base package name")
            .hasArg(true)
            .required(true)
            .build();

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

    public static Options buildCommandLineOptions() {
        OptionGroup typeGroup = new OptionGroup();
        typeGroup.setRequired(false);
        typeGroup.addOption(OPTION_JAVA_TYPE);
        typeGroup.addOption(OPTION_SOLIDITY_TYPE);

        Options options = new Options();
        options.addOptionGroup(typeGroup);
        options.addOption(OPTION_BIN_FILE);
        options.addOption(OPTION_ABI_FILE);
        options.addOption(OPTION_OUTPUT);
        options.addOption(OPTION_PACKAGE);

        return options;
    }

    public static void run(String[] args) throws Exception {
        if (args.length < 1 || !args[0].equals("generate")) {
            exitErrorAndPrintHelp("error: \'generate\' is missing");
        } else {
            main(tail(args));
        }
    }

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(buildCommandLineOptions(), args);
            boolean useJavaNativeTypes = !line.hasOption(OPTION_SOLIDITY_TYPE.getOpt());
            String binaryFileLocation = line.getOptionValue(OPTION_BIN_FILE.getOpt(),
                    Contract.BIN_NOT_PROVIDED);
            String absFileLocation = line.getOptionValue(OPTION_ABI_FILE.getOpt());
            String destinationDirLocation = line.getOptionValue(OPTION_OUTPUT.getOpt());
            String basePackageName = line.getOptionValue(OPTION_PACKAGE.getOpt());

            new SolidityFunctionWrapperGenerator(
                    binaryFileLocation,
                    absFileLocation,
                    destinationDirLocation,
                    basePackageName,
                    useJavaNativeTypes)
                    .generate();
        } catch (ParseException exp) {
            exitErrorAndPrintHelp(exp.getMessage());
        }
    }

    static List<AbiDefinition> loadContractDefinition(File absFile)
            throws IOException {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        AbiDefinition[] abiDefinition = objectMapper.readValue(absFile, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    private void generate() throws IOException, ClassNotFoundException {
        String binary = Contract.BIN_NOT_PROVIDED;
        if (!binaryFileLocation.equals(Contract.BIN_NOT_PROVIDED)) {
            File binaryFile = new File(binaryFileLocation);

            byte[] bytes = Files.readBytes(new File(binaryFile.toURI()));
            binary = new String(bytes);
        }

        File absFile = new File(absFileLocation);
        if (!absFile.exists() || !absFile.canRead()) {
            exitError("Invalid input ABI file specified: " + absFileLocation);
        }
        String fileName = absFile.getName();
        String contractName = getFileNameNoExtension(fileName);
        byte[] bytes = Files.readBytes(new File(absFile.toURI()));
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

    private static void exitErrorAndPrintHelp(String message) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(COMMAND_PREFIX, buildCommandLineOptions());
        exitError(message);
    }
}