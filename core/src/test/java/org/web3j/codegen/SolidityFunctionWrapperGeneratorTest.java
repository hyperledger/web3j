package org.web3j.codegen;

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
        testCodeGeneration("greeter", "greeter");
    }

    @Test
    public void testContractsGeneration() throws Exception {
        testCodeGeneration("contracts", "HumanStandardToken");
    }

    @Test
    public void testSimpleStorageGeneration() throws Exception {
        testCodeGeneration("simplestorage", "SimpleStorage");
    }

    @Test
    public void testFibonacciGeneration() throws Exception {
        testCodeGeneration("fibonacci", "Fibonacci");
    }

    @Test
    public void testArrays() throws Exception {
        testCodeGeneration("arrays", "Arrays");
    }

    @Test
    public void testShipIt() throws Exception {
        testCodeGeneration("shipit", "ShipIt");
    }

    private void testCodeGeneration(String contractName, String inputFileName) throws Exception {
        SolidityFunctionWrapperGenerator.main(Arrays.asList(
                solidityBaseDir + "/" + contractName + "/build/" + inputFileName + ".bin",
                solidityBaseDir + "/" + contractName + "/build/" + inputFileName + ".abi",
                "-p", "org.web3j.unittests",
                "-o", tempDirPath
        ).toArray(new String[0])); // https://shipilev.net/blog/2016/arrays-wisdom-ancients/

        verifyGeneratedCode(tempDirPath + "/org/web3j/unittests/"
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
            assertTrue("Generated contract contains compile time error", task.call());
        }
    }
}
