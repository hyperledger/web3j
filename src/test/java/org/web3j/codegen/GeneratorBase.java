package org.web3j.codegen;


import java.io.File;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;

public abstract class GeneratorBase {
    private File tempDir;
    String tempDirPath;

    @Before
    public void setUp() throws Exception {
        tempDir = Files.createTempDirectory(
                SolidityFunctionWrapperGeneratorTest.class.getSimpleName()).toFile();
        tempDirPath = tempDir.getPath();
    }

    @After
    public void tearDown() throws Exception {
        for (File file:tempDir.listFiles()) {
            file.delete();
        }
        tempDir.delete();
    }
}
