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

import org.junit.Before;
import org.junit.Test;

import org.web3j.TempFileProvider;
import org.web3j.utils.Strings;

import static org.junit.Assert.assertTrue;
import static org.web3j.codegen.FunctionWrapperGenerator.JAVA_TYPES_ARG;
import static org.web3j.codegen.FunctionWrapperGenerator.SOLIDITY_TYPES_ARG;

public class TruffleJsonFunctionWrapperGeneratorTest extends TempFileProvider {

    private static final String PackageName = "org.web3j.unittests.truffle.java";

    private String contractBaseDir;

    private static void verifyGeneratedCode(String sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(Collections.singletonList(sourceFile));
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, diagnostics, null, null, compilationUnits);
            assertTrue("Generated contract contains compile time error", task.call());
        }
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        URL url = SolidityFunctionWrapperGeneratorTest.class.getClass().getResource("/truffle");
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
    private void testCodeGenerationJvmTypes(
            String contractName, String inputFileName) throws Exception {

        testCodeGeneration(
                contractName, inputFileName, PackageName, JAVA_TYPES_ARG);

    }

    @SuppressWarnings("SameParameterValue")
    private void testCodeGenerationSolidtyTypes(
            String contractName, String inputFileName) throws Exception {

        testCodeGeneration(
                contractName, inputFileName, PackageName, SOLIDITY_TYPES_ARG);

    }

    private void testCodeGeneration(
            String contractName, String inputFileName, String packageName, String types)
            throws Exception {

        TruffleJsonFunctionWrapperGenerator.main(Arrays.asList(
                types,
                ContractJsonParseTest
                        .jsonFileLocation(contractBaseDir, contractName, inputFileName),
                "-p", packageName,
                "-o", tempDirPath
        ).toArray(new String[0]));

        verifyGeneratedCode(tempDirPath + File.separator
                + packageName.replace('.', File.separatorChar) + File.separator
                + Strings.capitaliseFirstLetter(inputFileName) + ".java");
    }


}