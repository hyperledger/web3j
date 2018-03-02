package org.web3j.codegen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.Test;

import org.web3j.TempFileProvider;
import org.web3j.utils.Strings;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.web3j.codegen.SolidityFunctionWrapperGenerator.JAVA_TYPES_ARG;
import static org.web3j.codegen.SolidityFunctionWrapperGenerator.SOLIDITY_TYPES_ARG;
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
        testCodeGenerationJvmTypes("greeter", "greeter");
        testCodeGenerationSolidityTypes("greeter", "greeter");
    }

    @Test
    public void testContractsGeneration() throws Exception {
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

    private void testCodeGenerationJvmTypes(
            String contractName, String inputFileName) throws Exception {

        testCodeGeneration(
                contractName, inputFileName, "org.web3j.unittests.java", JAVA_TYPES_ARG);

    }

    private void testCodeGenerationSolidityTypes(
            String contractName, String inputFileName) throws Exception {

        testCodeGeneration(
                contractName, inputFileName, "org.web3j.unittests.solidity", SOLIDITY_TYPES_ARG);
    }

    private void testCodeGeneration(
            String contractName, String inputFileName, String packageName, String types)
            throws Exception {

        SolidityFunctionWrapperGenerator.main(Arrays.asList(
                types,
                solidityBaseDir + File.separator + contractName + File.separator
                        + "build" + File.separator + inputFileName + ".bin",
                solidityBaseDir + File.separator + contractName + File.separator
                        + "build" + File.separator + inputFileName + ".abi",
                "-p", packageName,
                "-o", tempDirPath
        ).toArray(new String[0])); // https://shipilev.net/blog/2016/arrays-wisdom-ancients/

        verifyGeneratedCode(tempDirPath + File.separator
                + packageName.replace('.', File.separatorChar) + File.separator
                + Strings.capitaliseFirstLetter(inputFileName) + ".java");
    }

    private void verifyGeneratedCode(String sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager =
                     compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(Arrays.asList(sourceFile));
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, diagnostics, null, null, compilationUnits);
            boolean result = task.call();

            System.out.println(diagnostics.getDiagnostics());
            assertTrue("Generated contract contains compile time error", result);
        }
    }
}
