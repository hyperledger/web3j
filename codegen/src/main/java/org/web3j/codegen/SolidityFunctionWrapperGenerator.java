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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.tx.Contract;
import org.web3j.utils.Files;
import org.web3j.utils.Strings;

import static org.web3j.codegen.Console.exitError;
import static org.web3j.utils.Collection.tail;
import static picocli.CommandLine.Help.Visibility.ALWAYS;

/** Java wrapper source code generator for Solidity ABI format. */
public class SolidityFunctionWrapperGenerator extends FunctionWrapperGenerator {
    public static final String COMMAND_SOLIDITY = "solidity";
    public static final String COMMAND_GENERATE = "generate";
    public static final String COMMAND_PREFIX = COMMAND_SOLIDITY + " " + COMMAND_GENERATE;

    /*
     * Usage: solidity generate [-hV] [-jt] [-st] -a=<abiFile> [-b=<binFile>]
     * -o=<destinationFileDir> -p=<packageName>
     * -h, --help                 Show this help message and exit.
     * -V, --version              Print version information and exit.
     * -a, --abiFile=<abiFile>    abi file with contract definition.
     * -b, --binFile=<binFile>    bin file with contract compiled code in order to
     * generate deploy methods.
     * -o, --outputDir=<destinationFileDir>
     * destination base directory.
     * -p, --package=<packageName>
     * base package name.
     * -jt, --javaTypes       use native java types.
     * Default: true
     * -st, --solidityTypes   use solidity types.
     */

    private final File binFile;
    private final File abiFile;

    private final String contractName;

    private final int addressLength;

    private final boolean generateSendTxForCalls;

    public SolidityFunctionWrapperGenerator(
            final File binFile,
            final File abiFile,
            final File destinationDir,
            final String contractName,
            final String basePackageName,
            final boolean useJavaNativeTypes,
            final boolean useJavaPrimitiveTypes,
            final int addressLength) {

        this(
                binFile,
                abiFile,
                destinationDir,
                contractName,
                basePackageName,
                useJavaNativeTypes,
                useJavaPrimitiveTypes,
                false,
                Contract.class,
                addressLength);
    }

    protected SolidityFunctionWrapperGenerator(
            final File binFile,
            final File abiFile,
            final File destinationDir,
            final String contractName,
            final String basePackageName,
            final boolean useJavaNativeTypes,
            final boolean useJavaPrimitiveTypes,
            final boolean generateSendTxForCalls,
            final Class<? extends Contract> contractClass,
            final int addressLength) {

        super(
                contractClass,
                destinationDir,
                basePackageName,
                useJavaNativeTypes,
                useJavaPrimitiveTypes);

        this.binFile = binFile;
        this.abiFile = abiFile;
        this.contractName = contractName;
        this.addressLength = addressLength;
        this.generateSendTxForCalls = generateSendTxForCalls;
    }

    protected List<AbiDefinition> loadContractDefinition(final File absFile) throws IOException {
        final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        final AbiDefinition[] abiDefinition =
                objectMapper.readValue(absFile, AbiDefinition[].class);
        return Arrays.asList(abiDefinition);
    }

    public final void generate() throws IOException, ClassNotFoundException {
        String binary = Contract.BIN_NOT_PROVIDED;
        if (binFile != null) {
            final byte[] bytes = Files.readBytes(binFile);
            binary = new String(bytes);
        }
        final List<AbiDefinition> functionDefinitions = loadContractDefinition(abiFile);

        if (!functionDefinitions.isEmpty()) {

            final String className = Strings.capitaliseFirstLetter(contractName);
            System.out.print("Generating " + basePackageName + "." + className + " ... ");

            new SolidityFunctionWrapper(
                            useJavaNativeTypes,
                            useJavaPrimitiveTypes,
                            generateSendTxForCalls,
                            addressLength)
                    .generateJavaFiles(
                            contractClass,
                            contractName,
                            binary,
                            functionDefinitions,
                            destinationDirLocation.toString(),
                            basePackageName,
                            null);

            System.out.println("File written to " + destinationDirLocation.toString() + "\n");
        } else {
            System.out.println("Ignoring empty ABI file: " + abiFile.getName() + ".abi" + "\n");
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals(COMMAND_SOLIDITY)) {
            args = tail(args);
        }

        if (args.length > 0 && args[0].equals(COMMAND_GENERATE)) {
            args = tail(args);
        }

        CommandLine.run(new PicocliRunner(), args);
    }

    @Command(
            name = COMMAND_PREFIX,
            mixinStandardHelpOptions = true,
            version = "4.0",
            sortOptions = false)
    private static class PicocliRunner implements Runnable {
        @Option(
                names = {"-a", "--abiFile"},
                description = "abi file with contract definition.",
                required = true)
        private File abiFile;

        @Option(
                names = {"-b", "--binFile"},
                description =
                        "bin file with contract compiled code "
                                + "in order to generate deploy methods.")
        private File binFile;

        @Option(
                names = {"-c", "--contractName"},
                description = "contract name (defaults to ABI file name).")
        private String contractName;

        @Option(
                names = {"-o", "--outputDir"},
                description = "destination base directory.",
                required = true)
        private File destinationFileDir;

        @Option(
                names = {"-p", "--package"},
                description = "base package name.",
                required = true)
        private String packageName;

        @Option(
                names = {"-jt", JAVA_TYPES_ARG},
                description = "use native Java types.",
                showDefaultValue = ALWAYS)
        private boolean javaTypes = true;

        @Option(
                names = {"-st", SOLIDITY_TYPES_ARG},
                description = "use Solidity types.")
        private boolean solidityTypes;

        @Override
        public void run() {
            try {
                // grouping is not implemented in picocli yet(planned for 3.1), therefore
                // simply check if solidityTypes were requested
                final boolean useJavaTypes = !(solidityTypes);

                if (contractName == null || contractName.isEmpty()) {
                    contractName = getFileNameNoExtension(abiFile.getName());
                }

                final boolean primitiveTypes = false;
                final int addressLength = Address.DEFAULT_LENGTH / Byte.SIZE;
                new SolidityFunctionWrapperGenerator(
                                binFile,
                                abiFile,
                                destinationFileDir,
                                contractName,
                                packageName,
                                useJavaTypes,
                                primitiveTypes,
                                addressLength)
                        .generate();
            } catch (final Exception e) {
                exitError(e);
            }
        }
    }
}
