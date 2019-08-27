package org.web3j.console.project;

import org.junit.Assert;
import org.junit.Test;
import org.web3j.TempFileProvider;

import java.io.File;

public class ProjectStructureTest extends TempFileProvider {
    ProjectStructure projectStructure;

    private void init() throws Exception {
        setUp();
        projectStructure = new ProjectStructure(tempDirPath, "test.test", "Test");
        projectStructure.createDirectoryStructure();
 }

    @Test
    public void getRootTest() throws Exception {
        init();
        Assert.assertTrue(projectStructure.getRoot().equals(tempDirPath));

    }

    @Test
    public void getProjectRootTest() throws Exception {
        init();
        Assert.assertTrue(projectStructure.getProjectRoot().equals(tempDirPath + File.separator + "Test"));

    }

    @Test
    public void getPackageNameTest() throws Exception {
        init();
        Assert.assertTrue(projectStructure.getPackageName().equals("test.test"));

    }

    @Test
    public void getProjectName() throws Exception {
        init();
        Assert.assertTrue(projectStructure.getProjectName().equals("Test"));

    }

    @Test
    public void getTestPathTest() throws Exception {
        init();
        String testPath = tempDirPath
                + File.separator
                + "Test"
                + File.separator
                + "src"
                + File.separator
                + "test"
                + File.separator
                + "java"
                + File.separator
                + "test"
                + File.separator
                + "test"
                + File.separator;

        Assert.assertEquals(testPath, projectStructure.getTestPath());


    }

    @Test
    public void getSolidityPathTest() throws Exception {
        init();
        String solidityPath =
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "src"
                        + File.separator
                        + "main"
                        + File.separator
                        + "solidity"
                        + File.separator;
        Assert.assertEquals(solidityPath, projectStructure.getSolidityPath());
    }

    @Test
    public void getMainPathTest() throws Exception {
        init();
        String mainPath =
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "src"
                        + File.separator
                        + "main"
                        + File.separator
                        + "java"
                        + File.separator
                        + "test"
                        + File.separator
                        + "test"
                        + File.separator
                ;

        Assert.assertEquals(mainPath,projectStructure.getMainPath());

    }

    @Test
    public void getWrapperPathTest() throws Exception {
        init();
        String wrapperPath =
                tempDirPath
                        + File.separator
                        + "Test"
                        + File.separator
                        + "gradle"
                        + File.separator
                        + "wrapper"
                        + File.separator;

        Assert.assertEquals(wrapperPath,projectStructure.getWrapperPath());
    }
}
