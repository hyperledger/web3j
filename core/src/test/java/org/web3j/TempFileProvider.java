package org.web3j;

import java.io.File;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;

/**
 * Base class for tests wishing to use temporary file locations.
 */
public class TempFileProvider {
    private File tempDir;
    protected String tempDirPath;

    @Before
    public void setUp() throws Exception {
        tempDir = new File("/Users/sam/development/work/blk_io/web3j/output");
        tempDir.mkdirs(); // TODO[Sam]: reinstate the below!
//        tempDir = Files.createTempDirectory(
//                TempFileProvider.class.getSimpleName()).toFile();
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
