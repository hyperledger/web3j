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
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.web3j.TempFileProvider;
import org.web3j.utils.Strings;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.web3j.codegen.FunctionWrapperGenerator.JAVA_TYPES_ARG;
import static org.web3j.codegen.FunctionWrapperGenerator.SOLIDITY_TYPES_ARG;

public class TruffleJsonFunctionWrapperGeneratorTest extends TempFileProvider {

    private static final String PackageName = "org.web3j.unittests.truffle.java";

    private String contractBaseDir;

    private static void verifyGeneratedCode(final String sourceFile) throws IOException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (final StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, null, null)) {
            final Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromStrings(
                            Collections.singletonList(sourceFile));
            final JavaCompiler.CompilationTask task =
                    compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
            assertTrue(task.call(), "Generated contract contains compile time error");
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        final URL url = SolidityFunctionWrapperGeneratorTest.class.getResource("/truffle");
        contractBaseDir = url.getPath();
    }

    @Test
    public void testLibGeneration() throws Exception {
        testCodeGenerationJvmTypes("MetaCoin", "ConvertLib");
        testCodeGenerationSolidtyTypes("MetaCoin", "ConvertLib");
    }

    @Test
    public void testContractGeneration() throws Exception {
        testCodeGenerationJvmTypes("MetaCoin", "MetaCoin");
        testCodeGenerationSolidtyTypes("MetaCoin", "MetaCoin");
    }

    @SuppressWarnings("SameParameterValue")
    private void testCodeGenerationJvmTypes(final String contractName, final String inputFileName)
            throws Exception {

        testCodeGeneration(contractName, inputFileName, PackageName, JAVA_TYPES_ARG);
    }

    @SuppressWarnings("SameParameterValue")
    private void testCodeGenerationSolidtyTypes(
            final String contractName, final String inputFileName) throws Exception {

        testCodeGeneration(contractName, inputFileName, PackageName, SOLIDITY_TYPES_ARG);
    }

    private void testCodeGeneration(
            final String contractName,
            final String inputFileName,
            final String packageName,
            final String types)
            throws Exception {

        TruffleJsonFunctionWrapperGenerator.main(
                Arrays.asList(
                                types,
                                ContractJsonParseTest.jsonFileLocation(
                                        contractBaseDir, contractName, inputFileName),
                                "-p",
                                packageName,
                                "-o",
                                tempDirPath)
                        .toArray(new String[0]));

        verifyGeneratedCode(
                tempDirPath
                        + File.separator
                        + packageName.replace('.', File.separatorChar)
                        + File.separator
                        + Strings.capitaliseFirstLetter(inputFileName)
                        + ".java");
    }
}
