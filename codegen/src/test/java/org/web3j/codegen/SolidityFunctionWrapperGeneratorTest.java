/*
 * Copyright 2019 Web3 Labs LTD.
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.Test;

import org.web3j.TempFileProvider;
import org.web3j.utils.Strings;

import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.web3j.codegen.FunctionWrapperGenerator.JAVA_TYPES_ARG;
import static org.web3j.codegen.FunctionWrapperGenerator.SOLIDITY_TYPES_ARG;
import static org.web3j.codegen.SolidityFunctionWrapperGenerator.COMMAND_GENERATE;
import static org.web3j.codegen.SolidityFunctionWrapperGenerator.COMMAND_SOLIDITY;
import static org.web3j.codegen.SolidityFunctionWrapperGenerator.getFileNameNoExtension;

public class SolidityFunctionWrapperGeneratorTest extends TempFileProvider {

    private String solidityBaseDir;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        URL url = SolidityFunctionWrapperGeneratorTest.class.getClass().getResource("/solidity");
        solidityBaseDir = url.getPath();
    }

    @Test
    public void testGetFileNoExtension() {
        assertThat(getFileNameNoExtension(""), is(""));
        assertThat(getFileNameNoExtension("file"), is("file"));
        assertThat(getFileNameNoExtension("file."), is("file"));
        assertThat(getFileNameNoExtension("file.txt"), is("file"));
    }

    @Test
    public void testGreeterGeneration() throws Exception {
        testCodeGenerationJvmTypes("greeter", "Greeter");
        testCodeGenerationSolidityTypes("greeter", "Greeter");
    }

    @Test
    public void testHumanStandardTokenGeneration() throws Exception {
        testCodeGenerationJvmTypes("contracts", "HumanStandardToken");
        testCodeGenerationSolidityTypes("contracts", "HumanStandardToken");
    }

    @Test
    public void testSimpleStorageGeneration() throws Exception {
        testCodeGenerationJvmTypes("simplestorage", "SimpleStorage");
        testCodeGenerationSolidityTypes("simplestorage", "SimpleStorage");
    }

    @Test
    public void testFibonacciGeneration() throws Exception {
        testCodeGenerationJvmTypes("fibonacci", "Fibonacci");
        testCodeGenerationSolidityTypes("fibonacci", "Fibonacci");
    }

    @Test
    public void testArrays() throws Exception {
        testCodeGenerationJvmTypes("arrays", "Arrays");
        testCodeGenerationSolidityTypes("arrays", "Arrays");
    }

    @Test
    public void testShipIt() throws Exception {
        testCodeGenerationJvmTypes("shipit", "ShipIt");
        testCodeGenerationSolidityTypes("shipit", "ShipIt");
    }

    @Test
    public void testMisc() throws Exception {
        testCodeGenerationJvmTypes("misc", "Misc");
        testCodeGenerationSolidityTypes("misc", "Misc");
    }

    @Test
    public void testContractsNoBin() throws Exception {
        testCodeGeneration("contracts", "HumanStandardToken", JAVA_TYPES_ARG, false);
        testCodeGeneration("contracts", "HumanStandardToken", SOLIDITY_TYPES_ARG, false);
    }

    @Test
    public void testGenerationCommandPrefixes() throws Exception {
        testCodeGeneration(
                Arrays.asList(COMMAND_SOLIDITY, COMMAND_GENERATE),
                "contracts",
                "HumanStandardToken",
                JAVA_TYPES_ARG,
                true);
        testCodeGeneration(
                Arrays.asList(COMMAND_GENERATE),
                "contracts",
                "HumanStandardToken",
                SOLIDITY_TYPES_ARG,
                true);
    }

    private void testCodeGenerationJvmTypes(String contractName, String inputFileName)
            throws Exception {
        testCodeGeneration(contractName, inputFileName, JAVA_TYPES_ARG, true);
    }

    private void testCodeGenerationSolidityTypes(String contractName, String inputFileName)
            throws Exception {
        testCodeGeneration(contractName, inputFileName, SOLIDITY_TYPES_ARG, true);
    }

    private void testCodeGeneration(
            String contractName, String inputFileName, String types, boolean useBin)
            throws Exception {
        testCodeGeneration(emptyList(), contractName, inputFileName, types, useBin);
    }

    private void testCodeGeneration(
            List<String> prefixes,
            String contractName,
            String inputFileName,
            String types,
            boolean useBin)
            throws Exception {
        String packageName = null;
        if (types.equals(JAVA_TYPES_ARG)) {
            packageName = "org.web3j.unittests.java";
        } else if (types.equals(SOLIDITY_TYPES_ARG)) {
            packageName = "org.web3j.unittests.solidity";
        }

        List<String> options = new ArrayList<>();
        options.addAll(prefixes);
        options.add(types);
        if (useBin) {
            options.add("-b");
            options.add(
                    solidityBaseDir
                            + File.separator
                            + contractName
                            + File.separator
                            + "build"
                            + File.separator
                            + inputFileName
                            + ".bin");
        }
        options.add("-a");
        options.add(
                solidityBaseDir
                        + File.separator
                        + contractName
                        + File.separator
                        + "build"
                        + File.separator
                        + inputFileName
                        + ".abi");
        options.add("-p");
        options.add(packageName);
        options.add("-o");
        options.add(tempDirPath);

        SolidityFunctionWrapperGenerator.main(options.toArray(new String[options.size()]));

        verifyGeneratedCode(
                tempDirPath
                        + File.separator
                        + packageName.replace('.', File.separatorChar)
                        + File.separator
                        + Strings.capitaliseFirstLetter(inputFileName)
                        + ".java");
    }

    private void verifyGeneratedCode(String sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromStrings(Arrays.asList(sourceFile));
            JavaCompiler.CompilationTask task =
                    compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
            boolean result = task.call();

            System.out.println(diagnostics.getDiagnostics());
            assertTrue("Generated contract contains compile time error", result);
        }
    }
}
